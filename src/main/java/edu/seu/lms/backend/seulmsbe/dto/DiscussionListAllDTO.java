package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.util.List;

@Data
public class DiscussionListAllDTO {
    private List<DiscussionDTO> list;
    private int totalNum;

}
