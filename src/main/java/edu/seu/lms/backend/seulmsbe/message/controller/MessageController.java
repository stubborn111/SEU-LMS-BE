package edu.seu.lms.backend.seulmsbe.message.controller;


import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.dto.MessageListDTO;
import edu.seu.lms.backend.seulmsbe.message.service.IMessageService;
import edu.seu.lms.backend.seulmsbe.request.*;
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
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private IMessageService iMessageService;

    /**
     * sendPM:给某人发消息
     * sendToClass：给班级内所有学生发通知
     * sendNotice：发送通知
     * mark：标志已读
     * listMessage：列出某人的全部消息
     */
    @PostMapping("/list")
    public BaseResponse<MessageListDTO> listMessage(@RequestBody MessageListRequest messageListRequest, HttpServletRequest request){
        return iMessageService.list(messageListRequest,request);
    }

    @PostMapping("/send-to-class")
    public BaseResponse<Integer> sendToClass(@RequestBody SendToClassRequest sendToClassRequest,HttpServletRequest request){
        return iMessageService.sendToClass(sendToClassRequest,request);
    }

    @PostMapping("/mark")
    public BaseResponse<String> mark(@RequestBody MarkMessageRequest markMessageRequest,HttpServletRequest request){
        return iMessageService.markmessage(markMessageRequest,request);
    }

    @PostMapping("sendPM")
    public BaseResponse<String> sendPM(@RequestBody SendPM1Request sendPMRequest, HttpServletRequest request) {
        return iMessageService.sendPM(sendPMRequest,request);
    }
    @PostMapping("sendNotice")
    public BaseResponse<Integer> sendNotice(@RequestBody MessageNoticeRequest noticeRequest, HttpServletRequest request) {
        return iMessageService.sendNotice(noticeRequest,request);
    }

}
