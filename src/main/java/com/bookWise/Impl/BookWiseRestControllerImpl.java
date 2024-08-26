package com.bookWise.Impl;

import com.bookWise.SecurityConfig.loginUserConfig.BookWiseLoginUser;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookEncounter;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BookWiseRestControllerImpl {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

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

            // Handle bookCover and bookPdf if they are not empty
            String bookCoverPath = null;
            String bookPdfPath = null;
            String uniqueSuffix = String.valueOf(System.currentTimeMillis()); // Unique suffix based on current timestamp
            if (StringUtils.isNotEmpty(bookCoverBase64)) {
                bookCoverPath = FileUtils.saveBase64ToFile(bookCoverBase64, "BookUpload", bookTitle, uniqueSuffix);
            }

            if (StringUtils.isNotEmpty(bookPdfBase64)) {
                bookPdfPath = FileUtils.saveBase64ToFile(bookPdfBase64, "BookUpload", bookTitle, uniqueSuffix);
            }else {
                response.put("success", false);
                response.put("message", "Book pdf should not blank.");
                return response;
            }

            BookEncounter bookEncounter = null;
            if (StringUtils.isNoneBlank(bookEncounterId) && Integer.parseInt(bookEncounterId) > 0) {
                bookEncounter = (BookEncounter) bookWiseDAO.find(BookEncounter.class, Integer.parseInt(bookEncounterId));
            } else {
                bookEncounter = new BookEncounter();
            }

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

            if(StringUtils.isNotBlank(publicationDateStr)){
                Date publicationDate = dateFormat.parse(publicationDateStr);
                bookEncounter.setPublicationDate(new Timestamp(publicationDate.getTime()));
            }

            bookEncounter.setUpdatedById(String.valueOf(user.getUserId()));
            bookEncounter.setUploadedByName(user.getUserName());
            bookEncounter.setUploadedTime(new Timestamp(new Date().getTime()));
            bookWiseDAO.saveOrUpdate(bookEncounter);

            response.put("success", true);
            response.put("message", "Books saved or updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error while saving or updating books.");
            return response;
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
                List<BookEncounter> bookEncounters = findBooksByUser(user.getUserId(), start, size);
                long totalBooks = countBooksByUser(user.getUserId());

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
    public List<BookEncounter> findBooksByUser(int userId, int start, int size) {
        Session session = null;
        try {
            session = bookWiseDAO.openSesstion();
            String query = "FROM BookEncounter WHERE updatedById = :userId";
            return session.createQuery(query, BookEncounter.class)
                    .setParameter("userId", String.valueOf(userId))
                    .setFirstResult(start)
                    .setMaxResults(size)
                    .list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Transactional
    public long countBooksByUser(int userId) {
        Session session = null;
        try {
            session = bookWiseDAO.openSesstion();

            String query = "SELECT COUNT(*) FROM BookEncounter WHERE updatedById = :userId";
            return (Long) session.createQuery(query)
                    .setParameter("userId", String.valueOf(userId))
                    .uniqueResult();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return 0;
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

}
