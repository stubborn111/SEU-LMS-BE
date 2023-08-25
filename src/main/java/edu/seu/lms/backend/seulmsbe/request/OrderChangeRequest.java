package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/27 16:55
 */
@Data
public class OrderChangeRequest {
    private String content;
    private String imgUrl;
    private String orderId;
}
