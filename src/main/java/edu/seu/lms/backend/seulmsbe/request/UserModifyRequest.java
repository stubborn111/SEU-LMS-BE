package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/24 9:48
 */
@Data
public class UserModifyRequest {
    private String phone;
    private String name;
    private String imgUrl;
}
