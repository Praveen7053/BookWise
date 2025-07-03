package com.bookWise.bookComments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCommentDTO {
    private Long commentId;
    private String commentText;
    private String userName;
    private Timestamp createdAt;
}
