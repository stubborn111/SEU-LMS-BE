package edu.seu.lms.backend.seulmsbe.assiment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
public class Assiment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("studentID")
    private String studentID;

    private String describe;

    private Float score;

    private String file;

    private Integer status;


}
