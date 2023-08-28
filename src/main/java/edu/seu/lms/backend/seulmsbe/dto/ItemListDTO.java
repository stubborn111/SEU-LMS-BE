package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/15 23:03
 */
@Data
public class ItemListDTO {
    private List<ItemDTO> list;
    private int totalNum;
}
