package com.bookWise.Impl;

import com.bookWise.SecurityConfig.loginUserConfig.BookWiseLoginUser;
import com.bookWise.common.dto.ImageResponse;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookEncounter;
import com.bookWise.service.book.BookService;
import com.bookWise.util.BookUtils;
import com.bookWise.util.DateConstant;
import com.bookWise.util.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BookWiseRestControllerImpl {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Autowired
    private BookUtils bookUtils;

    @Autowired
    private BookService bookService;

    @Transactional
    public Map<String, Object> saveUpdateNewBooks(String bookDataJson) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser user = (BookWiseLoginUser) authentication.getPrincipal();
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateConstant.DATE_FORMAT_YYYY_MM_DD);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> bookData = mapper.readValue(bookDataJson, Map.class);

            String bookEncounterId = (String) bookData.getOrDefault("bookEncounterId", "0");
            BookEncounter bookEncounter = null;
            boolean isNewBook = true;

            // Load existing book if editing
            if (StringUtils.isNoneBlank(bookEncounterId) && Integer.parseInt(bookEncounterId) > 0) {
                bookEncounter = (BookEncounter) bookWiseDAO.find(BookEncounter.class, Integer.parseInt(bookEncounterId));
                if (bookEncounter == null) {
                    response.put("success", false);
                    response.put("message", "Book not found for editing.");
                    return response;
                }
                isNewBook = false;
            } else {
                bookEncounter = new BookEncounter();
            }

            // Extract data from request
            String bookTitle = (String) bookData.getOrDefault("bookTitle", "");
            String authorName = (String) bookData.getOrDefault("authorName", "");
            String isbnNumber = (String) bookData.getOrDefault("isbnNumber", "");
            String bookPrice = (String) bookData.getOrDefault("bookPrice", "");
            String bookCategory = (String) bookData.getOrDefault("bookCategory", "");
            String publicationDateStr = (String) bookData.getOrDefault("publicationDate", "");
            String bookLanguage = (String) bookData.getOrDefault("bookLanguage", "");
            String bookDescription = (String) bookData.getOrDefault("bookDescription", "");
            String bookCoverBase64 = (String) bookData.getOrDefault("bookCover", "");
            String bookPdfBase64 = (String) bookData.getOrDefault("bookPdf", "");
            String numberOfPages = (String) bookData.getOrDefault("numberOfPages", "");

            if(StringUtils.isBlank(bookTitle)){
                response.put("success", false);
                response.put("message", "Please enter book title.");
            }

            if(StringUtils.isBlank(authorName)){
                response.put("success", false);
                response.put("message", "Please enter book author name.");
            }

            if(StringUtils.isBlank(bookCategory)){
                response.put("success", false);
                response.put("message", "Please select book category.");
            }

            // Handle file paths
            String bookCoverPath = bookEncounter.getFrontPageImagePath(); // Preserve existing cover path
            String bookPdfPath = bookEncounter.getPdfPath(); // Preserve existing PDF path
            String uniqueSuffix = String.valueOf(System.currentTimeMillis());

            // Handle cover image if new one provided
            if (StringUtils.isNotEmpty(bookCoverBase64)) {
                bookCoverPath = FileUtils.saveBase64ToFile(bookCoverBase64, "BookUpload", bookTitle, uniqueSuffix);
            }

            // Handle PDF file
            if (StringUtils.isNotEmpty(bookPdfBase64)) {
                bookPdfPath = FileUtils.saveBase64ToFile(bookPdfBase64, "BookUpload", bookTitle, uniqueSuffix);
            } else if (isNewBook) {
                // Require PDF only for new books
                response.put("success", false);
                response.put("message", "Book PDF is required for new books.");
                return response;
            }

            // Update book fields
            bookEncounter.setBookTitle(bookTitle);
            bookEncounter.setBookAuthor(authorName);
            bookEncounter.setBookIsbnNumber(isbnNumber);
            bookEncounter.setBookCategory(bookCategory);
            bookEncounter.setBookPrice(bookPrice);
            bookEncounter.setBookLanguage(bookLanguage);
            bookEncounter.setBookDescription(bookDescription);
            bookEncounter.setFrontPageImagePath(bookCoverPath);
            bookEncounter.setPdfPath(bookPdfPath);
            bookEncounter.setBookPageNumber(numberOfPages);

            // Handle publication date
            if (StringUtils.isNotBlank(publicationDateStr)) {
                Date publicationDate = dateFormat.parse(publicationDateStr);
                bookEncounter.setPublicationDate(new Timestamp(publicationDate.getTime()));
            }

            // Update metadata
            bookEncounter.setUpdatedById(String.valueOf(user.getUserId()));
            bookEncounter.setUploadedByName(user.getUserName());
            bookEncounter.setUploadedTime(new Timestamp(new Date().getTime()));

            // Save or update the book
            bookWiseDAO.saveOrUpdate(bookEncounter);

            response.put("success", true);
            response.put("message", "Book " + (isNewBook ? "saved" : "updated") + " successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error while saving or updating book: " + e.getMessage());
        }
        return response;
    }

    public ResponseEntity<Map<String, Object>> getSellerUploadedBooks(int page, int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser user = (BookWiseLoginUser) authentication.getPrincipal();

            if (user != null) {
                int start = (page - 1) * size;
                List<BookEncounter> bookEncounters = bookService.loadAllBookEncounter(user.getUserId(), start, size, "", "", "", "");
                long totalBooks = bookService.countBooks(user.getUserId(), "", "", "");

                int totalPages = (int) Math.ceil((double) totalBooks / size);

                response.put("success", true);
                response.put("books", bookEncounters);
                response.put("currentPage", page);
                response.put("totalPages", totalPages);
                response.put("totalItems", totalBooks);
            } else {
                response.put("success", false);
                response.put("message", "User logged out. Please Sign in again!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error while getting seller books.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @Transactional
    public Map<String, Object> deleteUploadedBooks(String json) {
        Map<String, Object> response = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> bookData = mapper.readValue(json, Map.class);
            int bookID = (int) bookData.getOrDefault("bookId", "");

            BookEncounter bookEncounter = (BookEncounter) bookWiseDAO.find(BookEncounter.class, bookID);
            if (bookEncounter != null) {
                bookWiseDAO.delete(bookEncounter);

                String frontPageImagePath = bookEncounter.getFrontPageImagePath();
                String pdfPath = bookEncounter.getPdfPath();

                if (frontPageImagePath != null && !frontPageImagePath.isEmpty()) {
                    FileUtils.deleteFolder(frontPageImagePath);
                }

                if(pdfPath != null && !pdfPath.isEmpty()){
                    FileUtils.deleteFolder(pdfPath);
                }

                response.put("success", true);
                response.put("message", "Book and associated files deleted successfully.");
            } else {
                response.put("success", false);
                response.put("message", "Book not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error while deleting book and files.");
        }

        return response;
    }

    public ResponseEntity<Map<String, Object>> getSellerBooks(String json) {
        Map<String, Object> response = new HashMap<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> bookData = mapper.readValue(json, Map.class);

            String title = (String) bookData.getOrDefault("title", "");
            String bookId = (String) bookData.getOrDefault("bookId", "");
            String timeFilter = (String) bookData.getOrDefault("timeFilter", "");
            String sortBy = (String) bookData.getOrDefault("sortBy", "");
            int page = (int) bookData.getOrDefault("page", 0);
            int size = (int) bookData.getOrDefault("size", 0);
            String uploadedByName = (String) bookData.getOrDefault("uploadedByName", "");

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser user = (BookWiseLoginUser) authentication.getPrincipal();

            if (user != null) {
                int start = (page - 1) * size;

                // Get filtered and sorted books using existing method
                List<BookEncounter> books = bookService.loadAllBookEncounter(
                        user.getUserId(), start, size, title, bookId, timeFilter, sortBy);

                // Get total count using existing method
                long totalBooks = bookService.countBooks(
                        user.getUserId(), title, bookId, timeFilter);

                // Convert books to DTOs and add additional information
                List<Map<String, Object>> bookDTOs = books.stream()
                        .map(this::convertToBookDTO)
                        .collect(Collectors.toList());

                response.put("books", bookDTOs);
                response.put("totalPages", (int) Math.ceil((double) totalBooks / size));
                response.put("totalElements", totalBooks);
                response.put("currentPage", page);
                response.put("success", true);

                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "User logged out. Please Sign in again!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error fetching books: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    public Map<String, Object> convertToBookDTO(BookEncounter book) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("bookEncounterId", book.getBookEncounterId());
        dto.put("bookTitle", book.getBookTitle());
        dto.put("bookAuthor", book.getBookAuthor());
        dto.put("bookPrice", book.getBookPrice());
        dto.put("bookCategory", book.getBookCategory());
        dto.put("frontPageImagePath", book.getFrontPageImagePath());
        dto.put("uploadedByName", book.getUploadedByName());
        dto.put("uploadedTime", book.getUploadedTime().toString());
        dto.put("bookIsbnNumber", book.getBookIsbnNumber());
        dto.put("bookDescription", book.getBookDescription());
        dto.put("bookLanguage", book.getBookLanguage());
        dto.put("publicationDate", book.getPublicationDate());
        dto.put("bookPageNumber", book.getBookPageNumber());
        dto.put("pdfPath", book.getPdfPath());
        dto.put("status", "ACTIVE"); // You might want to add actual status logic

        if (StringUtils.isNotBlank(book.getFrontPageImagePath())) {
            ImageResponse imageResponse = FileUtils.getImageContentAndMimeType(book.getFrontPageImagePath(), "BookUpload");
            if (imageResponse != null) {
                dto.put("coverImageContent", imageResponse.getImageContent());
                dto.put("coverImageMimeType", imageResponse.getImageMimeType());
            }
        }

        return dto;
    }
}
