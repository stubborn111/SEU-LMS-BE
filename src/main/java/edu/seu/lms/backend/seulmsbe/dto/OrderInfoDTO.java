package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/27 23:09
 */
@Data
public class OrderInfoDTO {
    private OrderDTO orderDTO;
    private UserDTO buyer;
    private UserDTO seller;
}
