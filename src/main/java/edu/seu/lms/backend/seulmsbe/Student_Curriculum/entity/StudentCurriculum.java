package edu.seu.lms.backend.seulmsbe.Student_Curriculum.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author szh
 * @since 2023-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("student_curriculum")
public class StudentCurriculum implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("studentID")
    private String studentID;

    @TableField("curriculumID")
    private String curriculumID;


}
