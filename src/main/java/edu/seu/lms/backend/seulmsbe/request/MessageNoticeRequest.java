package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class MessageNoticeRequest {
    String content;
    String courseID;
    String source;
}
