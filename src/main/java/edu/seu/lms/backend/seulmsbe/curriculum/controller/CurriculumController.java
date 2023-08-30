package edu.seu.lms.backend.seulmsbe.curriculum.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Student_Curriculum;
import edu.seu.lms.backend.seulmsbe.curriculum.mapper.CurriculumMapper;
import edu.seu.lms.backend.seulmsbe.curriculum.service.ICurriculumService;
import edu.seu.lms.backend.seulmsbe.dto.CourseDataDTO;
import edu.seu.lms.backend.seulmsbe.dto.CourseSearchDTO;
import edu.seu.lms.backend.seulmsbe.dto.CurriculumDTO;
import edu.seu.lms.backend.seulmsbe.request.CourseSearchRequest;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/list")
    public BaseResponse<CurriculumDTO> findPage(String userID, int currentPage, int pageSize)
    {
        List<Student_Curriculum> getStudent_Curriculum=
                curriculumMapper.selectCurriculumByStudent(userID);
        List<Curriculum> curriculum=new ArrayList<Curriculum>();
        for(int i=0;i<getStudent_Curriculum.size();i++)
        {
            curriculum.add(curriculumMapper.
                    getCurriculumById(getStudent_Curriculum.get(i).getCurriculumID()));
        }
        PageHelper.startPage(currentPage,pageSize);
        PageInfo<Curriculum> pageInfo=new PageInfo<>(curriculum);
        int num=(int)pageInfo.getTotal();
        CurriculumDTO dto=new CurriculumDTO(num,pageInfo.getList());
        return ResultUtils.success(dto);
    }

    @GetMapping("/get-into")
    public BaseResponse<CourseDataDTO> findCourse(String id)
    {
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
    public BaseResponse<CourseSearchDTO> courseSearch(@RequestBody CourseSearchRequest courseSearchRequest, HttpServletRequest request){
        System.out.println("flag");
        return iCurriculumService.searchCourse(courseSearchRequest,request);
    }
}
