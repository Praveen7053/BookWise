package com.bookWise.controller;

import com.bookWise.Impl.BookWiseRestControllerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping(value = "/api/book/actions")
public class BookWiseRestController {

    @Autowired
    private BookWiseRestControllerImpl bookWiseRestControllerImpl;

    @PostMapping(value = "/saveUpdateNewBooks",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> saveUpdateNewBooks(
            @RequestParam("bookData") String bookDataJson,
            @RequestParam(value = "bookCover", required = false) MultipartFile bookCover,
            @RequestParam(value = "bookPdf", required = false) MultipartFile bookPdf) {
        return bookWiseRestControllerImpl.saveUpdateNewBooks(bookDataJson, bookCover, bookPdf);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getSellerUploadedBooks")
    public ResponseEntity<Map<String, Object>> getSellerUploadedBooks(@RequestBody Map<String, Integer> paginationParams) {
        int page = paginationParams.getOrDefault("page", 1);
        int size = paginationParams.getOrDefault("size", 10);
        return bookWiseRestControllerImpl.getSellerUploadedBooks(page, size);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteUploadedBooks")
    public Map<String, Object> deleteUploadedBooks(@RequestBody String json) {
        return bookWiseRestControllerImpl.deleteUploadedBooks(json);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getSellerBooks")
    public ResponseEntity<Map<String, Object>> getSellerBooks(@RequestBody String json) {
        return bookWiseRestControllerImpl.getSellerBooks(json);
    }
}
