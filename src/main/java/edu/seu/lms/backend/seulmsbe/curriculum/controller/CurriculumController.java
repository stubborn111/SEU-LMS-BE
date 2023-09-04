package edu.seu.lms.backend.seulmsbe.curriculum.controller;


import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import edu.seu.lms.backend.seulmsbe.curriculum.mapper.CurriculumMapper;
import edu.seu.lms.backend.seulmsbe.curriculum.service.ICurriculumService;
import edu.seu.lms.backend.seulmsbe.dto.*;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    @GetMapping("/teacher-list")
    //1
    public BaseResponse<CourseListDTO> teacherlist(@RequestBody CourseListRequest courseListRequest,HttpServletRequest request)
    {
        return iCurriculumService.teacehrList(courseListRequest,request);
    }


    @GetMapping("/get-into")
    //通过课程id查找课程的所有信息1
    public BaseResponse<CourseDataDTO> findCourse(@RequestBody CourseGetIntoRequest courseGetIntoRequest,HttpServletRequest request)
    {
        String id=courseGetIntoRequest.getCourseId();
        Curriculum curriculum=curriculumMapper.getCurriculumById(id);
        String courseId=curriculum.getTeacherID();
        User teacher=curriculumMapper.selectUserById(courseId);
        CourseDataDTO courseDataDTO=new CourseDataDTO(curriculum.getName(),curriculum.getDescription(),curriculum.getImgUrl(),teacher.getNickname(),teacher.getAvatarUrl(),teacher.getPhone(),curriculum.getSemester(),teacher.getEmail());
        return ResultUtils.success(courseDataDTO);
    }

    @GetMapping("/test")
    public void test( )
    {
        System.out.println(curriculumMapper.selectById("1"));
    }

    @GetMapping("/student-search")
    //模糊搜索课程，显示课程的所有信息1
    public BaseResponse<CourseSearchDTO> courseSearch(@RequestBody CourseSearchRequest courseSearchRequest, HttpServletRequest request){
        return iCurriculumService.studentsearchCourse(courseSearchRequest,request);
    }
    @GetMapping("teacher-search")
    public BaseResponse<CourseListDTO> courseTeacherSearch(@RequestBody CourseSearchRequest courseSearchRequest,HttpServletRequest request)
    {
        return iCurriculumService.teacherSearch(courseSearchRequest,request);
    }

    @GetMapping("/list-for-teacehr")
    public BaseResponse<CourseListforTeacherDTO> courselistforteacher(@RequestBody CouseListforTeacherRequest couseListforTeacherRequest,HttpServletRequest request)
    {
        return iCurriculumService.listforteacher(couseListforTeacherRequest,request);
    }

    @GetMapping("/delete")
    //1
    public  BaseResponse<CourseData3DTO> delete(@RequestBody CourseGetIntoRequest courseGetIntoRequest,HttpServletRequest request)
    {
        curriculumMapper.deleteById(courseGetIntoRequest.getCourseId());
        return ResultUtils.success(null);
    }
    @GetMapping("/add")
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

}
