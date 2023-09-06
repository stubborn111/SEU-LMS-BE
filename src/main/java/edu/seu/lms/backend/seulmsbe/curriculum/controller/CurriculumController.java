package edu.seu.lms.backend.seulmsbe.curriculum.controller;


import com.baomidou.mybatisplus.extension.api.R;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import edu.seu.lms.backend.seulmsbe.curriculum.mapper.CurriculumMapper;
import edu.seu.lms.backend.seulmsbe.curriculum.service.ICurriculumService;
import edu.seu.lms.backend.seulmsbe.dto.Course.*;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    private CurriculumMapper curriculumMapper;

    @PostMapping("/student-list")
    //1
    public BaseResponse<CourseListDTO> findPage(@RequestBody CoursePageRequest coursePageRequest, HttpServletRequest request)
    {
        return iCurriculumService.studentListCourse(coursePageRequest,request);
    }
    @PostMapping("/teacher-list")
    //1
    public BaseResponse<CourseListDTO> teacherlist(@RequestBody CourseListRequest courseListRequest,HttpServletRequest request)
    {
        return iCurriculumService.teacehrList(courseListRequest,request);
    }
    @PostMapping("/list-student")
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
    //1
    public  BaseResponse<CourseData3DTO> delete(@RequestBody CourseGetIntoRequest courseGetIntoRequest, HttpServletRequest request)
    {
        curriculumMapper.deleteById(courseGetIntoRequest.getCourseID());
        return ResultUtils.success(null);
    }
    @PostMapping("/add")
    //1
    public BaseResponse<CourseaddRequest> addCouse(@RequestBody CourseaddRequest courseaddRequest,HttpServletRequest request)
    {
        return iCurriculumService.addCourse(courseaddRequest,request);
    }
    @GetMapping("/list-description")
    //未测试
    public BaseResponse<CourseListDescriptionDTO> listDescription(HttpServletRequest request)
    {
        return iCurriculumService.listDescription(request);
    }
    @PostMapping("/get-name")
    public BaseResponse<CourseNameDTO> getCourseName(@RequestBody CourseGetIntoRequest courseGetIntoRequest,HttpServletRequest request)
    {
        return iCurriculumService.getCourseName(courseGetIntoRequest,request);
    }
    @PostMapping("/modify")
    public BaseResponse<Curriculum> modifyCourse(@RequestBody CourseModifyRequest courseModifyRequest, HttpServletRequest request)
    {
        return iCurriculumService.modifyCourse(courseModifyRequest,request);
    }
    @PostMapping("/get-teacher-info")
    public BaseResponse<CourseTeacherDTO> getTeacherInfo(@RequestBody CourseGetIntoRequest courseGetIntoRequest, HttpServletRequest request)
    {
        return iCurriculumService.getTeacherInfo(courseGetIntoRequest,request);
    }

    @PostMapping("/get-into")
    public BaseResponse<CourseGetinfoDTO> getInto(@RequestBody CourseGetIntoRequest courseGetIntoRequest, HttpServletRequest request)
    {
        return iCurriculumService.getInto(courseGetIntoRequest,request);
    }
    @PostMapping("/admin-list")
    public BaseResponse<CourseAdminDTO> adminList(@RequestBody CourseAdminRequest courseAdminRequest, HttpServletRequest request)
    {
        return iCurriculumService.adminList(courseAdminRequest,request);
    }

}
