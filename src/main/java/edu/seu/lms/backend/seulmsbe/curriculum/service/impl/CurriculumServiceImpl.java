package edu.seu.lms.backend.seulmsbe.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import edu.seu.lms.backend.seulmsbe.curriculum.mapper.CurriculumMapper;
import edu.seu.lms.backend.seulmsbe.curriculum.service.ICurriculumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.dto.CourseData2DTO;
import edu.seu.lms.backend.seulmsbe.dto.CourseSearchDTO;
import edu.seu.lms.backend.seulmsbe.dto.SyllabusDTO;
import edu.seu.lms.backend.seulmsbe.dto.SyllabusListDTO;
import edu.seu.lms.backend.seulmsbe.request.CourseSearchRequest;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import edu.seu.lms.backend.seulmsbe.user.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static edu.seu.lms.backend.seulmsbe.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
@Service
public class CurriculumServiceImpl extends ServiceImpl<CurriculumMapper, Curriculum> implements ICurriculumService {
    @Autowired
    private CurriculumMapper curriculumMapper;

    @Autowired
    private IUserService userService;

    @Override
    public BaseResponse<CourseSearchDTO> searchCourse(CourseSearchRequest courseSearchRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        //取出数据
        int pagesize = courseSearchRequest.getPageSize();
        String keyword = courseSearchRequest.getKeyword();
        int curPage = courseSearchRequest.getCurrentPage();
        //构建查询体
        LambdaUpdateWrapper<Curriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.like(Curriculum::getName,keyword);

        Page<Curriculum> Page = curriculumMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        CourseSearchDTO dto = new CourseSearchDTO();
        dto.setTotalNum((int)Page.getTotal());

        List<Curriculum> tmp = Page.getRecords();
        List<CourseData2DTO> DTO = new ArrayList<>();

        for(Curriculum tt : tmp){
            CourseData2DTO temp = new CourseData2DTO();
            temp.setCourseID(tt.getId());
            temp.setCourseName(tt.getName());
            temp.setDescription(tt.getDescription());
            temp.setImgUrl(tt.getImgUrl());
            temp.setSemester(tt.getSemester());
            User teacher = userService.getuser(tt.getTeacherID());
            temp.setTeacherName(teacher.getNickname());
            temp.setTeacherAvatar(teacher.getAvatarUrl());
            DTO.add(temp);
        }
        dto.setList(DTO);
        return ResultUtils.success(dto);
    }
}
