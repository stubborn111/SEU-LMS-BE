package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;


@Data
public class ReplyListRequest {
    private String discussionID;
    private int currentPage;
    private int pageSize;
}