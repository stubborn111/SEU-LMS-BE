package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class HaveCheckInRequest {
    private String syllabusID;
    private String password;
}
