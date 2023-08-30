package edu.seu.lms.backend.seulmsbe.curriculum.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Student_Curriculum {
    String studentID;
    String curriculumID;
}
