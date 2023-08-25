package edu.seu.lms.backend.seulmsbe.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Setter
    @TableId(value = "ID",type = IdType.INPUT)
    private String id;

    private String nickname;

    private String psw;

    private Integer access;

    @TableField("avatarUrl")
    private String avatarUrl;

    private String phone;

    private String email;


}
