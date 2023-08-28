package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;


@Data
public class DiscussionListRequest {
    private String courseID;
    private int currentPage;
    private int pageSize;
}
