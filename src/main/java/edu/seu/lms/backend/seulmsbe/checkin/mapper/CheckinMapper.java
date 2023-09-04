package edu.seu.lms.backend.seulmsbe.checkin.mapper;

import edu.seu.lms.backend.seulmsbe.checkin.entity.Checkin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
public interface CheckinMapper extends BaseMapper<Checkin> {

    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN checkin.syllabusID = #{syllabusID}"+
            " AND checkin.isCheckedIn = 1 THEN "+
            "1 "+
            "else "+
            "NULL "+
            "end"+
            ") FROM checkin ")
    Integer getCheckedNum(String syllabusID);
    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN checkin.syllabusID = #{syllabusID}"+
            " AND checkin.isCheckedIn = 0 THEN "+
            "1 "+
            "else "+
            "NULL "+
            "end"+
            ") FROM checkin ")
    Integer getNotCheckedNum(String syllabusID);

}
