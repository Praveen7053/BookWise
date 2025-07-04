package com.bookWise.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
// This unique constraint is CRITICAL. It prevents a user from rating the same book multiple times.
@Table(name = "BOOK_RATING", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"USER_ID", "BOOK_ENCOUNTER_ID"})
})
public class BookRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RATING_ID")
    private Long ratingId;

    @Column(name = "RATING_VALUE", nullable = false)
    private int ratingValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private BookWiseUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ENCOUNTER_ID", nullable = false)
    private BookEncounter bookEncounter;

    @Column(name = "RATED_AT", nullable = false)
    private Timestamp ratedAt;

    @PrePersist
    @PreUpdate
    protected void onRate() {
        ratedAt = new Timestamp(System.currentTimeMillis());
    }
}