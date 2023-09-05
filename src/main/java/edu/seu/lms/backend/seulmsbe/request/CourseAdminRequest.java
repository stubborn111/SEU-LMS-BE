package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class CourseAdminRequest {
    private String teacherName;
    private String keyword;
    private int currentPage;
    private int pageSize;
}
