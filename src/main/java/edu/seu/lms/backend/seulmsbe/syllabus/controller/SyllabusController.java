package edu.seu.lms.backend.seulmsbe.syllabus.controller;


import edu.seu.lms.backend.seulmsbe.assignment.mapper.AssignmentMapper;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ErrorCode;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.dto.*;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.syllabus.service.ISyllabusService;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
@RequestMapping("/syllabus")
public class SyllabusController {
    @Autowired
    private ISyllabusService syllabusService;
    @Autowired
    private AssignmentMapper assignmentMapper;

    @PostMapping("/list")
    public BaseResponse<SyllabusListDTO> listSyllabus(@RequestBody SyllabusListRequest syllabusListRequest, HttpServletRequest request){
        return syllabusService.listSyllabus(syllabusListRequest,request);
    }

    @PostMapping("/check-in")
    public BaseResponse<Integer> checkin(@RequestBody CheckInRequest checkInRequest, HttpServletRequest request){
        return syllabusService.checkin(checkInRequest,request);
    }
    @PostMapping("/material/list")
    public BaseResponse<MaterialListDTO> listMaterial(@RequestBody SyllabusCommonRequest syllabusCommonRequest, HttpServletRequest request){
        return syllabusService.listMaterial(syllabusCommonRequest,request);
    }

    @PostMapping("/homework/list")
    public BaseResponse<SyllabusHomeworkListDTO> listHomework(@RequestBody SyllabusListHomeworkRequest syllabusListHomeworkRequest,HttpServletRequest request){
        return syllabusService.listHomework(syllabusListHomeworkRequest,request);
    }
    @PostMapping("/checkin/start")
    public BaseResponse<String> haveCheckin(@RequestBody HaveCheckInRequest haveCheckInRequest,HttpServletRequest request){
        return syllabusService.haveCheckIn(haveCheckInRequest,request);
    }

    @PostMapping("/checkin/stop")
    public BaseResponse<String> checkinStop(@RequestBody SyllabusCommonRequest syllabusCommonRequest, HttpServletRequest request){
        return syllabusService.checkinStop(syllabusCommonRequest,request);
    }
    @PostMapping("/homework/publish")
    public BaseResponse<String> homeworkPublish(@RequestBody HomeworkPublishRequest homeworkPublishRequest,HttpServletRequest request){
        return syllabusService.homeworkPublish(homeworkPublishRequest,request);
    }
    @PostMapping("/add")
    public BaseResponse<String> addSyllabus(@RequestBody SyllabusAddRequest syllabusAddRequest,HttpServletRequest request){
        return syllabusService.addsyllabus(syllabusAddRequest,request);
    }
    @PostMapping("/modify")
    public BaseResponse<String> modifySyllabus(@RequestBody SyllabusModifyRequest syllabusModifyRequest,HttpServletRequest request){
        return syllabusService.modifySyllabus(syllabusModifyRequest,request);
    }
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile (@RequestParam("file")MultipartFile file,HttpServletRequest request){
        if (file.isEmpty()) {
            return ResultUtils.error(ErrorCode.FILE_EMPTY);
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        // 上传后的路径
        String filePath = "E:\\暑期实训\\SEU-LMS-BE\\data/";

        // 解决中文问题，liunx下中文路径，图片显示问题
        fileName = UUID.randomUUID() + suffixName;

        // 构建上传路径
        File dest = new File(filePath + fileName);

        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            // 保存文件
            file.transferTo(dest);
            return ResultUtils.success(null);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtils.error(ErrorCode.FILE_EMPTY);
    }
    @PostMapping("homework/post-text")
    BaseResponse<String> postText(@RequestBody SyllabusPostTextRequest syllabusPostTextRequest,HttpServletRequest request){
        return syllabusService.postText(syllabusPostTextRequest,request);
    }
    @PostMapping("homework/post-file")
    BaseResponse<String> postfile(@RequestBody SyllabusPostFileRequest postFileRequest,HttpServletRequest request)
    {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String userID=currentUser.getId();
        String Url=postFileRequest.getHomeworkUrl();
        int index=Url.lastIndexOf(".");
        String type;
        if(index!=-1&&index<Url.length()-1)
        {
            type=Url.substring(index+1);
        }else {
            type=" ";
        }
        assignmentMapper.syllabusPostFile(userID,postFileRequest.getSyllabusID(),postFileRequest.getHomeworkTitle(),Url,type, DateTime.now());
        return ResultUtils.success(null);
    }
    @PostMapping("homework/feedback")
    BaseResponse<String> feedback(@RequestBody SyllabusFeedbackRequest feedbackRequest,HttpServletRequest request)
    {
        assignmentMapper.syllabusFeedback(feedbackRequest.getHomeworkID(),feedbackRequest.getRate()*5,feedbackRequest.getFeedback());
        return ResultUtils.success(null);
    }
    @PostMapping("homework/intro")
    public BaseResponse<HomeWorkIntroDTO> homeworkIntro(@RequestBody SyllabusIDRequest syllabusIDRequest, HttpServletRequest request) {
        return syllabusService.homeworkIntro(syllabusIDRequest,request);
    }
    @PostMapping("material/upload")
    public BaseResponse<FileListDTO> materialUpload(SyllabusIDRequest syllabusIDRequest, HttpServletRequest request) {
        return syllabusService.materialUpload(syllabusIDRequest,request);
    }

}
