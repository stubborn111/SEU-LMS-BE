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
import edu.seu.lms.backend.seulmsbe.dto.Course.*;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import edu.seu.lms.backend.seulmsbe.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Autowired
    UserMapper userMapper;
    public BaseResponse<CourseSearchDTO> studentsearchCourse(CourseSearchRequest courseSearchRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        //取出数据
        int pagesize = courseSearchRequest.getPageSize();
        String keyword = courseSearchRequest.getKeyword();
        int curPage = courseSearchRequest.getCurrentPage();
        //构建查询体
        LambdaUpdateWrapper<Curriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.like(Curriculum::getName,keyword);
        List<Curriculum> tmp = curriculumMapper.studentSearch(keyword,currentUser.getId(),pagesize*(curPage-1),pagesize);

        CourseSearchDTO dto = new CourseSearchDTO();
        //dto.setTotalNum((int)Page.getTotal());

        //List<Curriculum> tmp = Page.getRecords();
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
        if (curriculumMapper.getnum(keyword,currentUser.getId())!=null) dto.setTotalNum(curriculumMapper.getnum(keyword,currentUser.getId()));
        else dto.setTotalNum(0);
        return ResultUtils.success(dto);
    }




    @Override
    //分页返回用户的课程信息
    public BaseResponse<CourseListDTO> studentListCourse(CoursePageRequest coursePageRequest, HttpServletRequest request) {
        //取出数据
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        int pagesize = coursePageRequest.getPageSize();
        String userid = currentUser.getId();
        int curPage = coursePageRequest.getCurrentPage();
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
            String stuId=studentCurriculum.getCurriculumID();
            temp.setCourseID(studentCurriculum.getCurriculumID());
            Curriculum curriculum=curriculumMapper.selectById(stuId);
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

    @Override
    //通过教师的id找到他所有的课程，返回课程id和name
    public BaseResponse<CourseListforTeacherDTO> listforteacher(CouseListforTeacherRequest couseListforTeacherRequest, HttpServletRequest request) {
        String teacherId=couseListforTeacherRequest.getTeacherID();
        LambdaUpdateWrapper<Curriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(Curriculum::getTeacherID,teacherId);
        List<Curriculum> curriculumList=curriculumMapper.selectList(queryMapper);
        List<CourseData3DTO> courseData3DTOList=new ArrayList<>();
        CourseListforTeacherDTO courseListforTeacherDTO=new CourseListforTeacherDTO();
        for(Curriculum curriculum:curriculumList)
        {
            CourseData3DTO courseData3DTO=new CourseData3DTO();
            courseData3DTO.setCourseID(curriculum.getId());
            courseData3DTO.setCourseName(curriculum.getName());
            courseData3DTOList.add(courseData3DTO);
        }
        courseListforTeacherDTO.setTabList(courseData3DTOList);
        return ResultUtils.success(courseListforTeacherDTO);
    }

    @Override
    public BaseResponse<CourseaddRequest> addCourse(CourseaddRequest courseaddRequest, HttpServletRequest request) {
        Curriculum curriculum=new Curriculum();
        curriculum.setId(UUID.randomUUID().toString().substring(0,7));
        curriculum.setName(courseaddRequest.getCourseName());
        curriculum.setSemester(courseaddRequest.getSemester());
        curriculum.setImgUrl(courseaddRequest.getImgUrl());
        String teacherName=courseaddRequest.getTeacherName();
        LambdaUpdateWrapper<User> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(User::getNickname,teacherName);
        User teacher=userMapper.selectOne(queryMapper);
        curriculum.setTeacherID(teacher.getId());
        curriculumMapper.insertCurriculum(curriculum);
        return ResultUtils.success(null);
    }

    @Override
    public BaseResponse<CourseListDTO> teacehrList(CourseListRequest courseListRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        int pagesize =courseListRequest.getPageSize();
        String userid = courseListRequest.getUserID();
        int curPage = courseListRequest.getCurrentPage();
        //构建查询体
        LambdaUpdateWrapper<Curriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(Curriculum::getTeacherID,currentUser.getId());

        Page<Curriculum> Page = curriculumMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        List<Curriculum> curriculumList=Page.getRecords();
        CourseListDTO DTO=new CourseListDTO();
        List<CourseData2DTO> dto=new ArrayList<>();
        DTO.setTotalNum((int)Page.getTotal());
        for(Curriculum tt:curriculumList)
        {
            CourseData2DTO courseData2DTO=new CourseData2DTO();
            courseData2DTO.setCourseID(tt.getId());
            courseData2DTO.setCourseName(tt.getName());
            courseData2DTO.setDescription(tt.getDescription());
            courseData2DTO.setImgUrl(tt.getImgUrl());
            courseData2DTO.setSemester(tt.getSemester());
            User teacher=userService.getuser(tt.getTeacherID());
            courseData2DTO.setTeacherName(teacher.getNickname());
            courseData2DTO.setTeacherAvatar(teacher.getAvatarUrl());
            dto.add(courseData2DTO);
        }
        DTO.setList(dto);
        return ResultUtils.success(DTO);
    }

    @Override
    public BaseResponse<CourseListDescriptionDTO> listDescription(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        int access=currentUser.getAccess();
        CourseListDescriptionDTO DTO=new CourseListDescriptionDTO();
        List<CourseData3DTO> dto=new ArrayList<>();
        LambdaUpdateWrapper<Curriculum> queryWrapper=new LambdaUpdateWrapper<>();
        List<Curriculum> curriculumList=new ArrayList<>();
        if(access==0)//管理员
        {
            curriculumList=curriculumMapper.findAll();
        }
        else if (access==2)//老师
            {
            queryWrapper.eq(Curriculum::getTeacherID,currentUser.getId());
            curriculumList=curriculumMapper.selectList(queryWrapper);
        }
        for(Curriculum tt:curriculumList)
        {
            CourseData3DTO tem=new CourseData3DTO();
            tem.setCourseName(tt.getName());
            tem.setCourseID(tt.getId());
            dto.add(tem);
        }
        DTO.setDescriptionList(dto);
        return ResultUtils.success(DTO);
    }

    @Override
    public BaseResponse<CourseListDTO> teacherSearch(CourseSearchRequest courseSearchRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String userId=currentUser.getId();
        String keword=courseSearchRequest.getKeyword();
        int pagesize =courseSearchRequest.getPageSize();
        int curPage = courseSearchRequest.getCurrentPage();
        LambdaUpdateWrapper<Curriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(Curriculum::getTeacherID,userId);
        queryMapper.like(Curriculum::getName,keword);
        Page<Curriculum> Page=curriculumMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        CourseListDTO DTO=new CourseListDTO();
        DTO.setTotalNum((int)Page.getTotal());
        List<Curriculum> curriculumList=Page.getRecords();
        List<CourseData2DTO> dto=new ArrayList<>();
        for(Curriculum tt:curriculumList)
        {
            CourseData2DTO courseData2DTO=new CourseData2DTO();
            courseData2DTO.setCourseID(tt.getId());
            courseData2DTO.setCourseName(tt.getName());
            courseData2DTO.setDescription(tt.getDescription());
            courseData2DTO.setImgUrl(tt.getImgUrl());
            courseData2DTO.setSemester(tt.getSemester());
            User teacher=userService.getuser(tt.getTeacherID());
            courseData2DTO.setTeacherName(teacher.getNickname());
            courseData2DTO.setTeacherAvatar(teacher.getAvatarUrl());
            dto.add(courseData2DTO);
        }
        DTO.setList(dto);
        return ResultUtils.success(DTO);
    }

}
