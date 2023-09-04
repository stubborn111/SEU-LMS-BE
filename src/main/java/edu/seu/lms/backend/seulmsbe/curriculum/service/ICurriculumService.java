package edu.seu.lms.backend.seulmsbe.curriculum.service;

import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.lms.backend.seulmsbe.dto.CourseListDTO;
import edu.seu.lms.backend.seulmsbe.dto.CourseListforTeacherDTO;
import edu.seu.lms.backend.seulmsbe.dto.CourseSearchDTO;
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
    public BaseResponse<CourseSearchDTO> studentsearchCourse(CourseSearchRequest courseSearchRequest, HttpServletRequest request);

    public BaseResponse<CourseListDTO> studentListCourse(CoursePageRequest coursePageRequest, HttpServletRequest request);
    //public BaseResponse<CourseListDTO> listCourse(CourseListRequest courseListRequest,HttpServletRequest request);
    public BaseResponse<CourseListforTeacherDTO> listforteacher(CouseListforTeacherRequest couseListforTeacherRequest, HttpServletRequest request);
    public BaseResponse<CourseaddRequest> addCourse(CourseaddRequest courseaddRequest,HttpServletRequest request);
    public  BaseResponse<CourseListDTO> teacehrList(CourseListRequest courseListRequest,HttpServletRequest request);

    public BaseResponse<CourseListDescriptionDTO> listDescription(HttpServletRequest request);
    public BaseResponse<CourseListDTO> teacherSearch(CourseSearchRequest courseSearchRequest,HttpServletRequest request);
}
