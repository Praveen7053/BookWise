package com.bookWise.controller;

import com.bookWise.util.fileUtil.CombinedFileResponse;
import com.bookWise.util.fileUtil.FileRequest;
import com.bookWise.util.fileUtil.FileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bookWise.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileRestController {

    private static final String BASE_UPLOAD_DIR = "C:\\usr\\local\\bookWiseFile\\";

    private static final Map<String, String> MIME_TYPE_MAP = Map.of(
            "image/png", "png",
            "image/jpeg", "jpg",
            "image/gif", "gif",
            "image/webp", "webp"
            // Add more MIME types as needed
    );

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

    @PostMapping("/getBookImageNPdf")
    public ResponseEntity<CombinedFileResponse> getSavedBookImageNPdf(@RequestBody FileRequest fileRequest) {
        CombinedFileResponse combinedResponse = new CombinedFileResponse();

        // Handle the image file
        String imagePathStr = fileRequest.getImagePath();
        if (imagePathStr != null && !imagePathStr.isEmpty()) {
            Path imagePath = Paths.get(BASE_UPLOAD_DIR).resolve(imagePathStr).normalize();
            String imageExtension = FileUtils.getFileExtension(imagePathStr);
            String imageMimeType = FileUtils.getMimeTypeFromExtension(imageExtension);

            if (imageMimeType != null && Files.exists(imagePath)) {
                try {
                    byte[] imageContent = Files.readAllBytes(imagePath);
                    String encodedImageContent = Base64.getEncoder().encodeToString(imageContent);
                    combinedResponse.setImageContent(encodedImageContent);
                    combinedResponse.setImageMimeType(imageMimeType);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        }

        // Handle the PDF file
        String pdfPathStr = fileRequest.getPdfPath();
        if (pdfPathStr != null && !pdfPathStr.isEmpty()) {
            Path pdfPath = Paths.get(BASE_UPLOAD_DIR).resolve(pdfPathStr).normalize();
            String pdfExtension = FileUtils.getFileExtension(pdfPathStr);
            String pdfMimeType = FileUtils.getMimeTypeFromExtension(pdfExtension);

            if ("application/pdf".equals(pdfMimeType) && Files.exists(pdfPath)) {
                try {
                    byte[] pdfContent = Files.readAllBytes(pdfPath);
                    String encodedPdfContent = Base64.getEncoder().encodeToString(pdfContent);
                    combinedResponse.setPdfContent(encodedPdfContent);
                    combinedResponse.setPdfMimeType(pdfMimeType);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        }

        return ResponseEntity.ok(combinedResponse);
    }

}
