package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;


@Data
public class ReplySendRequest {
    private String content;
    private String discussionID;
    private String courseID;
}
