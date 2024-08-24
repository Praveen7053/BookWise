package com.bookWise.controller;

import com.bookWise.util.fileUtil.FileRequest;
import com.bookWise.util.fileUtil.FileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@RestController
@RequestMapping("/api/files")
public class FileRestController {

    private static final String BASE_UPLOAD_DIR = "C:\\usr\\local\\bookWiseFile\\";

    @PostMapping("/preview")
    public ResponseEntity<FileResponse> previewFile(@RequestBody FileRequest fileRequest) {
        Path filePath = Paths.get(BASE_UPLOAD_DIR).resolve(fileRequest.getPdfPath()).normalize();
        try {
            byte[] fileContent = Files.readAllBytes(filePath);
            String encodedFileContent = Base64.getEncoder().encodeToString(fileContent);

            FileResponse fileResponse = new FileResponse();
            fileResponse.setFileName(filePath.getFileName().toString());
            fileResponse.setFileContent(encodedFileContent);

            return ResponseEntity.ok(fileResponse);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
