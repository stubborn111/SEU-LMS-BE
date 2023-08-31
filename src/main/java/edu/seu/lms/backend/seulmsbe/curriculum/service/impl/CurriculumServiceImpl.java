package edu.seu.lms.backend.seulmsbe.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.entity.StudentCurriculum;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.mapper.StudentCurriculumMapper;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import edu.seu.lms.backend.seulmsbe.curriculum.mapper.CurriculumMapper;
import edu.seu.lms.backend.seulmsbe.curriculum.service.ICurriculumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.dto.*;
import edu.seu.lms.backend.seulmsbe.request.CourseListRequest;
import edu.seu.lms.backend.seulmsbe.request.CourseSearchRequest;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.service.IUserService;
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
    @Autowired
    StudentCurriculumMapper studentCurriculumMapper;

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

    @Override
    public BaseResponse<CourseListDTO> listCourse(CourseListRequest courseListRequest, HttpServletRequest request) {
        //取出数据
        int pagesize = courseListRequest.getPageSize();
        String userid = courseListRequest.getUserid();
        int curPage = courseListRequest.getCurrentPage();
        //构建查询体
        LambdaUpdateWrapper<StudentCurriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(StudentCurriculum::getStudentID,userid);

        Page<StudentCurriculum> Page = studentCurriculumMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        CourseListDTO dto = new CourseListDTO();
        dto.setTotalNum((int)Page.getTotal());

        List<StudentCurriculum> tmp = Page.getRecords();
        List<CourseData2DTO> DTO = new ArrayList<>();
        for(StudentCurriculum studentCurriculum:tmp)
        {
            CourseData2DTO temp=new CourseData2DTO();
            String id=studentCurriculum.getCurriculumID();
            temp.setCourseID(studentCurriculum.getCurriculumID());
            Curriculum curriculum=curriculumMapper.getCurriculumById(id);
            temp.setDescription(curriculum.getDescription());
            temp.setImgUrl(curriculum.getImgUrl());
            temp.setSemester(curriculum.getSemester());
            temp.setCourseName(curriculum.getName());

            User teacher = userService.getuser(curriculum.getTeacherID());
            temp.setTeacherName(teacher.getNickname());
            temp.setTeacherAvatar(teacher.getAvatarUrl());
            DTO.add(temp);
        }
        dto.setList(DTO);
        return ResultUtils.success(dto);
    }
}
