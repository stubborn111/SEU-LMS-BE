package edu.seu.lms.backend.seulmsbe.syllabus.mapper;

import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
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
public interface SyllabusMapper extends BaseMapper<Syllabus> {
    @Select("SELECT * FROM syllabus where syllabus.curriculumID = #{courseID} order by syllabus.time desc limit 0,1")
    Syllabus getlatest(@Param("courseID") String courseID);
}
