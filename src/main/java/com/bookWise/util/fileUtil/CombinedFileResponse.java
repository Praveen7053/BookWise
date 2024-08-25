package com.bookWise.util.fileUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CombinedFileResponse {
    private String imageContent;
    private String imageMimeType;
    private String pdfContent;
    private String pdfMimeType;

}
