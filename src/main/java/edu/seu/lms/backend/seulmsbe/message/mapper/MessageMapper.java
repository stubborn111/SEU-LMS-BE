package edu.seu.lms.backend.seulmsbe.message.mapper;

import edu.seu.lms.backend.seulmsbe.message.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
public interface MessageMapper extends BaseMapper<Message> {
    @Select("SELECT * FROM message "+
            "WHERE message.toUserID = #{useID} "+
            "order by message.time desc "+
            "LIMIT #{begin},#{size}")
    List<Message> getlist(String useID,int begin,int size);

    @Insert("insert into message(ID, fromUserID, toUserID, content, isRead, time) " +
            "VALUES(#{id},#{fromUserID},#{toUserID},#{content},0,NOW())")
    void insertMessage(Message message);
}
