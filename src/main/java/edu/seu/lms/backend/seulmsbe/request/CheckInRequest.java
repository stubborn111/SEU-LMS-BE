package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class CheckInRequest {
    private String syllabusID;
    private String checkInPsw;
}
