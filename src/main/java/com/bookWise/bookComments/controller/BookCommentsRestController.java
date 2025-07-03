package com.bookWise.bookComments.controller;

import com.bookWise.bookComments.dto.BookCommentDTO;
import com.bookWise.bookComments.dto.NewCommentRequest;
import com.bookWise.bookComments.impl.BookCommentsRestImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/bookComments/actions")
public class BookCommentsRestController {

    @Autowired
    private BookCommentsRestImpl bookCommentsRest;

    @GetMapping("/forBook/{bookEncounterId}")
    public ResponseEntity<List<BookCommentDTO>> getCommentsForBook(@PathVariable Integer bookEncounterId) {
        List<BookCommentDTO> comments = bookCommentsRest.getCommentsForBook(bookEncounterId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/addComment")
    public ResponseEntity<?> addComment(@RequestBody NewCommentRequest commentRequest) {
        // Basic validation
        if (commentRequest.getCommentText() == null || commentRequest.getCommentText().trim().isEmpty()) {
            return new ResponseEntity<>("Comment text cannot be empty.", HttpStatus.BAD_REQUEST);
        }

        try {
            BookCommentDTO newComment = bookCommentsRest.addComment(commentRequest);
            return new ResponseEntity<>(newComment, HttpStatus.CREATED);
        } catch (Exception e) {
            // A more robust solution would use a @ControllerAdvice for exception handling
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
