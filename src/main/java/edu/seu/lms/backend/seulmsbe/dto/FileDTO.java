package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class FileDTO {
    String type;
    String name;
    String description;
    int  status;
    String url;
    DateTime time;
}
