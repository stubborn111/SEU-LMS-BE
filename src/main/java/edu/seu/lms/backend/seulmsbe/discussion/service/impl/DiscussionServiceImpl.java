package edu.seu.lms.backend.seulmsbe.discussion.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.discussion.entity.Discussion;
import edu.seu.lms.backend.seulmsbe.discussion.mapper.DiscussionMapper;
import edu.seu.lms.backend.seulmsbe.discussion.service.IDiscussionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.dto.Discussion.Discussion2DTO;
import edu.seu.lms.backend.seulmsbe.dto.Discussion.DiscussionDTO;
import edu.seu.lms.backend.seulmsbe.dto.Discussion.DiscussionListAllDTO;
import edu.seu.lms.backend.seulmsbe.dto.Discussion.DiscussionListDTO;
import edu.seu.lms.backend.seulmsbe.request.DiscussionListRequest;
import edu.seu.lms.backend.seulmsbe.request.DiscussionPublishRequest;
import edu.seu.lms.backend.seulmsbe.request.ReplyListRequest;
import edu.seu.lms.backend.seulmsbe.request.ReplySendRequest;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
public class DiscussionServiceImpl extends ServiceImpl<DiscussionMapper, Discussion> implements IDiscussionService {

    @Autowired
    private DiscussionMapper discussionMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public BaseResponse<?> listall(DiscussionListRequest discussionListRequest, HttpServletRequest request) {

        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String userID=currentUser.getId();
        int pagesize = discussionListRequest.getPageSize();
        String courseid = discussionListRequest.getCourseID();
        int curPage = discussionListRequest.getCurrentPage();

        LambdaUpdateWrapper<Discussion> queryMapper = new LambdaUpdateWrapper<>();
        //queryMapper.eq(Discussion::getFromUserID,currentUser.getId());
        queryMapper.eq(Discussion::getCurriculumID,courseid);
        queryMapper.isNull(Discussion::getReplyID);

        Page<Discussion> discussionPage = discussionMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        //DiscussionListDTO dto = new DiscussionListDTO(discussionPage,discussionPage.getTotal());
        DiscussionListAllDTO dto = new DiscussionListAllDTO();
        dto.setTotalNum((int)discussionPage.getTotal());
        List<Discussion> temp = discussionMapper.getlist(courseid,(curPage-1)*pagesize,pagesize);
        List<DiscussionDTO> tt = temp.stream().map(Discussion->{return todiscussionDTO(Discussion);}).collect(Collectors.toList());
        dto.setList(tt);
        return ResultUtils.success(dto);
    }
    private DiscussionDTO todiscussionDTO(Discussion tmp){
        DiscussionDTO temp = new DiscussionDTO();
        temp.setDiscussionID(tmp.getId());
        temp.setTitle(tmp.getTitle());
        if (tmp.getTime()!=null) {
            temp.setTime(tmp.getTime().toString().replace("T"," "));
        }
        temp.setContent(tmp.getContent());
        User user = userMapper.selectById(tmp.getFromUserID());
        temp.setFromUserName(user.getNickname());
        temp.setFromUserAvatar(user.getAvatarUrl());
        return temp;
    }
    @Override
    public BaseResponse<DiscussionListDTO> listreply(ReplyListRequest replyListRequest, HttpServletRequest request) {

        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        int pagesize = replyListRequest.getPageSize();
        String discussionid = replyListRequest.getDiscussionID();
        int curPage = replyListRequest.getCurrentPage();

        LambdaUpdateWrapper<Discussion> queryMapper = new LambdaUpdateWrapper<>();
        //queryMapper.eq(Discussion::getFromUserID,currentUser.getId());
        queryMapper.eq(Discussion::getReplyID,discussionid);
        //queryMapper.isNull(Discussion::getReplyID);
        queryMapper.orderByAsc(Discussion::getTime);
        Page<Discussion> discussionPage = discussionMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        //DiscussionListDTO dto = new DiscussionListDTO(discussionPage,discussionPage.getTotal());
        DiscussionListDTO dto = new DiscussionListDTO();
        dto.setTotalNum((int)discussionPage.getTotal());
        dto.setList(discussionPage.getRecords().stream().map(Discussion->{
            return todiscussion2DTO(Discussion);
        }).collect(Collectors.toList()));

        return ResultUtils.success(dto);
    }
    private Discussion2DTO todiscussion2DTO(Discussion tmp){
        Discussion2DTO temp = new Discussion2DTO();
        temp.setReplyID(tmp.getReplyID());
        temp.setTitle(tmp.getTitle());
        if (tmp.getTime()!=null) {
            temp.setTime(tmp.getTime().toString().replace("T"," "));
        }
        temp.setContent(tmp.getContent());
        User user = userMapper.selectById(tmp.getFromUserID());
        temp.setFromUserName(user.getNickname());
        temp.setFromUserAvatar(user.getAvatarUrl());
        return temp;
    }
    @Override
    public BaseResponse<Integer> replysend(ReplySendRequest replySendRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String id = UUID.randomUUID().toString().substring(0,7);
        Discussion temp = new Discussion();
        temp.setId(id);
        temp.setContent(replySendRequest.getContent());
        temp.setCurriculumID(replySendRequest.getCourseID());
        temp.setFromUserID(currentUser.getId());
        temp.setReplyID(replySendRequest.getDiscussionID());
        temp.setTime(LocalDateTime.now());
        discussionMapper.insert(temp);
        return ResultUtils.success(1);
    }

    @Override
    public BaseResponse<Discussion> publish(DiscussionPublishRequest discussionPublishRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        Discussion discussion=new Discussion();
        discussion.setId(UUID.randomUUID().toString().substring(0,7));
        discussion.setContent(discussionPublishRequest.getDiscussionContent());
        discussion.setTitle(discussionPublishRequest.getDiscussionName());
        discussion.setCurriculumID(discussionPublishRequest.getCourseID());
        discussion.setFromUserID(currentUser.getId());
        discussion.setTime(LocalDateTime.now());
        System.out.println(discussion);
        discussionMapper.insertDiscussion(discussion);
        return ResultUtils.success(null);
    }
}
