package edu.seu.lms.backend.seulmsbe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description: 用于多表查询
 * @author: Yiqi Yu
 * @time: 2023/5/14 23:30
 */
@Data
public class ItemUserDTO {
    /**
     * 商品id
     */
    private String id;

    /**
     * 商品图片url
     */
    private String imgUrl;

    /**
     * 发售者
     */
    private String ownerId;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 价格
     */
    private Double price;

    /**
     * 商品状态 0表示未出售 1表示正在出售
     */
    private Integer status;

    /**
     * 商品创建时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    /**
     * 名称
     */
    private String itemName;
    private Integer userId;
    private Integer userCredit;
    private String userImgUrl;
}

