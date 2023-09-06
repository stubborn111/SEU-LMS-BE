package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class SyllabusAddRequest {
    String courseID;
    String syllabusTitle;
    String selectedDateTime;
}
