package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/25 20:57
 */
@Data
public class ItemModifyRequest {
    private String itemId;
    private String itemName;
    private Double price;
    private String description;
    private String imgUrl;
}
