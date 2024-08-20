package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.PhotoDTO;
import com.uit.hotelmanagement.entities.Photo;
import com.uit.hotelmanagement.entities.Room;
import com.uit.hotelmanagement.exceptions.FileServiceException;
import com.uit.hotelmanagement.exceptions.ResourceNotFoundException;
import com.uit.hotelmanagement.repositories.PhotoRepository;
import com.uit.hotelmanagement.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService{
    private final FileService fileService;
    private final PhotoRepository photoRepository;
    private final RoomRepository roomRepository;

    @Value("${base.url}")
    private String baseUrl;
    @Override
    public List<Photo> saveAllPhotos(Room room, MultipartFile[] files) {
        List<Photo> photos = new ArrayList<>();

        if (files != null) {
            Arrays.stream(files).forEach(file -> {
                try {
                    String photoName = fileService.uploadFile(file);

                    Photo photo = Photo.builder()
                            .name(photoName)
                            .room(room)
                            .build();

                    photos.add(photo);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            return photoRepository.saveAll(photos);
        }
        return photos;
    }

    @Override
    public void deleteAllPhotos(Room room) {
        List<Photo> deletePhotos = photoRepository.findByRoom(room);

        deletePhotos.forEach(photo -> {
            try {
                fileService.deleteFile(photo.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        photoRepository.deleteAll(deletePhotos);
    }

    @Override
    public List<PhotoDTO> addPhoto(Integer roomId, MultipartFile[] files) {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", Integer.toString(roomId)));

        List<Photo> savedPhotos = new ArrayList<>();

        if (files != null) {
            if (files.length + existingRoom.getPhotos().size() > 5) {
                throw new FileServiceException("Can't upload more than 5 photos!");
            } else {
               savedPhotos = saveAllPhotos(existingRoom, files);
            }
        }

        return savedPhotos.stream().map(this::photoToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PhotoDTO> getAllPhotos() {
        List<Photo> existingPhotos = photoRepository.findAll();
        return existingPhotos.stream().map(this::photoToDTO).collect(Collectors.toList());
    }

    @Override
    public PhotoDTO getPhotoById(Integer photoId) {
        Photo existingPhoto = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo", "photoId", Integer.toString(photoId)));
        return photoToDTO(existingPhoto);
    }

    @Override
    public PhotoDTO getPhotoByPhotoName(String photoName) {
        Photo existingPhoto = photoRepository.findByName(photoName)
                .orElseThrow(() -> new ResourceNotFoundException("Photo", "photo name", photoName));
        return photoToDTO(existingPhoto);
    }

    @Override
    public List<PhotoDTO> getAllPhotosByRoomId(Integer roomId) {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", Integer.toString(roomId)));

        List<Photo> photos = photoRepository.findByRoom(existingRoom);

        return photos.stream().map(this::photoToDTO).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> deletePhoto(Integer photoId) throws IOException {
        Photo existingPhoto = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo", "photoId", Integer.toString(photoId)));

        fileService.deleteFile(existingPhoto.getName());

        photoRepository.delete(existingPhoto);

        return Map.of("Message", "Photo with Id " + photoId + " has been deleted successfully!");
    }

    @Override
    public PhotoDTO photoToDTO(Photo photo) {
        return PhotoDTO.builder()
                .photoId(photo.getPhotoId())
                .name(photo.getName())
                .roomNumber(photo.getRoom().getRoomNumber())
                .photoUrl(baseUrl + "/file/" + photo.getName())
                .build();
    }
}
