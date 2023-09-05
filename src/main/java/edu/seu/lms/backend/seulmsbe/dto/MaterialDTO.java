package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaterialDTO {
    private String type;
    private String name;
    private String description;
    private Integer status;
    private String url;
    private LocalDateTime time;
}
