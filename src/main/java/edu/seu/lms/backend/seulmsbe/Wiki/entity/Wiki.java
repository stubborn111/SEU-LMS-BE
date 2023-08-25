package edu.seu.lms.backend.seulmsbe.Wiki.entity;

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
public class Wiki implements Serializable {

    private static final long serialVersionUID = 1L;

    private String question;

    private String answer;


}
