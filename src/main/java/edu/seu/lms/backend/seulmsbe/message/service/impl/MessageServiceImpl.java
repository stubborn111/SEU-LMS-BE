package edu.seu.lms.backend.seulmsbe.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import edu.seu.lms.backend.seulmsbe.dto.CourseData2DTO;
import edu.seu.lms.backend.seulmsbe.dto.CourseSearchDTO;
import edu.seu.lms.backend.seulmsbe.dto.MessageDTO;
import edu.seu.lms.backend.seulmsbe.dto.MessageListDTO;
import edu.seu.lms.backend.seulmsbe.message.entity.Message;
import edu.seu.lms.backend.seulmsbe.message.mapper.MessageMapper;
import edu.seu.lms.backend.seulmsbe.message.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.request.MessageListRequest;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public BaseResponse<MessageListDTO> list(MessageListRequest messageListRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        //取出数据
        int pagesize = messageListRequest.getPageSize();
        int curPage = messageListRequest.getCurrentPage();

        Page<Message> Page = messageMapper.selectPage(new Page<>(curPage,pagesize),null);
        MessageListDTO dto = new MessageListDTO();
        dto.setTotalNum((int)Page.getTotal());

        List<Message> tmp = Page.getRecords();
        List<MessageDTO> DTO = new ArrayList<>();

        for(Message tt : tmp){
            MessageDTO temp = new MessageDTO();
            temp.setMessageID(tt.getId());
            temp.setContent(tt.getContent());
            User user = iUserService.getuser(tt.getFromUserID());
            temp.setFromUserAccess(user.getAccess()==0?"admin":"teacher");
            temp.setFromUserName(user.getNickname());
            temp.setFromUserAvatar(user.getAvatarUrl());
            temp.setTime(tt.getTime().toString());
            DTO.add(temp);
        }
        dto.setList(DTO);
        return ResultUtils.success(dto);
    }
}
