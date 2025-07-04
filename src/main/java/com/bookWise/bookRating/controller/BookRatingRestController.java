package com.bookWise.bookRating.controller;

import com.bookWise.bookRating.dto.BookRatingDTO;
import com.bookWise.bookRating.dto.SubmitRatingRequest;
import com.bookWise.bookRating.impl.BookRatingRestImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class BookRatingRestController {

    @Autowired
    private BookRatingRestImpl ratingRest;

    @GetMapping("/book/{bookEncounterId}")
    public ResponseEntity<BookRatingDTO> getRatingForBook(@PathVariable Integer bookEncounterId) {
        return ResponseEntity.ok(ratingRest.getRatingForBook(bookEncounterId));
    }

    @PostMapping("/submit")
    public ResponseEntity<BookRatingDTO> submitRating(@RequestBody SubmitRatingRequest request) {
        if (request.getRatingValue() < 1 || request.getRatingValue() > 5) {
            return ResponseEntity.badRequest().build();
        }
        BookRatingDTO updatedRating = ratingRest.submitRating(request);
        return ResponseEntity.ok(updatedRating);
    }
}


