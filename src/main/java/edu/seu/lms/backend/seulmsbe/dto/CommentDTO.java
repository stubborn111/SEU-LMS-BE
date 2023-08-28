package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/18 23:22
 */
@Data
public class CommentDTO {
    private String commentId;
    private String fromUserName;
    private String fromUserUrl;
    private String fromUserId;
    private String toUserId;
    private int rank;
    private String content;
    private String commentTime;
}
