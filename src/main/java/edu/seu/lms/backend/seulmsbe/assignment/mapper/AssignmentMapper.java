package edu.seu.lms.backend.seulmsbe.assignment.mapper;

import edu.seu.lms.backend.seulmsbe.assignment.entity.Assignment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

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
            "NULL "+
            "end"+
            ") FROM assignment ")
    Integer getStatus0num(String syllabusID);

    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN assignment.syllabusID = #{syllabusID}"+
            " AND assignment.status = 1 THEN "+
            "1 "+
            "else "+
            "NULL "+
            "end"+
            ") FROM assignment ")
    Integer getStatus1num(String syllabusID);

    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN assignment.syllabusID = #{syllabusID}"+
            " AND assignment.status = 2 THEN "+
            "1 "+
            "else "+
            "NULL "+
            "end"+
            ") FROM assignment ")
    Integer getStatus2num(String syllabusID);

    @Select(" SELECT AVG(assignment.score) FROM assignment WHERE assignment.syllabusID = #{syllabusID} ")
    Integer getAvgScore(String syllabusID);

    @Select("SELECT * FROM assignment WHERE syllabusID = #{id}")
    List<Assignment> getlist(String id);
}
