package edu.seu.lms.backend.seulmsbe.curriculum.controller;


import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import edu.seu.lms.backend.seulmsbe.curriculum.mapper.CurriculumMapper;
import edu.seu.lms.backend.seulmsbe.curriculum.service.ICurriculumService;
import edu.seu.lms.backend.seulmsbe.dto.CourseDataDTO;
import edu.seu.lms.backend.seulmsbe.dto.CourseListDTO;
import edu.seu.lms.backend.seulmsbe.dto.CourseSearchDTO;
import edu.seu.lms.backend.seulmsbe.request.CourseGetIntoRequest;
import edu.seu.lms.backend.seulmsbe.request.CourseListRequest;
import edu.seu.lms.backend.seulmsbe.request.CourseSearchRequest;
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

    @PostMapping("/list")
    public BaseResponse<CourseListDTO> findPage(@RequestBody CourseListRequest courseListRequest, HttpServletRequest request)
    {
        return iCurriculumService.listCourse(courseListRequest,request);
    }

    @GetMapping("/get-into")
    //通过课程id查找课程的所有信息
    public BaseResponse<CourseDataDTO> findCourse(@RequestBody CourseGetIntoRequest courseGetIntoRequest,HttpServletRequest request)
    {
        String id=courseGetIntoRequest.getId();
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

    @GetMapping("/search")
    //模糊搜索课程，显示课程的所有信息
    public BaseResponse<CourseSearchDTO> courseSearch(@RequestBody CourseSearchRequest courseSearchRequest, HttpServletRequest request){
        return iCurriculumService.searchCourse(courseSearchRequest,request);
    }
}
