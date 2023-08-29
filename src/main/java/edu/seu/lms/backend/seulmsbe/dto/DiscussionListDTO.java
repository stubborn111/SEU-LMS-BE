package edu.seu.lms.backend.seulmsbe.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.discussion.entity.Discussion;
import lombok.Data;

import java.util.List;


@Data
public class DiscussionListDTO {
    private List<Discussion> list;
    private int totalNum;

}
