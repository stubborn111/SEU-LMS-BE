package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class MaterialUploadRequest {
    private String syllabusID;
    private String name;
    private String content;
    private String fileUrl;
}
