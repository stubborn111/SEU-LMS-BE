package edu.seu.lms.backend.seulmsbe.curriculum.service;

import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.lms.backend.seulmsbe.dto.CourseListDTO;
import edu.seu.lms.backend.seulmsbe.dto.CourseListallDTO;
import edu.seu.lms.backend.seulmsbe.dto.CourseSearchDTO;
import edu.seu.lms.backend.seulmsbe.request.CourseListRequest;
import edu.seu.lms.backend.seulmsbe.request.CourseSearchRequest;

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

    public BaseResponse<CourseListallDTO> listallCourse(HttpServletRequest request);
    public BaseResponse<CourseListDTO> listCourse(CourseListRequest courseListRequest,HttpServletRequest request);
}
