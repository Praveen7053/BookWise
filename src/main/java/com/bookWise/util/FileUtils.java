package com.bookWise.util;

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

    public static String saveBase64ToFile(String base64Data, String mainDir, String bookTitle) throws IOException {
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

        // Create the main directory if it does not exist
        File mainDirFile = new File(BASE_UPLOAD_DIR + mainDir);
        if (!mainDirFile.exists()) {
            mainDirFile.mkdirs();
        }

        // Create a subdirectory named after the book title if it does not exist
        File bookDirFile = new File(mainDirFile, bookTitle);
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
}
