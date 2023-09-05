package edu.seu.lms.backend.seulmsbe.curriculum.mapper;

import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import io.swagger.models.auth.In;
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


    @Insert("insert into curriculum(id,name,imgUrl,teacherID,description,semester)" +
            "value (#{id},#{name},#{imgUrl},#{teacherID},#{description},#{semester})")
    void insertCurriculum(Curriculum curriculum);//加入课程

//    @Select("select * from curriculum limit #{currentPage},#{pageSize}")
//    List<Curriculum> selectPage(int currentPage,int pageSize);


    @Select("select * from user where id=#{id}")
    User selectUserById(String id);

    @Select("SELECT * FROM curriculum,student_curriculum "+
            "WHERE student_curriculum.studentID = #{userID}"+
            " AND curriculum.name Like CONCAT('%',#{keyword},'%') "+
            "AND student_curriculum.curriculumID = curriculum.ID "+
            "LIMIT #{begin},#{size}")
    //分页模糊查询登录学生的课程
    List<Curriculum> studentSearch(String keyword,String userID,int begin,int size);

    @Select("select * from curriculum,user" +
            " where curriculum.name like CONCAT('%',#{keyword},'%')" +
            " AND user.nickname like CONCAT('%',#{teacherName},'%')" +
            " AND curriculum.teacherID=user.ID" +
            " limit #{begin},#{size}")
    List<Curriculum> SearchByNameAndTeacher(String keyword,String teacherName,int begin,int size);

    @Select("select SUM(" +
            "case " +
            " when curriculum.name like CONCAT('%',#{keyword},'%')" +
            " AND user.nickname like CONCAT('%',#{teacherName},'%')" +
            " AND curriculum.teacherID=user.ID then" +
            " 1" +
            " else" +
            " NULL" +
            " end" +
            ") from curriculum,user")
    int getnumAdmin(String keyword,String teacherName);
    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN student_curriculum.studentID = #{userID}"+
            " AND curriculum.name Like CONCAT('%',#{keyword},'%') "+
            "AND student_curriculum.curriculumID = curriculum.ID THEN "+
            "1 "+
            "else "+
            "NULL "+
            "end"+
            ") FROM curriculum,student_curriculum ")
    //返回登录学生所模糊搜索的课程总数
    Integer getnum(String keyword, String userID);
}
