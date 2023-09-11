package edu.seu.lms.backend.seulmsbe.message.mapper;

import edu.seu.lms.backend.seulmsbe.message.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    /**
     * getlist：分页获取某人的消息列表
     * insertMessage：发送消息
     * deleteMessage：删除信息
     * updateFrom/ToUserID：处理被删除人遗留的消息
     * @return
     */
    @Select("SELECT * FROM message "+
            "WHERE message.toUserID = #{useID} "+
            "order by message.time desc "+
            "LIMIT #{begin},#{size}")
    List<Message> getlist(String useID,int begin,int size);

    @Insert("insert into message(ID, fromUserID, toUserID, content, isRead, time) " +
            "VALUES(#{id},#{fromUserID},#{toUserID},#{content},0,NOW())")
    void insertMessage(Message message);

    @Update("update message set fromUserID='000000000' where fromUserID=#{userID}")
    void updateFromUserID(String userID);

    @Update("update message set toUserID='000000000' where toUserID=#{userID}")
    void updateToUserID(String toUsrID);

    @Delete("delete from message where toUserID='000000000' and fromUserID='000000000'")
    void deleteMessage();
}
