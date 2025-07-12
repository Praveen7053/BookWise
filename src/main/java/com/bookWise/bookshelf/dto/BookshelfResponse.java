package com.bookWise.bookshelf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookshelfResponse {
    private boolean success;
    private String message;
    
    @JsonProperty("isInShelf")
    private boolean isInShelf;
    
    private Long bookshelfId; // optional, for when we need the ID
} 