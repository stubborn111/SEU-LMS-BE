package edu.seu.lms.backend.seulmsbe.Wiki.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Wiki implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "wikiID",type = IdType.INPUT)
    private String wikiID;

    private String question;

    private String answer;

    @TableField("isSolved")
    private Boolean isSolved;

    @TableField("fromUserID")
    private String fromUserID;

    @TableField("time")
    private LocalDateTime time;

}
