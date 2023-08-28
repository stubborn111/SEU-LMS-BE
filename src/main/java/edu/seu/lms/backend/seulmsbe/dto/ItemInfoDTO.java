package edu.seu.lms.backend.seulmsbe.dto;

import lombok.Data;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/19 9:50
 */
@Data
public class ItemInfoDTO {
    private DiscussionDTO itemInfo;
    private UserDTO ownerInfo;
}
