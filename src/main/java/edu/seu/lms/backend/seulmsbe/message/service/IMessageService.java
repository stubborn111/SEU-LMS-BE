package edu.seu.lms.backend.seulmsbe.message.service;

import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.dto.MessageListDTO;
import edu.seu.lms.backend.seulmsbe.message.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.lms.backend.seulmsbe.request.DiscussionListRequest;
import edu.seu.lms.backend.seulmsbe.request.MarkMessageRequest;
import edu.seu.lms.backend.seulmsbe.request.MessageListRequest;
import edu.seu.lms.backend.seulmsbe.request.SendToClassRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
public interface IMessageService extends IService<Message> {
    BaseResponse<MessageListDTO> list(MessageListRequest messageListRequest, HttpServletRequest request);
    BaseResponse<String> sendToClass(SendToClassRequest sendToClassRequest,HttpServletRequest request);

    BaseResponse<String> markmessage(MarkMessageRequest markMessageRequest,HttpServletRequest request);
}
