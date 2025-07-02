package com.bookWise.bookDetails.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BookEncounterDTO {

    private Integer bookEncounterId;
    private String bookTitle;
    private String bookAuthor;
    private String bookIsbnNumber;
    private String bookPrice;
    private String bookPageNumber;
    private String bookCategory;
    private Timestamp publicationDate;
    private String bookLanguage;
    private String bookDescription;
    private String pdfPath;
    private String frontPageImagePath;
    private String uploadedByName;
    private String updatedById;
    private Timestamp uploadedTime;

    private String coverImageContent;
    private String coverImageMimeType;
}
