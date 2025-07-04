package com.bookWise.bookRating.dto;

import lombok.Data;

@Data
public class SubmitRatingRequest {
    private Integer bookEncounterId;
    private int ratingValue;
}
