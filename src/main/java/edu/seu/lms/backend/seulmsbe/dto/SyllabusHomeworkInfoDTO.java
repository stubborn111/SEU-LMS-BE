package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

@Data
public class SyllabusHomeworkInfoDTO {
    private String homeworkName;
    private String homeworkDescription;
    private Integer toBeCorrectedNum;
    private Integer uncommittedNum;
}
