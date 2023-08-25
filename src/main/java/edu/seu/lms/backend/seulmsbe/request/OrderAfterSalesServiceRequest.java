package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/7 20:04
 */
@Data
public class OrderAfterSalesServiceRequest implements Serializable {

    private static final long serialVersionUID = 1384281191076132052L;
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 售后请求内容
     */
    private String content;
}
