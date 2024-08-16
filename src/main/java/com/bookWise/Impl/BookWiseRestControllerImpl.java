package com.bookWise.Impl;

import com.bookWise.SecurityConfig.loginUserConfig.BookWiseLoginUser;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookEncounter;
import com.bookWise.util.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
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

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> bookData = mapper.readValue(bookDataJson, Map.class);

            String bookEncounterId = (String) bookData.getOrDefault("bookEncounterId", "");
            String bookTitle = (String) bookData.getOrDefault("bookTitle", "");
            String authorName = (String) bookData.getOrDefault("authorName", "");
            String isbnNumber = (String) bookData.getOrDefault("isbnNumber", "");
            String bookPrice = (String) bookData.getOrDefault("bookPrice", "");
            String bookCategory = (String) bookData.getOrDefault("bookCategory", "");
            String publicationDate = (String) bookData.getOrDefault("publicationDate", "");
            String bookLanguage = (String) bookData.getOrDefault("bookLanguage", "");
            String bookDescription = (String) bookData.getOrDefault("bookDescription", "");
            String bookCoverBase64 = (String) bookData.getOrDefault("bookCover", "");
            String bookPdfBase64 = (String) bookData.getOrDefault("bookPdf", "");

            // Handle bookCover and bookPdf if they are not empty
            String bookCoverPath = null;
            String bookPdfPath = null;
            if (StringUtils.isNotEmpty(bookCoverBase64)) {
                bookCoverPath = FileUtils.saveBase64ToFile(bookCoverBase64, "BookUpload", bookTitle);
            }

            if (StringUtils.isNotEmpty(bookPdfBase64)) {
                bookPdfPath = FileUtils.saveBase64ToFile(bookPdfBase64, "BookUpload", bookTitle);
            }else {
                response.put("success", false);
                response.put("message", "Book pdf should not blank.");
                return response;
            }

            BookEncounter bookEncounter = null;
            if (StringUtils.isNoneBlank(bookEncounterId) && Integer.parseInt(bookEncounterId) > 0) {
                bookEncounter = (BookEncounter) bookWiseDAO.find(BookEncounter.class, bookEncounterId);
            } else {
                bookEncounter = new BookEncounter();
            }

            bookEncounter.setBookTitle(bookTitle);
            bookEncounter.setBookAuthor(authorName);
            bookEncounter.setBookIsbnNumber(isbnNumber);
            bookEncounter.setBookCategory(bookCategory);
            bookEncounter.setBookPrice(bookPrice);
            bookEncounter.setPublicationDate(Timestamp.valueOf(publicationDate));
            bookEncounter.setBookLanguage(bookLanguage);
            bookEncounter.setBookDescription(bookDescription);
            bookEncounter.setFrontPageImagePath(bookCoverPath);
            bookEncounter.setPdfPath(bookPdfPath);
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

}
