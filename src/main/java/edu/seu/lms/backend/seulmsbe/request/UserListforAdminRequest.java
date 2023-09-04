package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class UserListforAdminRequest {
    String id;
    String nickName;
    int currentPage;
    int pageSize;
}
