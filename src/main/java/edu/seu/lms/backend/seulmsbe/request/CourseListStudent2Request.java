package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class CourseListStudent2Request {
    private String nickName;
    private String courseID;
    private Integer currentPage;
    private Integer pageSize;
}
