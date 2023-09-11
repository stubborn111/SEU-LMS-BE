package edu.seu.lms.backend.seulmsbe.curriculum.controller;


import com.baomidou.mybatisplus.extension.api.R;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.mapper.StudentCurriculumMapper;
import edu.seu.lms.backend.seulmsbe.assignment.mapper.AssignmentMapper;
import edu.seu.lms.backend.seulmsbe.checkin.mapper.CheckinMapper;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import edu.seu.lms.backend.seulmsbe.curriculum.mapper.CurriculumMapper;
import edu.seu.lms.backend.seulmsbe.curriculum.service.ICurriculumService;
import edu.seu.lms.backend.seulmsbe.discussion.mapper.DiscussionMapper;
import edu.seu.lms.backend.seulmsbe.dto.Course.*;
import edu.seu.lms.backend.seulmsbe.event.mapper.EventMapper;
import edu.seu.lms.backend.seulmsbe.file.mapper.FileMapper;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import edu.seu.lms.backend.seulmsbe.syllabus.mapper.SyllabusMapper;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static edu.seu.lms.backend.seulmsbe.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
@RestController
@RequestMapping("/course")
public class CurriculumController {
    @Autowired
    private ICurriculumService iCurriculumService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CurriculumMapper curriculumMapper;
    @PostMapping("/student-list")
    //如果参数为空，列出所有课程,如果有参数，根据参数搜索列出包含参数的所有课程,搜索为模糊搜索，并且分页列出
    public BaseResponse<CourseListDTO> findPage(@RequestBody CoursePageRequest coursePageRequest, HttpServletRequest request)
    {
        return iCurriculumService.studentListCourse(coursePageRequest,request);
    }
    @PostMapping("/teacher-list")
    //通过老师id搜索老师负责的课程，并且分页列出
    public BaseResponse<CourseListDTO> teacherlist(@RequestBody CourseListRequest courseListRequest,HttpServletRequest request)
    {
        return iCurriculumService.teacehrList(courseListRequest,request);
    }
    @PostMapping("/list-student")
    //要先根据courseID找到课程，然后列出该课程的所有学生，之后再进行模糊搜索的操作，分页列出
    public BaseResponse<CourseListStudentDTO> listStudent(@RequestBody CourseListStudent2Request courseListStudentRequest,HttpServletRequest request){
        return iCurriculumService.listStudent(courseListStudentRequest,request);
    }


    @PostMapping("/test")
    public void test( )
    {
        System.out.println(curriculumMapper.selectById("1"));
    }

    @PostMapping("/student-search")
    //模糊搜索课程，显示课程的所有信息1
    public BaseResponse<CourseSearchDTO> courseSearch(@RequestBody CourseSearchRequest courseSearchRequest, HttpServletRequest request){
        return iCurriculumService.studentsearchCourse(courseSearchRequest,request);
    }
    @PostMapping("teacher-search")
    //分页模糊搜索课程
    public BaseResponse<CourseListDTO> courseTeacherSearch(@RequestBody CourseSearchRequest courseSearchRequest,HttpServletRequest request)
    {
        return iCurriculumService.teacherSearch(courseSearchRequest,request);
    }

    @PostMapping("/list-for-teacher")
    public BaseResponse<CourseListforTeacherDTO> courselistforteacher(@RequestBody CouseListforTeacherRequest couseListforTeacherRequest, HttpServletRequest request)
    {
        return iCurriculumService.listforteacher(couseListforTeacherRequest,request);
    }

    @PostMapping("/delete")
    //删除课程以及它所有的相关内容
    public  BaseResponse<CourseData3DTO> delete(@RequestBody CourseGetIntoRequest courseGetIntoRequest, HttpServletRequest request)
    {
        iCurriculumService.deleteCourseByID(courseGetIntoRequest.getCourseID());
        return ResultUtils.success(null);
    }
    @PostMapping("/add")
    //添加课程
    public BaseResponse<CourseaddRequest> addCouse(@RequestBody CourseaddRequest courseaddRequest,HttpServletRequest request)
    {
        return iCurriculumService.addCourse(courseaddRequest,request);
    }
    @GetMapping("/list-description")
    //若请求来自老师则搜索对应老师的课程的id和名称，若为管理员则返回全部课程的id和名称
    public BaseResponse<CourseListDescriptionDTO> listDescription(HttpServletRequest request)
    {
        return iCurriculumService.listDescription(request);
    }
    @PostMapping("/get-name")
    //通过课程id得到课程名称
    public BaseResponse<CourseNameDTO> getCourseName(@RequestBody CourseGetIntoRequest courseGetIntoRequest,HttpServletRequest request)
    {
        CourseNameDTO DTO=new CourseNameDTO();
        DTO.setCourseName(curriculumMapper.selectById(courseGetIntoRequest.getCourseID()).getName());
        return ResultUtils.success(DTO);
    }
    @PostMapping("/modify")
    //根据courseID找到课程，然后将剩余项目更新
    public BaseResponse<Curriculum> modifyCourse(@RequestBody CourseModifyRequest courseModifyRequest, HttpServletRequest request)
    {
        return iCurriculumService.modifyCourse(courseModifyRequest,request);
    }
    @PostMapping("/get-teacher-info")
    //根据课程id返回教师的电话和email
    public BaseResponse<CourseTeacherDTO> getTeacherInfo(@RequestBody CourseGetIntoRequest courseGetIntoRequest, HttpServletRequest request)
    {
        return iCurriculumService.getTeacherInfo(courseGetIntoRequest,request);
    }

    @PostMapping("/get-intro")
    //通过课程id搜索课程，返回课程的所有信息
    public BaseResponse<CourseInfoDTO> getInto(@RequestBody CourseGetIntoRequest courseGetIntoRequest, HttpServletRequest request)
    {
        return iCurriculumService.getInto(courseGetIntoRequest,request);
    }
    @PostMapping("/admin-list")
    //如果参数为空，列出所有课程，如果有参数，根据参数搜索，列出包含参数的所有课程，模糊搜索
    public BaseResponse<CourseAdminDTO> adminList(@RequestBody CourseAdminRequest courseAdminRequest, HttpServletRequest request)
    {
        return iCurriculumService.adminList(courseAdminRequest,request);
    }
    @PostMapping("/sendNotice")
    public BaseResponse<String> sendNotice(@RequestBody SendNoticeRequest sendNoticeRequest,HttpServletRequest request){
        return iCurriculumService.sendNotice(sendNoticeRequest,request);
    }
    @GetMapping("/all-course")
    //返回所有课程的id，名称和负责教师
    public BaseResponse<CourseAllListDTO> allCourse(HttpServletRequest request)
    {
        List<Curriculum> curriculumList=curriculumMapper.findAll();
        CourseAllListDTO DTO=new CourseAllListDTO();
        List<CourseData3DTO> dto=new ArrayList<>();
        for (Curriculum tt:curriculumList)
        {
            CourseData3DTO tmp=new CourseData3DTO();
            tmp.setCourseID(tt.getId());
            tmp.setCourseName(tt.getName());
            tmp.setTeacherName(userMapper.selectById(tt.getTeacherID()).getNickname());
            dto.add(tmp);
        }
        DTO.setList(dto);
        return ResultUtils.success(DTO);
    }


}
