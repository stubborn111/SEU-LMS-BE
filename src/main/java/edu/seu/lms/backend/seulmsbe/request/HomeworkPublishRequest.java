package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class HomeworkPublishRequest {
    private String syllabusID;
    private String homeworkName;
    private String homeworkDescription;
    private String deadline;
}
