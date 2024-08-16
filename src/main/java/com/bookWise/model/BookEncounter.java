package com.bookWise.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "BOOK_ENCOUNTER")
public class BookEncounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_ENCOUNTER_ID")
    private int bookEncounterId;

    @Column(name = "BOOK_TITLE")
    private String bookTitle;

    @Column(name = "BOOK_AUTHOR")
    private String bookAuthor;

    @Column(name = "BOOK_ISBN_NUMBER")
    private String bookIsbnNumber;

    @Column(name = "BOOK_PRICE")
    private String bookPrice;

    @Column(name = "BOOK_PAGE_NUMBER")
    private String bookPageNumber;

    @Column(name = "BOOK_CATEGORY")
    private String bookCategory;

    @Column(name = "PUBLICATION_DATE")
    private String publicationDate;

    @Column(name = "BOOK_LANGUAGE")
    private String bookLanguage;

    @Column(name = "BOOK_DESCRIPTION")
    private String bookDescription;

    @Column(name = "PDF_PATH")
    private String pdfPath;

    @Column(name = "FRONT_PAGE_IMAGE_PATH")
    private String frontPageImagePath;
}
