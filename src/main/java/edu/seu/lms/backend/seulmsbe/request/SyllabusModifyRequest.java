package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class SyllabusModifyRequest {
    String syllabusID;
    String syllabusTitle;
    String selectedDateTime;
}
