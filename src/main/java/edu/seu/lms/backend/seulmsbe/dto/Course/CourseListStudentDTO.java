package edu.seu.lms.backend.seulmsbe.dto.Course;

import lombok.Data;

import java.util.List;

@Data
public class CourseListStudentDTO {
    private Integer totalNum;
    private List<CourseStudentDTO> list;
}
