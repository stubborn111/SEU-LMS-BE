package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

@Data
public class WebSocketDTO {
    private String type = "checkin-update";
    private checkInData checkInData;
    private String password;
}
