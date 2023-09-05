package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class UserPasswordRequest {
    String ordiPassword;
    String password;
}
