package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/21 10:44
 */
@Data
public class ItemStatusRequest {
    private String itemId;
    private Integer newStatus;
}
