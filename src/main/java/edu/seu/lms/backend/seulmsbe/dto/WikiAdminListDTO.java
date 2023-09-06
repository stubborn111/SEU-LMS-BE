package edu.seu.lms.backend.seulmsbe.dto;

import edu.seu.lms.backend.seulmsbe.Wiki.entity.Wiki;
import lombok.Data;

import java.util.List;

@Data
public class WikiAdminListDTO {
    private Integer totalNum;
    private List<WikiDTO> list;
}
