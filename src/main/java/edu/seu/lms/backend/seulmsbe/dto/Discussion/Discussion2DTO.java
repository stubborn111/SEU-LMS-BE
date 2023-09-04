package edu.seu.lms.backend.seulmsbe.dto.Discussion;

import lombok.Data;


@Data
public class Discussion2DTO {
    private String replyID;
    private String fromUserName;
    private String fromUserAvatar;
    private String title;
    private String content;
    private String  time;

}
