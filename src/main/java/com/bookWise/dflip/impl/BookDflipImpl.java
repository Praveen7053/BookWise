package com.bookWise.dflip.impl;

import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookEncounter;
import com.bookWise.service.book.BookService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class BookDflipImpl {

    private static final Logger log = LoggerFactory.getLogger(BookDflipImpl.class);

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Autowired
    private BookService bookService;

    // Inject the base storage path from application.properties
    @Value("${bookwise.storage.path}")
    private String bookwiseStoragePath;

    public Map<String, Object> previewBookPdf(Long bookEncounterId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            log.info("Starting PDF preview for bookEncounterId: {}", bookEncounterId);
            
            if (bookEncounterId == null || bookEncounterId <= 0) {
                log.warn("Attempted to preview PDF with invalid bookEncounterId: {}", bookEncounterId);
                response.put("success", false);
                response.put("message", "Invalid book encounter ID");
                return response;
            }

            // 1. Fetch the book encounter to get the PDF path
            BookEncounter bookEncounter = (BookEncounter) bookWiseDAO.find(BookEncounter.class, bookEncounterId.intValue());
            log.info("Book encounter found: {}", bookEncounter != null ? "YES" : "NO");

            if (bookEncounter == null) {
                log.error("Book encounter not found for ID: {}", bookEncounterId);
                response.put("success", false);
                response.put("message", "Book encounter not found");
                return response;
            }

            log.info("Book title: {}", bookEncounter.getBookTitle());
            log.info("PDF path: {}", bookEncounter.getPdfPath());
            log.info("Storage path: {}", bookwiseStoragePath);

            if (StringUtils.isBlank(bookEncounter.getPdfPath())) {
                log.error("PDF path is missing for book encounter ID: {}", bookEncounterId);
                response.put("success", false);
                response.put("message", "PDF path is missing");
                return response;
            }

            // 2. Construct the full path and read the file into a byte array
            Path pdfFilePath = Paths.get(bookwiseStoragePath, bookEncounter.getPdfPath());
            log.info("Full PDF file path: {}", pdfFilePath);
            log.info("File exists: {}", Files.exists(pdfFilePath));

            if (!Files.exists(pdfFilePath)) {
                log.error("File does not exist at the specified path: {}", pdfFilePath);
                response.put("success", false);
                response.put("message", "PDF file not found");
                return response;
            }

            byte[] pdfContent = Files.readAllBytes(pdfFilePath);
            log.info("PDF content size: {} bytes", pdfContent.length);

            if (pdfContent.length == 0) {
                log.error("PDF file is empty: {}", pdfFilePath);
                response.put("success", false);
                response.put("message", "PDF file is empty");
                return response;
            }

            // 3. Encode the PDF content as Base64
            String encodedPdfContent = Base64.getEncoder().encodeToString(pdfContent);
            
            // 4. Return the response with Base64-encoded content
            response.put("success", true);
            response.put("fileContent", encodedPdfContent);
            response.put("fileName", bookEncounter.getBookTitle() + ".pdf");
            
            log.info("Successfully previewed PDF '{}' for bookEncounterId: {} ({} bytes)", 
                    bookEncounter.getBookTitle() + ".pdf", bookEncounterId, pdfContent.length);
            
            return response;

        } catch (NoSuchFileException e) {
            log.error("File not found for bookEncounterId {}: {}", bookEncounterId, e.getMessage());
            response.put("success", false);
            response.put("message", "PDF file not found");
            return response;
        } catch (IOException e) {
            log.error("IO Error reading PDF for bookEncounterId {}: {}", bookEncounterId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error reading PDF file");
            return response;
        } catch (Exception e) {
            log.error("Error previewing PDF for bookEncounterId: {} - {}", bookEncounterId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error processing PDF");
            return response;
        }
    }
}