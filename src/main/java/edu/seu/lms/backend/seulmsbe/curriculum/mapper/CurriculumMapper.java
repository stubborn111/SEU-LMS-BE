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


    @Select("select * from curriculum where teacherID=#{teacherID}")
    List<Curriculum> selectCurriculumByteacher(String teacherID);//通过老师id查找课程

    @Select("select * from curriculum where name=#{name}")
    List<Curriculum> selectCurriculumByName(String name);//通过课程名称查找课程
    @Select("SELECT * FROM curriculum,student_curriculum "+
            "WHERE student_curriculum.studentID = #{userID}"+
            " AND curriculum.name Like CONCAT('%',#{keyword},'%') "+
            "AND student_curriculum.curriculumID = curriculum.ID "+
            "LIMIT #{begin},#{size}")
    //分页模糊查询登录学生的课程
    List<Curriculum> studentSearch(String keyword,String userID,int begin,int size);


    @Select("SELECT * FROM user,student_curriculum "+
            "WHERE student_curriculum.curriculumID = #{courseID}"+
            " AND user.nickname Like CONCAT('%',#{keyword},'%') "+
            "AND student_curriculum.studentID = user.ID "+
            "AND user.access = 1 "+
            "LIMIT #{begin},#{size}")
        //分页通过课程id模糊查询课程
    List<User> listStudent(String keyword,String courseID,int begin,int size);
    @Select("select SUM(" +
            "case " +
            "WHEN student_curriculum.curriculumID = #{courseID}"+
            " AND user.nickname Like CONCAT('%',#{keyword},'%') "+
            "AND student_curriculum.studentID = user.ID "+
            "AND user.access = 1 THEN "+
            " 1" +
            " else" +
            " 0" +
            " end" +
            ") from user,student_curriculum")
    Integer getListStudentNum(String keyword,String courseID);
    //通过课程id准确查询，用户名称模糊查询，找到符合条件的所有用户的数量
    @Select("select * from curriculum,user" +
            " where curriculum.name like CONCAT('%',#{keyword},'%')" +
            " AND user.nickname like CONCAT('%',#{teacherName},'%')" +
            " AND curriculum.teacherID=user.ID" +
            " limit #{begin},#{size}")
    //分页，通过课程名称和老师名称模糊查询所有符合条件的课程
    List<Curriculum> SearchByNameAndTeacher(String keyword,String teacherName,int begin,int size);

    @Select("select * from curriculum" +
            " limit #{begin},#{size}")
    //分页显示所有课程
    List<Curriculum> SearchCourse(String keyword,String teacherName,int begin,int size);

    @Select("select SUM(" +
            "case " +
            " when curriculum.name like CONCAT('%',#{keyword},'%')" +
            " AND user.nickname like CONCAT('%',#{teacherName},'%')" +
            " AND curriculum.teacherID=user.ID then" +
            " 1" +
            " else" +
            " 0" +
            " end" +
            ") from curriculum,user")
    //找到符合条件的课程数量，模糊搜索课程名称和老师名称
    Integer getnumAdmin(String keyword,String teacherName);
    @Select("select count(*) from curriculum")
        //找到所有课程的数量
    Integer getNumAdmin0();
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
