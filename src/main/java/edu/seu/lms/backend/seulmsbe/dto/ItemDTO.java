package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/12 18:53
 */
@Data
public class ItemDTO {
    private String itemId;
    private String imgUrl;
    private String ownerId;
    private String description;
    private double price;
    private int status;
    private String uploadTime;
    private String ownerUrl;
    private String itemName;

}
