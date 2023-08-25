package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 用户登录请求体
 * @author: Yiqi Yu
 * @time: 2023/4/6 21:38
 */
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -7325144208677889396L;
    private String userAccount;
    private String userPassword;

    private String type;
}
