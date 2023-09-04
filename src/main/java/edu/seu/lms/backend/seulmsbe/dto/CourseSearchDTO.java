package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseSearchDTO {
    private List<CourseData2DTO> list;
    private int totalNum;

}
