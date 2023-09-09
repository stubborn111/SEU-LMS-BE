package edu.seu.lms.backend.seulmsbe.syllabus.mapper;

import edu.seu.lms.backend.seulmsbe.assignment.entity.Assignment;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

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

    @Select("select count(*) from syllabus")
    int AllNum();
    @Select("select count(*) from syllabus where syllabus.isCheckedIn=1")
    int CheckinNum();
    @Select("select * from syllabus where curriculumID=#{curriculumID} and isCheckedIn=1")
    List<Syllabus> getSyllabusByCourseID(String curriculumID);
    @Update("update Assignment set file=#{body},name=#{homeworkTitle},type='multi-text',status=1,time=NOW() where syllabusID=#{syllabusID} and studentID=#{userID}")
    void updateAssignPostText(String syllabusID, String userID, String homeworkTitle, String body);
}
