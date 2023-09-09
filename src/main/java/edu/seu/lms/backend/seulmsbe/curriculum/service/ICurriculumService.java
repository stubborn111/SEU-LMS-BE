package edu.seu.lms.backend.seulmsbe.curriculum.service;

import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.lms.backend.seulmsbe.dto.Course.*;
import edu.seu.lms.backend.seulmsbe.request.*;
import org.springframework.web.bind.annotation.RequestBody;

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
    public BaseResponse<CourseNameDTO> getCourseName(CourseGetIntoRequest courseGetIntoRequest, HttpServletRequest request);
    public BaseResponse<Curriculum> modifyCourse(CourseModifyRequest courseModifyRequest,HttpServletRequest request);
    public BaseResponse<CourseTeacherDTO> getTeacherInfo(CourseGetIntoRequest courseGetIntoRequest,HttpServletRequest request);

    public BaseResponse<CourseInfoDTO>  getInto(CourseGetIntoRequest courseGetIntoRequest,HttpServletRequest request);
    public BaseResponse<CourseAdminDTO> adminList(CourseAdminRequest courseAdminRequest,HttpServletRequest request);

    BaseResponse<CourseListStudentDTO> listStudent(CourseListStudent2Request courseListStudentRequest, HttpServletRequest request);
    BaseResponse<String> sendNotice(SendNoticeRequest sendNoticeRequest,HttpServletRequest request);

}
