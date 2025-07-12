package com.bookWise.dflip.controller;

import com.bookWise.dflip.impl.BookDflipImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bookWiseDflipView/book")
@CrossOrigin(origins = "*", allowedHeaders = "*") // Add CORS support
public class BookDflipController {

    @Autowired
    private BookDflipImpl bookDflip;

    @PostMapping("/preview/{bookEncounterId}")
    public ResponseEntity<Map<String, Object>> previewBookPdf(@PathVariable Integer bookEncounterId) {
        // Return PDF as Base64-encoded content, similar to FileRestController preview
        return ResponseEntity.ok(bookDflip.previewBookPdf(Long.valueOf(bookEncounterId)));
    }

}
