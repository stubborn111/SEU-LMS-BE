package edu.seu.lms.backend.seulmsbe.assignment.entity;

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

    @TableId("ID")
    private String ID;
    private String studentID;
    private String syllabusID;
    private String describe;
    private String name;
    private Float score;
    private String type;
    private String file;
    private Integer status;
    private LocalDateTime time;
}
