package com.bookWise.util.fileUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponse {
    private String fileName;
    private String fileContent;
    private String mimeType;
}
