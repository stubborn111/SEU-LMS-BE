package edu.seu.lms.backend.seulmsbe.syllabus.service;

import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.dto.MaterialListDTO;
import edu.seu.lms.backend.seulmsbe.dto.SyllabusHomeworkListDTO;
import edu.seu.lms.backend.seulmsbe.dto.SyllabusListDTO;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
public interface ISyllabusService extends IService<Syllabus> {
    public BaseResponse<SyllabusListDTO> listSyllabus(SyllabusListRequest syllabusListRequest, HttpServletRequest request);

    BaseResponse<Integer> checkin(CheckInRequest checkInRequest, HttpServletRequest request);

    BaseResponse<MaterialListDTO> listMaterial(SyllabusCommonRequest syllabusCommonRequest, HttpServletRequest request);

    BaseResponse<SyllabusHomeworkListDTO> listHomework(SyllabusListHomeworkRequest syllabusListHomeworkRequest, HttpServletRequest request);

    BaseResponse<String> haveCheckIn(HaveCheckInRequest haveCheckInRequest, HttpServletRequest request);

    BaseResponse<String> checkinStop(SyllabusCommonRequest syllabusCommonRequest, HttpServletRequest request);

    BaseResponse<String> homeworkPublish(HomeworkPublishRequest homeworkPublishRequest, HttpServletRequest request);
}
