package edu.seu.lms.backend.seulmsbe.curriculum.mapper;

import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
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
@Mapper
public interface CurriculumMapper extends BaseMapper<Curriculum> {

    @Select("select * from curriculum")//查询所有的课程
    List<Curriculum> findAll();

    @Select("select * from curriculum where id=#{id}")
    Curriculum getCurriculumById(String id);//通过课程id获得课程

    @Delete("delete from curriculum where id=#{id}")
    int deleteCurriculumById(String id);//通过课程id删除课程

    @Insert("insert into curriculum(id,name,imgUrl,teacherID,description)" +
            "value (#{id},#{name},#{imgUrl},#{teacherID},#{description})")
    int insertCurriculum(Curriculum curriculum);//加入课程

}
