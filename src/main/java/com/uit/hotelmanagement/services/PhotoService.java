package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.PhotoDTO;
import com.uit.hotelmanagement.dtos.RoomDTO;
import com.uit.hotelmanagement.entities.Photo;
import com.uit.hotelmanagement.entities.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PhotoService {
    List<Photo> saveAllPhotos(Room room, MultipartFile[] files);
    void deleteAllPhotos(Room room);
    // Post
    List<PhotoDTO> addPhoto(Integer roomId, MultipartFile[] files);
    // Get
    List<PhotoDTO> getAllPhotos();
    PhotoDTO getPhotoById(Integer photoId);
    PhotoDTO getPhotoByPhotoName(String photoName);
    List<PhotoDTO> getAllPhotosByRoomId(Integer roomId);
    //Delete
    Map<String, String> deletePhoto(Integer photoId) throws IOException;
    PhotoDTO photoToDTO(Photo photo);
}
