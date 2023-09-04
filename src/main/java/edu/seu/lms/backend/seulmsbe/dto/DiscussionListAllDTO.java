package edu.seu.lms.backend.seulmsbe.dto;

import edu.seu.lms.backend.seulmsbe.discussion.entity.Discussion;
import lombok.Data;

import java.util.List;

@Data
public class DiscussionListAllDTO {
    private List<DiscussionDTO> list;
    private int totalNum;

}
