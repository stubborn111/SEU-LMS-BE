package edu.seu.lms.backend.seulmsbe.syllabus.service;

import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.dto.*;
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

    BaseResponse<String> addsyllabus(SyllabusAddRequest syllabusAddRequest, HttpServletRequest request);

    BaseResponse<String> modifySyllabus(SyllabusModifyRequest syllabusModifyRequest, HttpServletRequest request);
    BaseResponse<String> postText(SyllabusPostTextRequest syllabusPostTextRequest,HttpServletRequest request);
    BaseResponse<HomeWorkIntroDTO> homeworkIntro(SyllabusIDRequest syllabusIDRequest,HttpServletRequest request);

    BaseResponse<FileListDTO> materialUpload(SyllabusIDRequest syllabusIDRequest,HttpServletRequest request);
}
