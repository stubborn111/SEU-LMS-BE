package edu.seu.lms.backend.seulmsbe.user.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.mapper.StudentCurriculumMapper;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ErrorCode;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.dto.User.ListforAdminDTO;
import edu.seu.lms.backend.seulmsbe.dto.User.UserDTO;
import edu.seu.lms.backend.seulmsbe.dto.User.UserListTeacherDTO;
import edu.seu.lms.backend.seulmsbe.exception.BusinessException;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import edu.seu.lms.backend.seulmsbe.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.update;
import static edu.seu.lms.backend.seulmsbe.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StudentCurriculumMapper studentCurriculumMapper;
    /**
     * 测试
     * 实际不需要创建
     */
    @GetMapping("/create")
    public String createuser(String id, String name, String psw, int access){
        Map<String,Object> map = new HashMap<String,Object>();
        User user = new User();
        user.setId(id);
        user.setNickname(name);
        user.setPsw(psw);
        user.setAccess(access);
        try{
            int result=userService.createuser(user);
            if(result==1) {
                map.put("status", "200");
                map.put("msg", "添加成功");
            }else{
                map.put("status", "201");
                map.put("msg", "添加失败");
            }
        }catch(Exception ex){
            map.put("status","-1");
            map.put("errorMsg",ex.getMessage());
        }
        return JSON.toJSONString(map);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public BaseResponse<String> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        return userService.userLogin(userLoginRequest, request);
    }
    @PostMapping("/importToCourse")
    public BaseResponse<String> importToCourse(@RequestBody ImportUserRequest userRequest,HttpServletRequest request)
    {
        String[] userID=userRequest.getId();
        String courseID=userRequest.getCourseID();
        for (String id:userID)
        {
            if(userMapper.selectById(id).getAccess()==1) studentCurriculumMapper.insertStudentCourse(id,courseID, UUID.randomUUID().toString().substring(0,7));
        }
        return ResultUtils.success(null);
    }
    /**
     * 登出
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //int result = userService.userLogout(request);
        return ResultUtils.success(null);
    }

    /**
     * 修改用户信息
     * @param userModifyRequest1
     * @param request
     * @return
     */
    @PostMapping("/modify")
    public BaseResponse<Integer> userModify(@RequestBody UserModifyRequest1 userModifyRequest1, HttpServletRequest request) {
        return userService.modify(userModifyRequest1, request);
    }

    /**
     * 获取当前用户信息
     * @param request
     * @return
     */
    @GetMapping("/currentUser")
    public BaseResponse<UserDTO> getCurrentUser(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        //用户为空
        if(currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        String id = currentUser.getId();
        // todo 校验用户是否合法
        User user = userService.getById(id);
        return ResultUtils.success(toUserDTO(user));
    }

    @PostMapping("list-teacher")
    public BaseResponse<UserListTeacherDTO> listTeacher(@RequestBody TeacherListRequest teacherListRequest, HttpServletRequest request)
    {
        return userService.listTeacher(teacherListRequest,request);
    }

    @PostMapping("add-user")
    public BaseResponse<User> addUser(@RequestBody UserRequest userRequest,HttpServletRequest request)
    {
        User user=new User();
        System.out.println(userRequest.getAccess());
        user.setId(userRequest.getId());
        user.setNickname(userRequest.getNickName());
        if(userRequest.getAccess().equals("student")) user.setAccess(1);
        else if(userRequest.getAccess().equals("teacher")) user.setAccess(2);
        else user.setAccess(0);
        //user.setAccess(userRequest.getAccess());
        user.setAvatarUrl("https://pic.imgdb.cn/item/64f7dd30661c6c8e54d951e4.png");
        //user.setPhone(userRequest.getPhone());
        user.setPsw("123456");
        userMapper.insertUser(user);
        return ResultUtils.success(null);
    }
    @PostMapping ("delete-users")
    public BaseResponse<User> deleteUsers(@RequestBody UserDeletes userDeletes,HttpServletRequest request)
    {
        for(String userid:userDeletes.getId())
        {
            userService.deleteUser(userid);
        }
        return ResultUtils.success(null);
    }
    @PostMapping("modify-user")
    public BaseResponse<User> modifyUser(@RequestBody UserModifyRequset userModifyRequset,HttpServletRequest request)
    {
        LambdaUpdateWrapper<User> updateWrapper=new LambdaUpdateWrapper<>();
        int ass=1;
        if(userModifyRequset.getAccess().equals("student")) ass=1;
        if(userModifyRequset.getAccess().equals("teacher")) ass=2;
        if(userModifyRequset.getAccess().equals("admin")) ass=0;
        System.out.println(userModifyRequset);
        userMapper.updateUser00(userModifyRequset.getNickName(),userModifyRequset.getEmail(),ass,userModifyRequset.getPhone(),userModifyRequset.getAvatarUrl(),userModifyRequset.getID());

        return ResultUtils.success(null);
    }
    @PostMapping("list-for-admin")
    public BaseResponse<ListforAdminDTO> ListforAdmin(@RequestBody UserListforAdminRequest userListforAdminRequest,HttpServletRequest request)
    {
        return userService.listforadmin(userListforAdminRequest,request);
    }

    @PostMapping("change-psw")
    public BaseResponse<Integer> modifyPassword(@RequestBody UserPasswordRequest userPasswordRequest, HttpServletRequest request)
    {
        return userService.modifyPassword(userPasswordRequest,request);
    }
    @PostMapping("delete")
    public BaseResponse<User> deleteone(@RequestBody UserDeleteRequest userDeleteRequest,HttpServletRequest request)
    {
        userService.deleteUser(userDeleteRequest.getId());

        return ResultUtils.success(null);
    }
    @PostMapping("sendPM")
    public BaseResponse<String> sendPM(@RequestBody SendPMRequest sendPMRequest,HttpServletRequest request){
        return userService.sendPM(sendPMRequest,request);
    }
    private UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
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

        return userDTO;
    }
}
