package edu.seu.lms.backend.seulmsbe.dto.Course;

import lombok.Data;

@Data
public class CourseAdminDataDTO {
    String key;
    String courseID;
    String courseName;
    String description;
    String imgUrl;
    String teacherName;
    String teacherAvatar;
    String semester;
}
