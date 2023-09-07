package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EventDataDTO {
    Date date;
    String content;
    String type;
}
