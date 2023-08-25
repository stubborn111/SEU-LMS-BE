package edu.seu.lms.backend.seulmsbe.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/20 18:41
 */
@Data
public class OrderCreateRequest {
    private String name;
    private String tel;
    private String remark;
    private String position;
    private String place;
    private String delivery;
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date dateTime;
    private String itemIdPara;
    private String payment;
}
