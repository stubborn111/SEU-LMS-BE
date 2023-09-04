package edu.seu.lms.backend.seulmsbe.dto.Course;

import lombok.Data;

import java.util.List;

@Data
public class CourseListDescriptionDTO {
    List<CourseData3DTO> descriptionList;
}
