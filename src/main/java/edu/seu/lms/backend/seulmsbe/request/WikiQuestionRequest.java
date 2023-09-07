package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class WikiQuestionRequest {
    private String userID;
    private String question;
}
