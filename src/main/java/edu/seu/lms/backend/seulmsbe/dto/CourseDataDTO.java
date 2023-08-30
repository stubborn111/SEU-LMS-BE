package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data

public class CourseDataDTO {
    public CourseDataDTO(String courseName, String description, String imgUrl, String teacherName, String teacherAvatar, String teacherPhone, String semester, String teacherEmail) {
        this.courseName = courseName;
        this.description = description;
        this.imgUrl = imgUrl;
        this.teacherName = teacherName;
        this.teacherAvatar = teacherAvatar;
        this.teacherPhone = teacherPhone;
        this.semester = semester;
        this.teacherEmail = teacherEmail;
    }

    public CourseDataDTO() {
    }

    String courseName;
    String description;
    String imgUrl;
    String teacherName;
    String teacherAvatar;
    String teacherPhone;
    String semester;
    String teacherEmail;
}
