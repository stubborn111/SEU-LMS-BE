package edu.seu.lms.backend.seulmsbe.dto.Discussion;

import lombok.Data;

import java.util.List;


@Data
public class DiscussionListDTO {
    private List<Discussion2DTO> list;
    private int totalNum;

}
