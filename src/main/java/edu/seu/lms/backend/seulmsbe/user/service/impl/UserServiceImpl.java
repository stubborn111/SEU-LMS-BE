package edu.seu.lms.backend.seulmsbe.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ErrorCode;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.exception.BusinessException;
import edu.seu.lms.backend.seulmsbe.request.UserLoginRequest;
import edu.seu.lms.backend.seulmsbe.request.UserModifyRequest;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import edu.seu.lms.backend.seulmsbe.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import static edu.seu.lms.backend.seulmsbe.constant.UserConstant.USER_LOGIN_STATE;
import static com.sun.javafx.font.FontResource.SALT;
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
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户名或密码错误");
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
    public BaseResponse<Integer> modify(UserModifyRequest userModifyRequest, HttpServletRequest request) {
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

}
