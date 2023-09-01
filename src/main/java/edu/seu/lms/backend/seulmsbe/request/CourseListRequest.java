package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class CourseListRequest {
    private String userID;
    private int currentPage;
    private int pageSize;
}
