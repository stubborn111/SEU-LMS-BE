package edu.seu.lms.backend.seulmsbe.dto.Course;

import lombok.Data;

import java.util.List;

@Data
public class CourseAdminDTO {
    int totalNum;
    List<String> teacherList;
    List<CourseAdminDataDTO> list;
}
