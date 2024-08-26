package com.bookWise.controller;

import com.bookWise.Impl.BookWiseRestControllerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping(value = "/api/book/actions")
public class BookWiseRestController {

    @Autowired
    private BookWiseRestControllerImpl bookWiseRestControllerImpl;

    @RequestMapping(method = RequestMethod.POST, value = "/saveUpdateNewBooks")
    public Map<String, Object> saveUpdateNewBooks(@RequestBody String bookDataJson) {
        return bookWiseRestControllerImpl.saveUpdateNewBooks(bookDataJson);
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
}
