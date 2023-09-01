package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

/**
 * 修改请求
 */
@Data
public class UserModifyRequest {
    private String phone;
    private String name;
    private String avatar;
    private String email;
}
