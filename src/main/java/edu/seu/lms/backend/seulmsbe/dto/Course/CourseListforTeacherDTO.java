package edu.seu.lms.backend.seulmsbe.dto.Course;

import lombok.Data;

import java.util.List;

@Data
public class CourseListforTeacherDTO {
    private List<CourseData3DTO> tabList;
}
