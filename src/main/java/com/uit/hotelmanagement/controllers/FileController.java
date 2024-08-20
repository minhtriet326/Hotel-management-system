package com.uit.hotelmanagement.controllers;

import com.uit.hotelmanagement.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @Operation(
            summary = "Upload a file",
            description = "We will upload a file by providing a MultipartFile",
            tags = {"File", "post"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = Map.class),
                                        mediaType = "application/json")
            )
    })
    @PostMapping(value = "/uploadFile", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> uploadFileHandler(@RequestPart MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("Message", "File with name: " + fileService.uploadFile(file) + " has been uploaded successfully!"));
    }

    @Operation(
            summary = "Watch a photo by photoName",
            description = "We can watch a photo by providing photoName",
            tags = {"File", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = byte.class),
                                        mediaType = "image/png")
            )
    })
    @GetMapping(value = "/{photoName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> serviceFileHandler(@PathVariable String photoName) {
        try {
            FileInputStream resource = fileService.getResourceFile(photoName);

            byte[] photo = StreamUtils.copyToByteArray(resource);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                    .body(photo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
