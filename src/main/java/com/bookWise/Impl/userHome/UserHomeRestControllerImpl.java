package com.bookWise.Impl.userHome;

import com.bookWise.common.dto.ImageResponse;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookEncounter;
import com.bookWise.service.book.BookService;
import com.bookWise.util.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserHomeRestControllerImpl {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Autowired
    private BookService bookService;

    public ResponseEntity<Map<String, Object>> loadAllBooks(String json) {
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

            int start = (page - 1) * size;
            List<BookEncounter> books = bookService.loadAllBookEncounter(
                    0, start, size, title, bookId, timeFilter, sortBy);

            long totalBooks = bookService.countBooks(
                    0, title, bookId, timeFilter);

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
