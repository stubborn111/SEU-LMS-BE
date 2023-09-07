package edu.seu.lms.backend.seulmsbe.dto.Wiki;

import lombok.Data;

@Data
public class WikiDTO {
    private String fromUserName;
    private String fromUserAvatar;
    private String fromUserAccess;
    private String wikiID;
    private String question;
    private String time;
    private String answer;
}
