package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;


@Data
public class MessageDTO {
    private String fromUserName;
    private String fromUserAvatar;
    private String fromUserAccess;
    private String messageID;
    private String content;
    private String time;
    private Boolean isRead;
}
