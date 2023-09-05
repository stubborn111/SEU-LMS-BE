package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class PostAnswerRequest {
    private String wikiID;
    private String answer;
}
