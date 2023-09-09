package edu.seu.lms.backend.seulmsbe.event.mapper;

import edu.seu.lms.backend.seulmsbe.event.entity.Event;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
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
public interface EventMapper extends BaseMapper<Event> {

    @Select("select * from event where userID=#{userID}")
    List<Event> selectEventByUserID(String userID);
    @Delete("delete from event where syllabusID=#{syllabusID}")
    void deleteBySyllabusID(String syllabusID);

    @Delete("delete from event where userID=#{userID}")
    void deleteByUserID(String userID);
}
