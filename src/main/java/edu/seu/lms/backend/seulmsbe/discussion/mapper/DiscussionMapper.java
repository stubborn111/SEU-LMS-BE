package edu.seu.lms.backend.seulmsbe.discussion.mapper;

import edu.seu.lms.backend.seulmsbe.discussion.entity.Discussion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.lms.backend.seulmsbe.message.entity.Message;
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
public interface DiscussionMapper extends BaseMapper<Discussion> {
    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN discussion.replyID is null THEN "+
            "1 "+
            "else "+
            "0 "+
            "end"+
            ") FROM discussion ")
    Integer getDiscussionNum();

    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN discussion.replyID is not null THEN "+
            "1 "+
            "else "+
            "0 "+
            "end"+
            ") FROM discussion ")
    Integer getReplyNum();
    @Insert("insert into discussion(id,curriculumID,fromUserID,content,title,time)" +
            " value (#{id},#{curriculumID},#{fromUserID},#{content},#{title},#{time})")
    void insertDiscussion(Discussion discussion);

    @Select("SELECT * FROM discussion "+
            "WHERE discussion.curriculumID = #{courseID} AND discussion.replyID is NULL "+
            "order by discussion.time desc "+
            "LIMIT #{begin},#{size}")
    List<Discussion> getlist(String courseID, int begin, int size);

    @Delete("delete from discussion where curriculumID=#{curriculumID}")
    void deleteByCurriculumID(String curriculumID);

    @Update("update discussion set fromUserID='000000000' where fromUserID=#{userID}")
    void updateFromUserID(String userID);
}
