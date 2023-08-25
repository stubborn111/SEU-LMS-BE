package edu.seu.lms.backend.seulmsbe.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.lms.backend.seulmsbe.user.entity.User;

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
}
