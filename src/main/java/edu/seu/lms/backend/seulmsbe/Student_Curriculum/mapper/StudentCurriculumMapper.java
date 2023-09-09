package edu.seu.lms.backend.seulmsbe.Student_Curriculum.mapper;

import edu.seu.lms.backend.seulmsbe.Student_Curriculum.entity.StudentCurriculum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author szh
 * @since 2023-08-31
 */
public interface StudentCurriculumMapper extends BaseMapper<StudentCurriculum> {
    @Select("SELECT COUNT(DISTINCT curriculumID)\n" +
            "FROM student_curriculum\n" +
            "WHERE curriculumID IN (\n" +
            "  SELECT curriculumID\n" +
            "  FROM student_curriculum\n" +
            "  GROUP BY curriculumID\n" +
            "  HAVING COUNT(DISTINCT studentID) between -1 and 20\n" +
            ")")
    Integer get0_20Num();
    @Select("SELECT COUNT(DISTINCT curriculumID)\n" +
            "FROM student_curriculum\n" +
            "WHERE curriculumID IN (\n" +
            "  SELECT curriculumID\n" +
            "  FROM student_curriculum\n" +
            "  GROUP BY curriculumID\n" +
            "  HAVING COUNT(DISTINCT studentID) between 20 and 40\n" +
            ")")
    Integer get20_40Num();
    @Select("SELECT COUNT(DISTINCT curriculumID)\n" +
            "FROM student_curriculum\n" +
            "WHERE curriculumID IN (\n" +
            "  SELECT curriculumID\n" +
            "  FROM student_curriculum\n" +
            "  GROUP BY curriculumID\n" +
            "  HAVING COUNT(DISTINCT studentID) > 40\n" +
            ")")
    Integer getOver40Num();

    @Select("select count(*) from student_curriculum where curriculumID=#{curriculumID}")
    int getNumofCourse(String curriculumID);

    @Select("select count(*) from student_curriculum where curriculumID=#{curriculumID}")
    Integer getCourseNum(String curriculumID);

    @Delete("delete  from student_curriculum where curriculumID=#{curriculumID}")
    void deleteByCourseID(String curriculumID);

}
