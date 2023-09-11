package edu.seu.lms.backend.seulmsbe.checkin.mapper;

import edu.seu.lms.backend.seulmsbe.checkin.entity.Checkin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import org.apache.ibatis.annotations.Delete;
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
public interface CheckinMapper extends BaseMapper<Checkin> {

/*    getCheckedNum:得到某条大纲已经签到的人数
      getNotCheckedNum：得到某条大纲未签到的人数
      deleteBySyllabusID：通过大纲id删除签到条目
      deleteByUserID：删除某个用户的所有签到记录
    */
    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN checkin.syllabusID = #{syllabusID}"+
            " AND checkin.isCheckedIn = 1 THEN "+
            "1 "+
            "else "+
            "0 "+
            "end"+
            ") FROM checkin ")
    Integer getCheckedNum(String syllabusID);
    @Select("SELECT SUM(" +
            "CASE "+
            "WHEN checkin.syllabusID = #{syllabusID}"+
            " AND checkin.isCheckedIn = 0 THEN "+
            "1 "+
            "else "+
            "0 "+
            "end"+
            ") FROM checkin ")
    Integer getNotCheckedNum(String syllabusID);

    @Select("SELECT SUM(CASE WHEN checkin.isCheckedIn = 1 " +
            "AND DATE(checkin.time) = CURDATE() " +
            "THEN 1 else 0 end) FROM checkin")
    Integer getCheckIntodayNum();

    @Select("SELECT SUM(CASE WHEN checkin.isCheckedIn = 1 " +
            "AND DATE(checkin.time) = CURDATE()-interval 1 day " +
            "THEN 1 else 0 end) FROM checkin")
    Integer getCheckInYesterdayNum();

    @Select("SELECT SUM(CASE WHEN checkin.isCheckedIn = 1 " +
            "AND DATE(checkin.time) = CURDATE()-interval 2 day " +
            "THEN 1 else 0 end) FROM checkin")
    Integer getCheckIntwodayNum();
    @Select("select count(*) from checkin where isCheckedIn=#{isCheckedIn}")
    int getCheckinNum(int isCheckedIn);
    @Select("select count(*) from checkin where isCheckedIn=#{isCheckedIn} and syllabusID=#{syllabusID}")//找到对应大纲的签到人数和未签到人数
    int getCheckinBySylNum(int isCheckedIn,String syllabusID);
    @Delete("delete from checkin where syllabusID=#{syllabusID}")
    void deleteBySyllabusID(String syllabusID);

    @Delete("delete from checkin where studentID=#{userID}")
    void deleteByUserID(String userID);

    @Select("SELECT * FROM checkin "+
            "WHERE syllabusID = #{syllabusID} and studentID=#{studentID} "+
            "order by time desc "+
            "LIMIT 1")
    Checkin selectOnCheckin(String syllabusID,String studentID);
}
