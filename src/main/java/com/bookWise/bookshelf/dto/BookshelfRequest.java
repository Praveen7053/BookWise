package com.bookWise.bookshelf.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookshelfRequest {
    private Integer bookEncounterId;
    private String notes; // optional
} 