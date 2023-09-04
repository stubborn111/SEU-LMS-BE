package edu.seu.lms.backend.seulmsbe.dto.User;

import lombok.Data;

/**
 * 返回user类
 */
@Data
public class UserDTO {
    private String id;
    private String nickName;
    private String phone;

    private String imgUrl;

    private String access;
    private String email;
}
