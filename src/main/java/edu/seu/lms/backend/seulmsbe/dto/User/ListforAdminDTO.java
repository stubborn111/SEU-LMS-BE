package edu.seu.lms.backend.seulmsbe.dto.User;

import edu.seu.lms.backend.seulmsbe.request.UserListforAdminRequest;
import edu.seu.lms.backend.seulmsbe.request.UserModifyRequest1;
import edu.seu.lms.backend.seulmsbe.request.UserModifyRequset;
import lombok.Data;

import java.util.List;

@Data
public class ListforAdminDTO {
    int totalNum;
    List<UserDTO> list;
}
