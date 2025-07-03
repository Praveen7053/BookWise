package com.bookWise.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "BOOK_COMMENT")
public class BookComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long commentId;

    // Many comments can belong to one book.
    // This creates a foreign key column `BOOK_ENCOUNTER_ID`.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ENCOUNTER_ID", nullable = false)
    private BookEncounter bookEncounter;

    // Many comments can be written by one user.
    // This creates a foreign key column `USER_ID`.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private BookWiseUser user;

    @Column(name = "COMMENT_TEXT", nullable = false, length = 2000)
    private String commentText;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Timestamp createdAt;

    // This method is called automatically by JPA before the entity is persisted.
    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }
}