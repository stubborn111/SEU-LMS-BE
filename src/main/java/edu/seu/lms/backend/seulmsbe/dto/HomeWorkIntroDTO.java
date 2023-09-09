package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

@Data
public class HomeWorkIntroDTO {
    private String homeworkName;
    private String homeworkDescription;
    private String deadline;
    private HomeWorkHistoryDTO homeworkHistory;
}
