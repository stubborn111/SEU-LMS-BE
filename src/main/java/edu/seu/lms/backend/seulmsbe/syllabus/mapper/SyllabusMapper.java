package edu.seu.lms.backend.seulmsbe.syllabus.mapper;

import edu.seu.lms.backend.seulmsbe.assignment.entity.Assignment;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
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

    /*
    getSyllabusByCourseID：通过课程id得到大纲
    updateAssignPostText：更新作业
    deleteByCourseID：通过课程id删除大纲
    deleteBySyllabusID：通过大纲id删除大纲
    updateMaterials：更新课件
    */
    @Select("SELECT * FROM syllabus where syllabus.curriculumID = #{courseID} order by syllabus.time desc limit 0,1")
    Syllabus getlatest(@Param("courseID") String courseID);

    @Select("select count(*) from syllabus")
    int AllNum();
    @Select("select count(*) from syllabus where syllabus.isCheckedIn=1")
    int CheckinNum();
    @Select("select * from syllabus where curriculumID=#{curriculumID} and isCheckedIn=1")
    List<Syllabus> getSyllabusByCourseID(String curriculumID);

    @Select("select * from syllabus where curriculumID=#{curriculumID}")
    List<Syllabus> getSyllabusByCourseID2(String curriculumID);
    @Update("update Assignment set file=#{body},name=#{homeworkTitle},type='multi-text',status=1,time=NOW() where syllabusID=#{syllabusID} and studentID=#{userID}")
    void updateAssignPostText(String syllabusID, String userID, String homeworkTitle, String body);
    @Delete("delete from syllabus where curriculumID=#{curriculumID}")
    void deleteByCourseID(String curriculumID);

    @Delete("delete from syllabus where ID=#{syllabusID}")
    void deleteBySyllabusID(String syllabusID);
    @Select("select * from syllabus where curriculumID=#{curriculumID}")
    List<Syllabus> getSyllabusByCurriculumID(String curriculumID);
    @Update("update Syllabus set materials=#{materials} where ID=#{syllabusID}")
    void updateMaterials(String materials,String syllabusID);
}
