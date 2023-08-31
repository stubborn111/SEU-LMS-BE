package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class MessageListRequest {
    private int currentPage;
    private int pageSize;
}
