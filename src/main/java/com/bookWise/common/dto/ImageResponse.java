package com.bookWise.common.dto;

public class ImageResponse {
    private String imageContent;
    private String imageMimeType;

    public ImageResponse(String imageContent, String imageMimeType) {
        this.imageContent = imageContent;
        this.imageMimeType = imageMimeType;
    }

    public String getImageContent() {
        return imageContent;
    }

    public String getImageMimeType() {
        return imageMimeType;
    }
}