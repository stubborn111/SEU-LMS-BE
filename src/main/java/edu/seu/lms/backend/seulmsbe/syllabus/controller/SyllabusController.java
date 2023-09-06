package edu.seu.lms.backend.seulmsbe.syllabus.controller;


import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ErrorCode;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.dto.MaterialListDTO;
import edu.seu.lms.backend.seulmsbe.dto.SyllabusHomeworkListDTO;
import edu.seu.lms.backend.seulmsbe.dto.SyllabusListDTO;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.syllabus.service.ISyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
}
