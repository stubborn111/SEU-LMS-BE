package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class WikiMarkRequest {
    private String wikiID;
    private Boolean isSolved;
}
