package com.uit.hotelmanagement.controllers;

import com.uit.hotelmanagement.dtos.PhotoDTO;
import com.uit.hotelmanagement.services.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/photo")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @Operation(
            summary = "Add the photos for a specific room",
            description = "We will add the photos for a specific room by providing the roomId and the files",
            tags = {"Photo", "post"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = List.class),
                                        mediaType = "application/json")
            )
    })
    @PostMapping(value = "/addPhoto/{roomId}", consumes = "multipart/form-data")
    public ResponseEntity<List<PhotoDTO>> addPhotoHandler(@PathVariable Integer roomId,
                                                          @RequestPart MultipartFile[] files) {
        return ResponseEntity.status(HttpStatus.CREATED).body(photoService.addPhoto(roomId, files));
    }

    @Operation(
            summary = "Get all photos",
            description = "We will get all photos from database",
            tags = {"Photo", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getAllPhotos")
    public ResponseEntity<List<PhotoDTO>> getAllPhotosHandler() {
        return ResponseEntity.status(HttpStatus.OK).body(photoService.getAllPhotos());
    }

    @Operation(
            summary = "Get a specific photo by Id",
            description = "We will get a specific photo by providing Id",
            tags = {"Photo", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PhotoDTO.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getPhotoById/{photoId}")
    public ResponseEntity<PhotoDTO> getPhotoByIdHandler(@PathVariable Integer photoId) {
        return ResponseEntity.status(HttpStatus.OK).body(photoService.getPhotoById(photoId));
    }

    @Operation(
            summary = "Get a specific photo by photo name",
            description = "We will get a specific photo by providing photo name",
            tags = {"Photo", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PhotoDTO.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getPhotoByName/{photoName}")
    public ResponseEntity<PhotoDTO> getPhotoByPhotoNameHandler(@PathVariable String photoName) {
        return ResponseEntity.status(HttpStatus.OK).body(photoService.getPhotoByPhotoName(photoName));
    }

    @Operation(
            summary = "Get all photos of a room by roomId",
            description = "We will get all photos of a room by providing roomId",
            tags = {"Photo", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getAllPhotosByRoomId/{roomId}")
    public ResponseEntity<List<PhotoDTO>> getAllPhotosByRoomIdHandler(@PathVariable Integer roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(photoService.getAllPhotosByRoomId(roomId));
    }

    @Operation(
            summary = "Delete a specific photo by Id",
            description = "We will delete a specific photo by providing Id",
            tags = {"Photo", "delete"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Map.class),
                            mediaType = "application/json")
            )
    })
    @DeleteMapping("/deletePhoto/{photoId}")
    public ResponseEntity<Map<String, String>> deletePhotoHandler(@PathVariable Integer photoId) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(photoService.deletePhoto(photoId));
    }
}
