package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class CourseListRequest {
    private String userid;
    private int currentPage;
    private int pageSize;
}
