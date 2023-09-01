package edu.seu.lms.backend.seulmsbe.curriculum.service;

import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.lms.backend.seulmsbe.dto.*;
import edu.seu.lms.backend.seulmsbe.request.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
public interface ICurriculumService extends IService<Curriculum> {
    public BaseResponse<CourseSearchDTO> searchCourse(CourseSearchRequest courseSearchRequest, HttpServletRequest request);

    public BaseResponse<CourseData2DTO> listallCourse(CourseListAllRequest courseListAllRequest, HttpServletRequest request);
    public BaseResponse<CourseListDTO> listCourse(CourseListRequest courseListRequest,HttpServletRequest request);
    public BaseResponse<CourseListforTeacherDTO> listforteacher(CouseListforTeacherRequest couseListforTeacherRequest,HttpServletRequest request);
    public BaseResponse<CourseaddRequest> addCourse(CourseaddRequest courseaddRequest,HttpServletRequest request);
}
