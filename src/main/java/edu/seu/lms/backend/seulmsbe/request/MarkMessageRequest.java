package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;


@Data
public class MarkMessageRequest {
    private String messageID;
    private Boolean SetTo;
}
