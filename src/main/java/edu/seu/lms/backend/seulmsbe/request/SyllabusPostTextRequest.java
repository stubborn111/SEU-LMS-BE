package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class SyllabusPostTextRequest {
    String syllabusID;
    String homeworkTitle;
    String body;
}
