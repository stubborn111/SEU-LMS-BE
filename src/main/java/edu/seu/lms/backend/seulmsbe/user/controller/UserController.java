package edu.seu.lms.backend.seulmsbe.user.controller;


import com.alibaba.fastjson.JSON;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ErrorCode;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.dto.UserDTO;
import edu.seu.lms.backend.seulmsbe.exception.BusinessException;
import edu.seu.lms.backend.seulmsbe.request.UserLoginRequest;
import edu.seu.lms.backend.seulmsbe.request.UserModifyRequest;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import edu.seu.lms.backend.seulmsbe.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 登出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //int result = userService.userLogout(request);
        return ResultUtils.success(null);
    }

    /**
     * 修改用户信息
     * @param userModifyRequest
     * @param request
     * @return
     */
    @PostMapping("/modify")
    public BaseResponse<Integer> userModify(@RequestBody UserModifyRequest userModifyRequest, HttpServletRequest request) {
        return userService.modify(userModifyRequest, request);
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

    private UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setPhone(user.getPhone());
        userDTO.setId(user.getId());
        userDTO.setNickName(user.getNickname());
        userDTO.setImgUrl(user.getAvatarUrl());
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
