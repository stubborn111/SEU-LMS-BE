package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class CourseaddRequest {
    String courseName;
    String teacherName;
    String semester;
    String imgUrl;
    String unit;
    String credit;
    String teachingTime;
    String teachingLocation;
    String teachingMethod;
    String introduction;
}
