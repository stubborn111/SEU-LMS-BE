package edu.seu.lms.backend.seulmsbe.assignment.mapper;

import edu.seu.lms.backend.seulmsbe.assignment.entity.Assignment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
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
 * @since 2023-08-29
 */
public interface AssignmentMapper extends BaseMapper<Assignment> {
    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN assignment.syllabusID = #{syllabusID}"+
            " AND assignment.status = 0 THEN "+
            "1 "+
            "else "+
            "0 "+
            "end"+
            ") FROM assignment ")
    //找到在一个大纲下的未提交作业的数量
    Integer getStatus0num(String syllabusID);

    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN assignment.syllabusID = #{syllabusID}"+
            " AND assignment.status = 1 THEN "+
            "1 "+
            "else "+
            "0 "+
            "end"+
            ") FROM assignment ")
        //找到在一个大纲下的已提交作业的数量
    Integer getStatus1num(String syllabusID);

    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN assignment.syllabusID = #{syllabusID}"+
            " AND assignment.status = 2 THEN "+
            "1 "+
            "else "+
            "0 "+
            "end"+
            ") FROM assignment ")
        //找到在一个大纲下的已批改作业的数量
    Integer getStatus2num(String syllabusID);

    @Select(" SELECT AVG(assignment.score) FROM assignment WHERE assignment.syllabusID = #{syllabusID} ")
    //获得一条大纲的平均成绩
    Integer getAvgScore(String syllabusID);

    @Select("SELECT * FROM assignment WHERE syllabusID = #{id}")
    //找到一条大纲下的所有作业
    List<Assignment> getlist(String id);

    @Select("SELECT SUM(CASE WHEN assignment.status = 2 " +
            "AND DATE(assignment.time) = CURDATE() " +
            "THEN 1 else 0 end) FROM assignment")
    Integer getToday();

    @Select("SELECT SUM(CASE WHEN assignment.status = 2 " +
            "AND DATE(assignment.time) = CURDATE()-interval 1 day " +
            "THEN 1 else 0 end) FROM assignment")
    Integer getYesterday();

    @Select("SELECT SUM(CASE WHEN assignment.status = 2 " +
            "AND DATE(assignment.time) = CURDATE()-interval 2 day " +
            "THEN 1 else 0 end) FROM assignment")
    Integer getTwoday();

    @Select("select count(*)\n" +
            "from assignment where score<#{end} and score>=#{begin} and status=2 and syllabusID=#{syllabusID}")
    //找到在一条大纲下特定分数区段的作业数量
    Integer getScoreNum(float begin,float end,String syllabusID);
    @Select("select count(*)\n" +
            "from assignment where score=#{score} and status=2 and syllabusID=#{syllabusID}")
    //找到在一条大纲下某个分数的作业数量
    Integer getScoreOneNum(float score,String syllabusID);
    @Select("select count(*)\n" +
            "from assignment where status!=2 and syllabusID=#{syllabusID}")
    //找到一条大纲下未批改作业的数量
    Integer getNotCheckinNum(String syllabusID);
    @Select("select count(*) from assignment where syllabusID=#{syllabusID} and status=2")
    //找到一条大纲下已批改作业的数量
    Integer getAssignmentNum(String syllabusID);
    @Select("select SUM(score) from assignment where syllabusID=#{syllabusID} and status=2")
    //找到一条大纲下所有分数的总和
    Integer getAllScore(String syllabusID);

    @Update("update assignment set file=#{homeworkUrl},status=1 ,type=#{type},name=#{homeworkTitle},time=now() where studentID=#{userID} and syllabusID=#{syllabusID}")
    //修改
    void syllabusPostFile(String userID, String syllabusID, String homeworkTitle, String homeworkUrl, String type);

    @Update("update assignment set score=#{rate},feedback=#{feedback},status=2 where ID=#{homeworkID}")
    void syllabusFeedback(String homeworkID,int rate,String feedback);

    @Select("select * from assignment where syllabusID=#{syllabusID} and studentID=#{studentID}")
    Assignment selectAssignment(String syllabusID,String studentID);
    @Select("select * from assignment where name=#{name} and syllabusID=#{syllabusID}")
    Assignment selectAssignmentBySyllabus(String name,String syllabusID);

    @Delete("delete from assignment where syllabusID=#{syllabusID}")
    void deleteBySyllabusID(String syllabusID);
    @Delete("delete from assignment where studentID=#{userID}")
    void deleteByUserID(String userID);
}
