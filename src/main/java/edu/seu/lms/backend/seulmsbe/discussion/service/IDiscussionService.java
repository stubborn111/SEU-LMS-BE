package edu.seu.lms.backend.seulmsbe.discussion.service;

import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.discussion.entity.Discussion;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.lms.backend.seulmsbe.dto.Discussion.DiscussionListDTO;
import edu.seu.lms.backend.seulmsbe.request.DiscussionListRequest;
import edu.seu.lms.backend.seulmsbe.request.DiscussionPublishRequest;
import edu.seu.lms.backend.seulmsbe.request.ReplyListRequest;
import edu.seu.lms.backend.seulmsbe.request.ReplySendRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
public interface IDiscussionService extends IService<Discussion> {
    BaseResponse<?> listall(DiscussionListRequest discussionListRequest, HttpServletRequest request);
    BaseResponse<DiscussionListDTO> listreply(ReplyListRequest replyListRequest,HttpServletRequest request);
    BaseResponse<Integer> replysend(ReplySendRequest replySendRequest, HttpServletRequest request);

    BaseResponse<Discussion> publish(DiscussionPublishRequest discussionPublishRequest,HttpServletRequest request);
}
