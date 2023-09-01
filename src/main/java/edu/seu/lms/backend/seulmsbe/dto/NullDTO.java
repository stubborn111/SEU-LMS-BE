package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/27 18:49
 */
@Data
public class NullDTO {
    private List<MessageDTO> list;
    private int totalNum;
}
