package edu.seu.lms.backend.seulmsbe.dto;

import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import lombok.Data;

import java.util.List;

@Data
public class CurriculumDTO {
    public CurriculumDTO(int num, List<Curriculum> curriculumList) {
        this.num = num;
        this.curriculumList = curriculumList;
    }

    public CurriculumDTO() {
    }

    int num;
    List<Curriculum> curriculumList;
}
