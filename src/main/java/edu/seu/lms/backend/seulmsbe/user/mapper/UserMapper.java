package edu.seu.lms.backend.seulmsbe.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
public interface UserMapper extends BaseMapper<User> {
    @Insert("insert into user(nickname,id,email,access,phone,avatarUrl,psw)" +
            "values (#{nickname},#{id},#{email},#{access},#{phone},#{avatarUrl},#{psw})")
    void insertUser(User user);

    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN user.access = 0 THEN "+
            "1 "+
            "else "+
            "NULL "+
            "end"+
            ") FROM user ")
    Integer getStatus0Num();

    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN user.access = 1 THEN "+
            "1 "+
            "else "+
            "NULL "+
            "end"+
            ") FROM user ")
    Integer getStatus1Num();

    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN user.access = 2 THEN "+
            "1 "+
            "else "+
            "NULL "+
            "end"+
            ") FROM user ")
    Integer getStatus2Num();
}
