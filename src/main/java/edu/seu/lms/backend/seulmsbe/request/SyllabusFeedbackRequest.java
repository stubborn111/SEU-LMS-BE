package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class SyllabusFeedbackRequest {
    String homeworkID;
    int rate;
    String feedback;
}
