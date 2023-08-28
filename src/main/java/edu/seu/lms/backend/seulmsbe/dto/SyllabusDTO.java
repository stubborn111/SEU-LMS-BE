package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.util.List;
@Data
public class SyllabusDTO {
    private String syllabusID;
    private String title;
    private String[] meterials;
    private String[] homework;
}
