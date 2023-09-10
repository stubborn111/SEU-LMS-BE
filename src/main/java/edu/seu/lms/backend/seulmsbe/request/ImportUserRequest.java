package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class ImportUserRequest {
    private String[] id;
    private String courseID;
 }
