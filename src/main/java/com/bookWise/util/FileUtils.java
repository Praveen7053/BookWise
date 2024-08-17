package com.bookWise.util;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileUtils {

    // Specify your base upload directory
    private static final String BASE_UPLOAD_DIR = "C:\\usr\\local\\bookWiseFile\\";

    // Map of MIME types to file extensions
    private static final Map<String, String> MIME_TYPE_MAP = new HashMap<>();
    static {
        MIME_TYPE_MAP.put("image/png", "png");
        MIME_TYPE_MAP.put("image/jpeg", "jpg");
        MIME_TYPE_MAP.put("image/gif", "gif");
        MIME_TYPE_MAP.put("image/webp", "webp"); // Added WebP support
        MIME_TYPE_MAP.put("application/pdf", "pdf");
        // Add more MIME types as needed
    }

    private static String sanitizeFileName(String name) {
        // Replace spaces and other special characters with underscores
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public static String saveBase64ToFile(String base64Data, String mainDir, String bookTitle, String uniqueSuffix) throws IOException {
        // Extract MIME type from the base64 string
        String[] parts = base64Data.split(",");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid base64 data");
        }
        String mimeType = parts[0].split(";")[0].split(":")[1];

        // Determine the file extension based on MIME type
        String fileExtension = MIME_TYPE_MAP.get(mimeType);
        if (fileExtension == null) {
            throw new IllegalArgumentException("Unsupported MIME type: " + mimeType);
        }

        // Decode the base64 data
        byte[] data = Base64.getDecoder().decode(parts[1]);

        // Sanitize the book title
        String sanitizedBookTitle = sanitizeFileName(bookTitle + "_" + uniqueSuffix);

        // Create the main directory if it does not exist
        File mainDirFile = new File(BASE_UPLOAD_DIR + mainDir);
        if (!mainDirFile.exists()) {
            mainDirFile.mkdirs();
        }

        // Create a subdirectory named after the sanitized book title if it does not exist
        File bookDirFile = new File(mainDirFile, sanitizedBookTitle);
        if (!bookDirFile.exists()) {
            bookDirFile.mkdirs();
        }

        // Create a unique file name with the determined extension
        String fileName = UUID.randomUUID().toString() + "." + fileExtension;
        File file = new File(bookDirFile, fileName);

        // Write the decoded data to the file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        }
        return file.getAbsolutePath(); // Return the full path of the file
    }

    public static void deleteFolder(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {
            File file = new File(filePath);
            File targetFolder = file.getParentFile(); // Get the parent directory of the file

            // Check if the target folder exists and is a directory
            if (targetFolder.exists() && targetFolder.isDirectory()) {
                deleteDirectoryRecursively(targetFolder);
                boolean deleted = targetFolder.delete();
            }
        } else {
            System.out.println("File path is blank");
        }
    }

    private static void deleteDirectoryRecursively(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectoryRecursively(file); // Recursively delete subdirectories
                } else {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        System.out.println("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            }
        }
        boolean deleted = directory.delete();
        if (!deleted) {
            System.out.println("Failed to delete directory: " + directory.getAbsolutePath());
        }
    }

}
