package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/18 23:27
 */
@Data
public class CommentListDTO {
    private List<CommentDTO> list;
    private int totalNum;
}
