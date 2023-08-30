package edu.seu.lms.backend.seulmsbe.curriculum.entity;

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
public class Curriculum implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    private String name;

    @TableField("imgUrl")
    private String imgUrl;

    @TableField("teacherID")
    private String teacherID;

    private String description;
    private String semester;

}
