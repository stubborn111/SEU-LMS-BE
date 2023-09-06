package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MaterialListDTO {
    private List<MaterialDTO> fileList;
}
