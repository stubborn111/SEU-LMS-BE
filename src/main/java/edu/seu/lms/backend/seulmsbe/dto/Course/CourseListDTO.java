package edu.seu.lms.backend.seulmsbe.dto.Course;

import lombok.Data;

import java.util.List;

@Data
public class CourseListDTO {
    private List<CourseData2DTO> list;
    private int totalNum;

}
