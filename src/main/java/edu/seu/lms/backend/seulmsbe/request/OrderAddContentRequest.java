package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/7 20:02
 */
@Data
public class OrderAddContentRequest implements Serializable {

    private static final long serialVersionUID = 2536753120151725715L;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 评价内容
     */
    private String content;
    /**
     * 评分
     */
    private Integer rank;
}
