package edu.seu.lms.backend.seulmsbe.dto;

import edu.seu.lms.backend.seulmsbe.discussion.entity.Discussion;
import lombok.Data;

import java.util.List;


@Data
public class MessageListDTO {
    private List<MessageDTO> list;
    private int totalNum;
}
