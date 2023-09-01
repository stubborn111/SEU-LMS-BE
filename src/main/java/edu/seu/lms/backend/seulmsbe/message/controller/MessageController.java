package edu.seu.lms.backend.seulmsbe.message.controller;


import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.dto.MessageListDTO;
import edu.seu.lms.backend.seulmsbe.message.service.IMessageService;
import edu.seu.lms.backend.seulmsbe.request.MarkMessageRequest;
import edu.seu.lms.backend.seulmsbe.request.MessageListRequest;
import edu.seu.lms.backend.seulmsbe.request.SendToClassRequest;
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

    @PostMapping("/list")
    public BaseResponse<MessageListDTO> listMessage(@RequestBody MessageListRequest messageListRequest, HttpServletRequest request){
        return iMessageService.list(messageListRequest,request);
    }

    @PostMapping("/send-to-class")
    public BaseResponse<String> sendToClass(@RequestBody SendToClassRequest sendToClassRequest,HttpServletRequest request){
        return iMessageService.sendToClass(sendToClassRequest,request);
    }

    @PostMapping("/mark")
    public BaseResponse<String> mark(@RequestBody MarkMessageRequest markMessageRequest,HttpServletRequest request){
        return iMessageService.markmessage(markMessageRequest,request);
    }
}
