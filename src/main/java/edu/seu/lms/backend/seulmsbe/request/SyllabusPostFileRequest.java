package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class SyllabusPostFileRequest {
    String syllabusID;
    String homeworkTitle;
    String homeworkUrl;
}
