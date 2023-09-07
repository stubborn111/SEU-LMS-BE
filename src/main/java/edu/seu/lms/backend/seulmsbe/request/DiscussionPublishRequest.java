package edu.seu.lms.backend.seulmsbe.request;

import lombok.Data;

@Data
public class DiscussionPublishRequest {
    String courseID;
    String userID;
    String discussionName;
    String  discussionContent;
}
