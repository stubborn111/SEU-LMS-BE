package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.util.List;

@Data
public class SyllabusHomeworkListDTO {
    private Integer totalNum;
    private SyllabusHomeworkInfoDTO info;
    List<SyllabusHomeworkDTO> list;
}
