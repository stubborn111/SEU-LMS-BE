package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class UserModifyRequset {
    String nickName;
    String  ID;
    String access;
    String  email;
    String  phone;
    String  avatarUrl;
}
