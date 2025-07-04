package com.bookWise.bookRating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRatingDTO {
    private double averageRating;
    private int totalRatings;
    private int currentUserRating;
}
