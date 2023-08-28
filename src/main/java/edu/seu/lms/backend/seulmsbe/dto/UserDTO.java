package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/12 18:54
 */
@Data
public class UserDTO {
    private String id;
    private String nickName;
    private int accesstmp;
    private String phone;

    private String imgUrl;

    private String access;

}
