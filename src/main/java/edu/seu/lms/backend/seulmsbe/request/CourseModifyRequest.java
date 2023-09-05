package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class CourseModifyRequest {
    String courseID;
    String courseName;
    String teacherName;
    String semester;
    String imgUrl;
}
