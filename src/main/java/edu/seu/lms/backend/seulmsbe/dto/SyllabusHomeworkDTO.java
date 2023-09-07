package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

@Data
public class SyllabusHomeworkDTO {
    private String studentAvatar;
    private String studentNickName;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Integer status;
    private String homeworkID;
}
