package edu.seu.lms.backend.seulmsbe.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
public class SendToClassRequest {
    private String field;
    private String id;
}
