package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class SendPM1Request {
    String userID;
    String content;
    String source;
}
