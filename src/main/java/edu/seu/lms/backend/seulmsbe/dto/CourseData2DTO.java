package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

@Data
public class CourseData2DTO {
    private String courseID;
    String courseName;
    String description;
    String imgUrl;
    String teacherName;
    String teacherAvatar;
    String semester;

}
