package edu.seu.lms.backend.seulmsbe.message.service;

import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.dto.MessageListDTO;
import edu.seu.lms.backend.seulmsbe.message.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.lms.backend.seulmsbe.request.*;

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
    BaseResponse<Integer> sendToClass(SendToClassRequest sendToClassRequest,HttpServletRequest request);

    BaseResponse<String> markmessage(MarkMessageRequest markMessageRequest,HttpServletRequest request);
    BaseResponse<String> sendPM(SendPM1Request sendPMRequest,HttpServletRequest request);
    BaseResponse<Integer> sendNotice(MessageNoticeRequest noticeRequest,HttpServletRequest request);
}
