package edu.seu.lms.backend.seulmsbe.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.dto.User.ListforAdminDTO;
import edu.seu.lms.backend.seulmsbe.dto.User.UserListTeacherDTO;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
public interface IUserService extends IService<User> {
    int createuser(User user);

    BaseResponse<String> userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    public User userLogin(String userId, String userPassword, HttpServletRequest request);
    public User getSafetyUser(User originUser);
    public void userLogout(HttpServletRequest request);
    public BaseResponse<Integer> modify(UserModifyRequest1 userModifyRequest1, HttpServletRequest request);
    public User getuser(String id);
    public BaseResponse<UserListTeacherDTO> listTeacher(TeacherListRequest teacherListRequest, HttpServletRequest request);
    public BaseResponse<Integer> modifyPassword(UserPasswordRequest userPasswordRequest,HttpServletRequest request);
    public BaseResponse<ListforAdminDTO> listforadmin(UserListforAdminRequest userListforAdminRequest, HttpServletRequest request);

    BaseResponse<String> sendPM(SendPMRequest sendPMRequest, HttpServletRequest request);
    void deleteStudent(String userID);
    void deleteTeacher(String userID);
    void deleteAdmin(String userID);

    void deleteUser(String userID);
}
