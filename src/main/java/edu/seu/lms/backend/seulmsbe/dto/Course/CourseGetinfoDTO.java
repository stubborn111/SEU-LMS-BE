package edu.seu.lms.backend.seulmsbe.dto.Course;

import lombok.Data;

@Data
public class CourseGetinfoDTO {
    String courseName;
    CourseDescriptionDTO description;
    String imgUrl;
    String teacherName;
    String teacherAvatar;
    String teacherPhone;
    String semester;
    String teacherEmail;
}
