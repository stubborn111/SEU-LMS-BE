package edu.seu.lms.backend.seulmsbe.assignment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Assignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID",type = IdType.INPUT)
    private String ID;
    @TableField("studentID")
    private String studentID;
    @TableField("syllabusID")
    private String syllabusID;
    @TableField("name")
    private String name;
    @TableField("score")
    private Float score;
    @TableField("type")
    private String type;
    @TableField("file")
    private String file;
    @TableField("status")
    private Integer status;
    @TableField("time")
    private LocalDateTime time;
}
