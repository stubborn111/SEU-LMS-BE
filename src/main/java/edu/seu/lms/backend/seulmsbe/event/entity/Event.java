package edu.seu.lms.backend.seulmsbe.event.entity;

import java.time.LocalDate;
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
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    private LocalDate date;

    private String content;

    @TableField("userID")
    private String userID;


}
