package edu.seu.lms.backend.seulmsbe.dto.Course;

import lombok.Data;

@Data
public class CourseDescriptionDTO {
    String unit;
    String credit;
    String teachingTime;
    String teachingLocation;
    String teachingMethod;
    String introduction;
}
