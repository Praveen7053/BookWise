package com.bookWise.bookComments.dto;

import lombok.Data;

@Data
public class NewCommentRequest {
    private Integer bookEncounterId;
    private String commentText;
}
