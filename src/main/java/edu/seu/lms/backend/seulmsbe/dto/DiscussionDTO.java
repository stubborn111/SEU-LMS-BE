package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;


@Data
public class DiscussionDTO {
    private String discussionID;
    private String fromUserName;
    private String fromUserAvatar;
    private String title;
    private String content;
    private String  time;

}
