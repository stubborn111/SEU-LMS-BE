package edu.seu.lms.backend.seulmsbe.dto.Wiki;

import edu.seu.lms.backend.seulmsbe.Wiki.entity.Wiki;
import lombok.Data;

import java.util.List;

@Data
public class WikiListDTO {
    private List<Wiki> list;
    private int totalNum;
}
