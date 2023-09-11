package edu.seu.lms.backend.seulmsbe.syllabus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

    /*
    id:大纲的ID
    curriculumID:大纲所属的课程ID
    title:大纲标题
    materials:大纲里的课件
    assignments:大纲里教师布置的作业
    isCheckedIn:是否发起签到
    time:建立大纲的时间
    checkInPsw:签到密码
    assignmentContent:作业内容
    assignmentTime: 作业截至时间

 */



    private static final long serialVersionUID = 1L;

    @TableId(value = "ID",type = IdType.INPUT)
    private String id;

    @TableField("curriculumID")
    private String curriculumID;

    private String title;

    private String materials;

    private String assiments;

    @TableField("isCheckedIn")
    private Integer isCheckedIn;

    @TableField("time")
    private LocalDateTime time;

    @TableField("checkInPsw")
    private String checkInPsw;
    @TableField("assignmentContent")
    private String assignmentContent;
    @TableField("assignmentTime")
    private LocalDateTime assignmentTime;

}
