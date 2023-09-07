package edu.seu.lms.backend.seulmsbe.discussion.controller;


import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.discussion.entity.Discussion;
import edu.seu.lms.backend.seulmsbe.discussion.service.IDiscussionService;
import edu.seu.lms.backend.seulmsbe.dto.Discussion.DiscussionListDTO;
import edu.seu.lms.backend.seulmsbe.request.DiscussionListRequest;
import edu.seu.lms.backend.seulmsbe.request.DiscussionPublishRequest;
import edu.seu.lms.backend.seulmsbe.request.ReplyListRequest;
import edu.seu.lms.backend.seulmsbe.request.ReplySendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
@RestController
@RequestMapping("/discussion")
public class DiscussionController {
    @Autowired
    private IDiscussionService discussionService;
    @PostMapping("/list")
    public BaseResponse<?> listdiscussion(@RequestBody DiscussionListRequest discussionListRequest, HttpServletRequest request){
        return discussionService.listall(discussionListRequest,request);
    }

    @PostMapping("/reply-list")
    public BaseResponse<DiscussionListDTO> listreply(@RequestBody ReplyListRequest replyListRequest, HttpServletRequest request){
        return discussionService.listreply(replyListRequest,request);
    }

    @PostMapping("/reply-send")
    public BaseResponse<Integer>  replysend(@RequestBody ReplySendRequest replySendRequest, HttpServletRequest request){
        return discussionService.replysend(replySendRequest,request);
    }
    @PostMapping("/publish")
    public BaseResponse<Discussion> publish(@RequestBody DiscussionPublishRequest discussionPublishRequest, HttpServletRequest request)
    {
        return discussionService.publish(discussionPublishRequest,request);
    }

}
