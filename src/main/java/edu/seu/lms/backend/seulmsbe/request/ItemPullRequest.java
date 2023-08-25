package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

/**
 * @description:
 * @author: Yiqi Yu
 * @time: 2023/5/26 21:22
 */
@Data
public class ItemPullRequest {
    private String field;
    private String id;
}