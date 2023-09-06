package edu.seu.lms.backend.seulmsbe.checkin.entity;

import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Checkin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("syllabusID")
    private String syllabusID;

    @TableField("studentID")
    private String studentID;

    private LocalDate time;

    @TableField("isCheckedIn")
    private Integer isCheckedIn;

    @TableId(value = "ID",type = IdType.INPUT)
    private String ID;


}
