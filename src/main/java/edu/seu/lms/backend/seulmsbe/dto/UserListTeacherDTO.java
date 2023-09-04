package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserListTeacherDTO {
    List<TeacherDTO> teacherList;
}
