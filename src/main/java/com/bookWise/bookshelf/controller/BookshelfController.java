package com.bookWise.bookshelf.controller;

import com.bookWise.bookshelf.dto.BookshelfRequest;
import com.bookWise.bookshelf.dto.BookshelfResponse;
import com.bookWise.bookshelf.dto.ReadingProgressRequest;
import com.bookWise.bookshelf.impl.BookshelfImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookshelf")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BookshelfController {

    @Autowired
    private BookshelfImpl bookshelfImpl;

    @PostMapping("/add")
    public ResponseEntity<BookshelfResponse> addToShelf(@RequestBody BookshelfRequest request) {
        return bookshelfImpl.addToShelf(request);
    }

    @DeleteMapping("/remove/{bookEncounterId}")
    public ResponseEntity<BookshelfResponse> removeFromShelf(@PathVariable Integer bookEncounterId) {
        return bookshelfImpl.removeFromShelf(bookEncounterId);
    }

    @GetMapping("/check/{bookEncounterId}")
    public ResponseEntity<BookshelfResponse> checkInShelf(@PathVariable Integer bookEncounterId) {
        return bookshelfImpl.checkInShelf(bookEncounterId);
    }

    @GetMapping("/my-shelf")
    public ResponseEntity<List<Map<String, Object>>> getMyShelf() {
        return bookshelfImpl.getMyShelf();
    }

    @PostMapping("/update-progress")
    public ResponseEntity<BookshelfResponse> updateReadingProgress(@RequestBody ReadingProgressRequest request) {
        return bookshelfImpl.updateReadingProgress(request);
    }

    @GetMapping("/progress/{bookEncounterId}")
    public ResponseEntity<Map<String, Object>> getReadingProgress(@PathVariable Integer bookEncounterId) {
        return bookshelfImpl.getReadingProgress(bookEncounterId);
    }
} 