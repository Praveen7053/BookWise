package com.bookWise.Impl;

import com.bookWise.SecurityConfig.loginUserConfig.BookWiseLoginUser;
import com.bookWise.bookDetails.dto.BookEncounterDTO;
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
import org.springframework.web.multipart.MultipartFile;

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
    public Map<String, Object> saveUpdateNewBooks(
            String bookDataJson,
            MultipartFile bookCover,
            MultipartFile bookPdf
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser user = (BookWiseLoginUser) authentication.getPrincipal();
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateConstant.DATE_FORMAT_YYYY_MM_DD);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> bookData = mapper.readValue(bookDataJson, Map.class);

            String bookEncounterId = (String) bookData.getOrDefault("bookEncounterId", "0");
            BookEncounter bookEncounter;
            boolean isNewBook = true;

            // Load existing book if editing
            if (StringUtils.isNotBlank(bookEncounterId) && Integer.parseInt(bookEncounterId) > 0) {
                bookEncounter = (BookEncounter) bookWiseDAO.find(
                        BookEncounter.class,
                        Integer.parseInt(bookEncounterId)
                );

                if (bookEncounter == null) {
                    response.put("success", false);
                    response.put("message", "Book not found for editing.");
                    return response;
                }
                isNewBook = false;
            } else {
                bookEncounter = new BookEncounter();
            }

            // Extract data from JSON
            String bookTitle = (String) bookData.getOrDefault("bookTitle", "");
            String authorName = (String) bookData.getOrDefault("authorName", "");
            String isbnNumber = (String) bookData.getOrDefault("isbnNumber", "");
            String bookPrice = (String) bookData.getOrDefault("bookPrice", "");
            String bookCategory = (String) bookData.getOrDefault("bookCategory", "");
            String publicationDateStr = (String) bookData.getOrDefault("publicationDate", "");
            String bookLanguage = (String) bookData.getOrDefault("bookLanguage", "");
            String bookDescription = (String) bookData.getOrDefault("bookDescription", "");
            String numberOfPages = (String) bookData.getOrDefault("numberOfPages", "");

            // Basic validations (existing behavior preserved)
            if (StringUtils.isBlank(bookTitle)) {
                response.put("success", false);
                response.put("message", "Please enter book title.");
                return response;
            }

            if (StringUtils.isBlank(authorName)) {
                response.put("success", false);
                response.put("message", "Please enter book author name.");
                return response;
            }

            if (StringUtils.isBlank(bookCategory)) {
                response.put("success", false);
                response.put("message", "Please select book category.");
                return response;
            }

            // Preserve existing file paths
            String bookCoverPath = bookEncounter.getFrontPageImagePath();
            String bookPdfPath = bookEncounter.getPdfPath();
            String uniqueSuffix = String.valueOf(System.currentTimeMillis());

            // ✅ Handle cover file (NEW way)
            if (bookCover != null && !bookCover.isEmpty()) {
                bookCoverPath = FileUtils.saveMultipartFile(
                        bookCover,
                        "BookUpload",
                        bookTitle,
                        uniqueSuffix
                );
            }

            // ✅ Handle PDF file (NEW way)
            if (bookPdf != null && !bookPdf.isEmpty()) {
                bookPdfPath = FileUtils.saveMultipartFile(
                        bookPdf,
                        "BookUpload",
                        bookTitle,
                        uniqueSuffix
                );
            } else if (isNewBook) {
                response.put("success", false);
                response.put("message", "Book PDF is required for new books.");
                return response;
            }

            // Update entity fields
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

            // Publication date
            if (StringUtils.isNotBlank(publicationDateStr)) {
                Date publicationDate = dateFormat.parse(publicationDateStr);
                bookEncounter.setPublicationDate(
                        new Timestamp(publicationDate.getTime())
                );
            }

            // Metadata (unchanged behavior)
            bookEncounter.setUpdatedById(String.valueOf(user.getUserId()));
            bookEncounter.setUploadedByName(user.getUserName());
            bookEncounter.setUploadedTime(new Timestamp(System.currentTimeMillis()));

            // Save
            bookWiseDAO.saveOrUpdate(bookEncounter);

            response.put("success", true);
            response.put("message", "Book " + (isNewBook ? "saved" : "updated") + " successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error while saving or updating book.");
        }

        return response;
    }

    public ResponseEntity<Map<String, Object>> getSellerUploadedBooks(int page, int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser user = (BookWiseLoginUser) authentication.getPrincipal();

            if (user != null) {
                int start = (page - 1) * size;
                List<BookEncounter> bookEncounters = bookService.loadAllBookEncounter(user.getUserId(), start, size, "", "", "", "");
                long totalBooks = bookService.countBooks(user.getUserId(), "", "", "");

                List<BookEncounterDTO> bookDTOs = bookEncounters.stream()
                        .map(bookService::mapToBookEncounterDTO)
                        .collect(Collectors.toList());

                int totalPages = (int) Math.ceil((double) totalBooks / size);

                response.put("success", true);
                response.put("books", bookDTOs);
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
