package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/16 16:22
 */
@Data
public class ItemCreateRequest {
    private String itemName;
    private Double price;
    private String description;
    private String imgUrl;
}
