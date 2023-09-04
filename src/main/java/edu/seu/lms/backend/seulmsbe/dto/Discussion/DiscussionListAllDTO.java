package edu.seu.lms.backend.seulmsbe.dto.Discussion;

import lombok.Data;

import java.util.List;

@Data
public class DiscussionListAllDTO {
    private List<DiscussionDTO> list;
    private int totalNum;

}
