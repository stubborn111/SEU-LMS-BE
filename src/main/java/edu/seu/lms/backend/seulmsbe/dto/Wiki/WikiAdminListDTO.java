package edu.seu.lms.backend.seulmsbe.dto.Wiki;

import lombok.Data;

import java.util.List;

@Data
public class WikiAdminListDTO {
    private Integer totalNum;
    private List<WikiDTO> list;
}
