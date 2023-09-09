package edu.seu.lms.backend.seulmsbe.file.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @since 2023-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class File implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId("ID")
    private String id;

    private String type;

    private String name;

    private LocalDateTime time;

    private String description;

    private String url;

    private Integer status;

    @TableField("syllabusID")
    private String syllabusID;


}
