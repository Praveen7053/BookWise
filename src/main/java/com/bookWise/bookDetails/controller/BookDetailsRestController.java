package com.bookWise.bookDetails.controller;

import com.bookWise.bookDetails.impl.BookDetailsRestImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/bookDetails/actions")
public class BookDetailsRestController {

    @Autowired
    private BookDetailsRestImpl bookDetailsRestImpl;

    @RequestMapping(method = RequestMethod.POST, value = "/getUserBookEncounter")
    public ResponseEntity<Map<String, Object>> getUserBookEncounter(@RequestBody Map<String, Integer> paginationParams) {
        int bookEncounterId = paginationParams.getOrDefault("bookEncounterId", 1);
        return bookDetailsRestImpl.getUserBookEncounter(bookEncounterId);
    }
}
