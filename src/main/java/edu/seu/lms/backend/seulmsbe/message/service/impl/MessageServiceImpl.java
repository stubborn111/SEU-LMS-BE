package edu.seu.lms.backend.seulmsbe.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.entity.StudentCurriculum;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.mapper.StudentCurriculumMapper;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.curriculum.mapper.CurriculumMapper;
import edu.seu.lms.backend.seulmsbe.dto.MessageDTO;
import edu.seu.lms.backend.seulmsbe.dto.MessageListDTO;
import edu.seu.lms.backend.seulmsbe.message.entity.Message;
import edu.seu.lms.backend.seulmsbe.message.mapper.MessageMapper;
import edu.seu.lms.backend.seulmsbe.message.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import edu.seu.lms.backend.seulmsbe.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static edu.seu.lms.backend.seulmsbe.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentCurriculumMapper studentCurriculumMapper;

    @Autowired
    private CurriculumMapper curriculumMapper;

    @Override
    public BaseResponse<MessageListDTO> list(MessageListRequest messageListRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        //取出数据
        int pagesize = messageListRequest.getPageSize();
        int curPage = messageListRequest.getCurrentPage();
        LambdaUpdateWrapper<Message> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Message::getToUserID,currentUser.getId());

        Page<Message> Page = messageMapper.selectPage(new Page<>(curPage,pagesize),lambdaUpdateWrapper);
        MessageListDTO dto = new MessageListDTO();
        dto.setTotalNum((int)Page.getTotal());

        List<Message> tmp = messageMapper.getlist(currentUser.getId(),(curPage-1)*pagesize,pagesize);
        List<MessageDTO> DTO = new ArrayList<>();

        for(Message tt : tmp){
            MessageDTO temp = new MessageDTO();
            temp.setMessageID(tt.getId());
            temp.setContent(tt.getContent());
            User user = iUserService.getuser(tt.getFromUserID());
            temp.setFromUserAccess(user.getAccess()==0?"admin":"teacher");
            temp.setFromUserName(user.getNickname());
            temp.setFromUserAvatar(user.getAvatarUrl());
            //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            //temp.setTime(formatter.format(tt.getTime()));
            if (tt.getTime()!=null) {
                temp.setTime(tt.getTime().toString().replace("T"," "));
            }
            temp.setIsRead(tt.getIsRead()==0?Boolean.FALSE:Boolean.TRUE);
            DTO.add(temp);
        }
        dto.setList(DTO);
        return ResultUtils.success(dto);
    }

    @Override
    public BaseResponse<Integer> sendToClass(SendToClassRequest sendToClassRequest, HttpServletRequest request) {
        LambdaUpdateWrapper<StudentCurriculum> lambdaUpdateWrapper = new LambdaUpdateWrapper();
        lambdaUpdateWrapper.eq(StudentCurriculum::getCurriculumID,sendToClassRequest.getId());
        List<StudentCurriculum> sc = studentCurriculumMapper.selectList(lambdaUpdateWrapper);
        String teacherID = curriculumMapper.getCurriculumById(sendToClassRequest.getId()).getTeacherID();
        for(StudentCurriculum temp:sc){
            Message tmp = new Message();
            tmp.setId(UUID.randomUUID().toString().substring(0,7));
            tmp.setIsRead(0);
            tmp.setTime(LocalDateTime.now());
            tmp.setContent(sendToClassRequest.getField());
            tmp.setToUserID(temp.getStudentID());
            tmp.setFromUserID(teacherID);
            messageMapper.insert(tmp);
        }
        return ResultUtils.success(1);
    }

    @Override
    public BaseResponse<String> markmessage(MarkMessageRequest markMessageRequest, HttpServletRequest request) {
        LambdaUpdateWrapper<Message> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(Message::getId,markMessageRequest.getMessageID())
                .set(Message::getIsRead,markMessageRequest.getSetTo());
        update(queryWrapper);
        return ResultUtils.success(null);
    }

    @Override
    public BaseResponse<String> sendPM(SendPM1Request sendPMRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String fromUserID=currentUser.getId();
        String toUserID=sendPMRequest.getUserID();
        String content=sendPMRequest.getContent();
        String source=sendPMRequest.getSource();
        String id=UUID.randomUUID().toString().substring(0,7);
        Message message=new Message();
        message.setToUserID(toUserID);
        message.setContent(content);
        message.setId(id);
        message.setFromUserID(fromUserID);
        message.setSource(source);
        message.setTime(LocalDateTime.now());
        message.setIsRead(0);
        messageMapper.insert(message);
        return ResultUtils.success(null);
    }

    @Override
    public BaseResponse<Integer> sendNotice(MessageNoticeRequest noticeRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String fromUserID=currentUser.getId();
        String courseID=noticeRequest.getCourseID();
        String content=noticeRequest.getContent();
        String source=noticeRequest.getSource();
        LambdaUpdateWrapper<StudentCurriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(StudentCurriculum::getCurriculumID,courseID);
        List<StudentCurriculum> list=studentCurriculumMapper.selectList(queryMapper);
        for (StudentCurriculum tt:list)
        {
            Message tmp=new Message();
            tmp.setIsRead(0);
            tmp.setSource(source);
            tmp.setId(UUID.randomUUID().toString().substring(0,7));
            tmp.setContent(content);
            tmp.setFromUserID(fromUserID);
            tmp.setTime(LocalDateTime.now());
            tmp.setToUserID(tt.getStudentID());
            messageMapper.insert(tmp);
        }
        return ResultUtils.success(1);
    }

}
