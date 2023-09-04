package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class UserRequest {
    String nickName;
    String  ID;
    int access;
    String  email;
    String  phone;
    String  avatarUrl;
}
