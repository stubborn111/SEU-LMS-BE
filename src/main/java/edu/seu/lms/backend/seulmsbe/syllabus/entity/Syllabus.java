package edu.seu.lms.backend.seulmsbe.syllabus.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Syllabus implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    @TableField("curriculumID")
    private String curriculumID;

    private String title;

    private String materials;

    private String assiments;

    @TableField("isCheckedIn")
    private boolean isCheckedIn;

    @TableField("time")
    private LocalDateTime time;

}
