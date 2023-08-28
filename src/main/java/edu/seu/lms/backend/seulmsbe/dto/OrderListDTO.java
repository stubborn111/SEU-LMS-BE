package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/27 18:49
 */
@Data
public class OrderListDTO {
    private List<OrderDTO> list;
    private int totalNum;
}
