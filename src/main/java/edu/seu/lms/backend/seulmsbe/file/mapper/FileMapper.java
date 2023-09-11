package edu.seu.lms.backend.seulmsbe.file.mapper;

import edu.seu.lms.backend.seulmsbe.file.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author szh
 * @since 2023-09-04
 */
public interface FileMapper extends BaseMapper<File> {
    /**
     * deleteBySyllabusID：根据大纲ID删除对应文件
     * insertFile：插入文件
     */
    @Delete("delete from file where syllabusID=#{syllabusID}")
    void deleteBySyllabusID(String syllabusID);

    @Select("select * from file where syllabusID=#{syllabusID}")
    List<File> selectFileList(String syllabusID);

    @Insert("insert into file(ID, type, name, time, description, url, status, syllabusID)" +
            " values (#{id},#{type},#{name},#{time},#{description},#{url},#{status},#{syllabusID})")
    void insertFile(File file);
}
