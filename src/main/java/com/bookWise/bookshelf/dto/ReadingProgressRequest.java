package com.bookWise.bookshelf.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadingProgressRequest {
    private Integer bookEncounterId;
    private Integer currentPage;
    private Long readingTimeMinutes; // Time spent reading in this session
    private Boolean isCompleted; // Whether the book is finished
} 