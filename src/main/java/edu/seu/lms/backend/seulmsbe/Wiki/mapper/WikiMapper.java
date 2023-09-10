package edu.seu.lms.backend.seulmsbe.Wiki.mapper;

import edu.seu.lms.backend.seulmsbe.Wiki.entity.Wiki;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
public interface WikiMapper extends BaseMapper<Wiki> {
    @Select("SELECT * FROM wiki "+
            "order by wiki.isSolved asc,wiki.time desc "+
            "LIMIT #{begin},#{size}")
    List<Wiki> getList(int begin,int size);

    @Update("update wiki set fromUserID='000000000' where fromUserID=#{userID}")
    void updateWikiByUserID(String userID);
}
