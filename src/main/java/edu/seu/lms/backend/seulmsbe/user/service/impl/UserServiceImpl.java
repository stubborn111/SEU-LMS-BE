package edu.seu.lms.backend.seulmsbe.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.mapper.StudentCurriculumMapper;
import edu.seu.lms.backend.seulmsbe.Wiki.mapper.WikiMapper;
import edu.seu.lms.backend.seulmsbe.assignment.mapper.AssignmentMapper;
import edu.seu.lms.backend.seulmsbe.checkin.mapper.CheckinMapper;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ErrorCode;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import edu.seu.lms.backend.seulmsbe.curriculum.mapper.CurriculumMapper;
import edu.seu.lms.backend.seulmsbe.curriculum.service.impl.CurriculumServiceImpl;
import edu.seu.lms.backend.seulmsbe.discussion.mapper.DiscussionMapper;
import edu.seu.lms.backend.seulmsbe.dto.User.ListforAdminDTO;
import edu.seu.lms.backend.seulmsbe.dto.User.TeacherDTO;
import edu.seu.lms.backend.seulmsbe.dto.User.UserDTO;
import edu.seu.lms.backend.seulmsbe.dto.User.UserListTeacherDTO;
import edu.seu.lms.backend.seulmsbe.event.mapper.EventMapper;
import edu.seu.lms.backend.seulmsbe.exception.BusinessException;
import edu.seu.lms.backend.seulmsbe.message.entity.Message;
import edu.seu.lms.backend.seulmsbe.message.mapper.MessageMapper;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import edu.seu.lms.backend.seulmsbe.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private CheckinMapper checkinMapper;
    @Autowired
    private CurriculumMapper curriculumMapper;
    @Autowired
    private DiscussionMapper discussionMapper;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private StudentCurriculumMapper studentCurriculumMapper;
    @Autowired
    private WikiMapper wikiMapper;
    @Autowired
    private CurriculumServiceImpl curriculumService;
    @Override
    public int createuser(User user) {
        int count =userMapper.insert(user);
        return count;
    }

    @Override
    public User userLogin(String userId, String userPassword, HttpServletRequest request) {
        //校验
        if(StringUtils.isAnyBlank(userId, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户名或密码为空");
        }

        //加密
        //userPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        LambdaQueryWrapper<User> queryMapper = new LambdaQueryWrapper<>();
        queryMapper.eq(User::getId,userId);
        queryMapper.eq(User::getPsw, userPassword);
        User user = userMapper.selectOne(queryMapper);
        if(user == null) {
            System.out.println("originUser login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码错误");
        }

        //用户脱敏
        User safetyUser = getSafetyUser(user);

        //记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if(originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setPsw(originUser.getPsw());
        safetyUser.setAccess(originUser.getAccess());
        safetyUser.setNickname(originUser.getNickname());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPhone(originUser.getPhone());
        return safetyUser;
    }

    /**
     * 注销
     * @param request
     * @return
     */
    @Override
    public void userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }

    @Override
    public BaseResponse<String> userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        String userPassword = userLoginRequest.getUserPassword();
        String userAccount = userLoginRequest.getUserAccount();
        //登录逻辑
        userLogin(userAccount, userPassword, request);
        return ResultUtils.success(userLoginRequest.getType());
    }

    @Override
    public BaseResponse<Integer> modify(UserModifyRequest1 userModifyRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, currentUser.getId())
                .set(User::getNickname, userModifyRequest.getName())
                .set(User::getAvatarUrl, userModifyRequest.getAvatar())
                .set(User::getPhone, userModifyRequest.getPhone())
                .set(User::getEmail,userModifyRequest.getEmail());
        update(updateWrapper);
        currentUser.setNickname(userModifyRequest.getName());
        currentUser.setAvatarUrl(userModifyRequest.getAvatar());
        currentUser.setPhone(userModifyRequest.getPhone());
        currentUser.setEmail(userModifyRequest.getEmail());
        //更新cookie
        request.getSession().setAttribute(USER_LOGIN_STATE, currentUser);
        return ResultUtils.success(1);
    }

    @Override
    public User getuser(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public BaseResponse<UserListTeacherDTO> listTeacher(TeacherListRequest teacherListRequest, HttpServletRequest request) {
        LambdaUpdateWrapper<User> updateWrapper=new LambdaUpdateWrapper<>();
        if(teacherListRequest.getTeacherID()!=null&&(!teacherListRequest.getTeacherID().isEmpty())){
            updateWrapper.eq(User::getId,teacherListRequest.getTeacherID());
        }
        else updateWrapper.eq(User::getAccess,2);
        List<User> users=userMapper.selectList(updateWrapper);
        UserListTeacherDTO DTO=new UserListTeacherDTO();
        List<TeacherDTO> dto=new ArrayList<>();
        for(User tt:users)
        {
            TeacherDTO teacherDTO=new TeacherDTO();
            teacherDTO.setTeacherID(tt.getId());
            teacherDTO.setTeacherName(tt.getNickname());
            dto.add(teacherDTO);
        }
        DTO.setTeacherList(dto);
        return ResultUtils.success(DTO);
    }

    @Override
    public BaseResponse<Integer> modifyPassword(UserPasswordRequest userPasswordRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        System.out.println(userPasswordRequest);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId,currentUser.getId())
                .set(User::getPsw,userPasswordRequest.getPassword());
        update(updateWrapper);
        currentUser.setPsw(userPasswordRequest.getPassword());
        request.getSession().setAttribute(USER_LOGIN_STATE, currentUser);
        if(currentUser.getPsw().equals(userPasswordRequest.getPassword()))
        {
            return ResultUtils.success(1);
        }else
        {
            return ResultUtils.success(-1);
        }
    }

    @Override
    public BaseResponse<ListforAdminDTO> listforadmin(UserListforAdminRequest userListforAdminRequest, HttpServletRequest request) {
        int pagesize = userListforAdminRequest.getPageSize();
        int curPage = userListforAdminRequest.getCurrentPage();
        String id=userListforAdminRequest.getId();
        String nickName= userListforAdminRequest.getNickName();
        LambdaUpdateWrapper<User> queryMapper = new LambdaUpdateWrapper<>();
        if(id==null&&nickName!=null)
        {
            queryMapper.like(User::getNickname,nickName);
        }else if(id!=null&&nickName==null)
        {
            queryMapper.like(User::getId,id);
        } else if (id!=null&&nickName!=null) {
            queryMapper.like(User::getId,id)
                    .like(User::getNickname,nickName);
        }

        Page<User> Page=userMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        ListforAdminDTO DTO=new ListforAdminDTO();
        DTO.setTotalNum((int)Page.getTotal());
        List<User> users=Page.getRecords();
        List<UserDTO> dto=new ArrayList<>();
        for(User user:users)
        {
            UserDTO userDTO = new UserDTO();
            if(!user.getId().equals("000000000"))
            {
                userDTO.setPhone(user.getPhone());
                userDTO.setId(user.getId());
                userDTO.setNickName(user.getNickname());
                userDTO.setAvatarUrl(user.getAvatarUrl());
                userDTO.setEmail(user.getEmail());
                if(user.getAccess() == 0){
                    userDTO.setAccess("admin");
                }
                else if(user.getAccess() == 1){
                    userDTO.setAccess("student");
                }
                else userDTO.setAccess("teacher");
                dto.add(userDTO);
            }
        }
        DTO.setList(dto);
        return ResultUtils.success(DTO);
    }

    @Override
    public BaseResponse<String> sendPM(SendPMRequest sendPMRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String useID = sendPMRequest.getId();
        String msg = sendPMRequest.getAnswer();
        Message message = new Message();
        message.setId(UUID.randomUUID().toString().substring(0,7));
        message.setFromUserID(currentUser.getId());
        message.setToUserID(useID);
        message.setContent(msg);
        message.setIsRead(0);
        message.setTime(LocalDateTime.now());
        messageMapper.insert(message);
        return ResultUtils.success(null);
    }

    @Override
    public void deleteStudent(String userID) {
        assignmentMapper.deleteByUserID(userID);
        checkinMapper.deleteByUserID(userID);
        eventMapper.deleteByUserID(userID);
        studentCurriculumMapper.deleteByUserID(userID);
        userMapper.deleteById(userID);

    }

    @Override
    public void deleteTeacher(String userID) {
        List<Curriculum> list=curriculumMapper.selectCurriculumByteacher(userID);
        for (Curriculum tt:list)
        {
            curriculumService.deleteCourseByID(tt.getId());
        }
        userMapper.deleteById(userID);
    }

    @Override
    public void deleteAdmin(String userID) {
        userMapper.deleteById(userID);
    }

    @Override
    public void deleteUser(String userID) {
        User user=userMapper.selectById(userID);
        if(user.getAccess()==0) deleteAdmin(userID);
        if (user.getAccess()==1) deleteStudent(userID);
        if (user.getAccess()==2) deleteTeacher(userID);
        wikiMapper.updateWikiByUserID(userID);
        discussionMapper.updateFromUserID(userID);
        messageMapper.updateFromUserID(userID);
        messageMapper.updateToUserID(userID);
        messageMapper.deleteMessage();
    }

}
