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
            " AND checkin.position = '已签到' THEN "+
            "1 "+
            "else "+
            "NULL "+
            "end"+
            ") FROM checkin ")
    int getCheckedNum(String syllabusID);
    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN checkin.syllabusID = #{syllabusID}"+
            " AND checkin.position = '未签到' THEN "+
            "1 "+
            "else "+
            "NULL "+
            "end"+
            ") FROM checkin ")
    int getNotCheckedNum(String syllabusID);

}
