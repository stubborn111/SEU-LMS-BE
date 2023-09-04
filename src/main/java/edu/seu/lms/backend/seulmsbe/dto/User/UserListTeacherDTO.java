package edu.seu.lms.backend.seulmsbe.dto.User;

import lombok.Data;

import java.util.List;

@Data
public class UserListTeacherDTO {
    List<TeacherDTO> teacherList;
}
