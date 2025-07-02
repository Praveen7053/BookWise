package com.bookWise.bookDetails.impl;

import com.bookWise.bookDetails.dto.BookEncounterDTO;
import com.bookWise.common.dto.ImageResponse;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookEncounter;
import com.bookWise.service.book.BookService;
import com.bookWise.util.BookUtils;
import com.bookWise.util.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookDetailsRestImpl {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Autowired
    private BookUtils bookUtils;

    @Autowired
    private BookService bookService;

    public ResponseEntity<Map<String, Object>> getUserBookEncounter(int bookEncounterId) {
        Map<String, Object> response = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();

            if(bookEncounterId == 0){
                response.put("success", false);
                response.put("message", "BookEncounterId is zero!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (bookEncounterId > 0) {
                BookEncounter bookEncounter = bookService.loadBookEncounter(bookEncounterId);
                if(bookEncounter == null){
                    response.put("success", false);
                    response.put("message", "Error while loading book details!");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }

                BookEncounterDTO bookEncounterDTO = new BookEncounterDTO();
                bookEncounterDTO.setBookEncounterId(bookEncounter.getBookEncounterId());
                bookEncounterDTO.setBookTitle(bookEncounter.getBookTitle());
                bookEncounterDTO.setBookAuthor(bookEncounter.getBookAuthor());
                bookEncounterDTO.setBookIsbnNumber(bookEncounter.getBookIsbnNumber());
                bookEncounterDTO.setBookPrice(bookEncounter.getBookPrice());
                bookEncounterDTO.setBookPageNumber(bookEncounter.getBookPageNumber());
                bookEncounterDTO.setBookCategory(bookEncounter.getBookCategory());
                bookEncounterDTO.setPublicationDate(bookEncounter.getPublicationDate());
                bookEncounterDTO.setBookLanguage(bookEncounter.getBookLanguage());
                bookEncounterDTO.setBookDescription(bookEncounter.getBookDescription());
                bookEncounterDTO.setPdfPath(bookEncounter.getPdfPath());
                bookEncounterDTO.setFrontPageImagePath(bookEncounter.getFrontPageImagePath());
                bookEncounterDTO.setUploadedByName(bookEncounter.getUploadedByName());
                bookEncounterDTO.setUpdatedById(bookEncounter.getUpdatedById());
                bookEncounterDTO.setUploadedTime(bookEncounter.getUploadedTime());

                if (StringUtils.isNotBlank(bookEncounter.getFrontPageImagePath())) {
                    ImageResponse imageResponse = FileUtils.getImageContentAndMimeType(bookEncounter.getFrontPageImagePath(), "BookUpload");
                    if (imageResponse != null) {
                        bookEncounterDTO.setCoverImageContent(imageResponse.getImageContent());
                        bookEncounterDTO.setCoverImageMimeType(imageResponse.getImageMimeType());
                    }
                }

                response.put("success", true);
                response.put("bookDetails", mapper.writeValueAsString(bookEncounterDTO));
            } else {
                response.put("success", false);
                response.put("message", "User logged out. Please Sign in again!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error while loading books details.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
