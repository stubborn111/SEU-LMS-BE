package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.util.Map;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/12 18:53
 */
@Data
public class OrderDTO {
    private String id;
    private ItemDTO item;
    private String buyerId;
    private String sellerId;
    private int state;
    private String createTime;
    private String updateTime;
    private String payment;
    private String deliveryTime;
    private String delivery;
    private String name;
    private String tel;
    private String position;
    private String remark;
    private Map<String, String> message;
    private String imgUrl;
}
