package edu.seu.lms.backend.seulmsbe.upload;

import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ErrorCode;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.config.AliyunOSSConfig;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import edu.seu.lms.backend.seulmsbe.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.update;
import static edu.seu.lms.backend.seulmsbe.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private OSS ossClient;
    @Autowired
    private AliyunOSSConfig aliyunOSSConfig;
    @PostMapping("/image")
    public BaseResponse<String> uploadimage(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        if (file.isEmpty()) {
            return ResultUtils.error(ErrorCode.FILE_EMPTY);
        }

        String bucketNanme=aliyunOSSConfig.getBucketName();
        String endPoint = aliyunOSSConfig.getEndPoint();
        String accessKeyId = aliyunOSSConfig.getAccessKeyId();
        String accessKeySecret = aliyunOSSConfig.getAccessKeySecret();
        String fileHost = aliyunOSSConfig.getFileHost();
        //返回的Url
        String returnUrl="";
        //审核上传文件是否符合规定格式
//        boolean isLegal=false;
//        for (String type:IMAGE_TYPE){
//            if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(),type)){
//                isLegal=true;
//                break;
//            }
//        }
//        if (!isLegal){
////            如果不正确返回错误状态码
//            return StatusCode.ERROR.getMsg();
//        }
        //获取文件的名称
        String originalFilename = file.getOriginalFilename();
        //截取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
//        最终保存文件名称
        String newFileName= UUID.randomUUID().toString().substring(0,7)+ fileType;
        //构建日期路径  ps ：oss目标文件夹/yyyy/MM/dd/文件名称
        String filePath=new SimpleDateFormat("yyyy/MM/dd").format(new Date());
//        文件上传文件的路径
        String uploadUrl=fileHost+"/"+filePath+"/"+newFileName;
//        获取文件输入流
        InputStream inputStream=null;
        try{
            inputStream=file.getInputStream();

        }catch (IOException e){
            e.printStackTrace();
        }
        //文件上传到阿里云oss
//        ossClient.put
        ossClient.putObject(bucketNanme,uploadUrl,inputStream);//,meta
        returnUrl="http://"+bucketNanme+"."+endPoint+"/"+uploadUrl;
        //User user =(User) request.getSession().getAttribute(USER_LOGIN_STATE);
        //LambdaUpdateWrapper<User> queryWrapper = new LambdaUpdateWrapper<>();
        //queryWrapper.eq(User::getId,user.getId()).set(User::getAvatarUrl,returnUrl);
        //iUserService.update(queryWrapper);
        return ResultUtils.success(returnUrl);
    }
    @PostMapping("/file")
    public BaseResponse<String> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        if (file.isEmpty()) {
            return ResultUtils.error(ErrorCode.FILE_EMPTY);
        }

        String bucketNanme=aliyunOSSConfig.getBucketName();
        String endPoint = aliyunOSSConfig.getEndPoint();
        String accessKeyId = aliyunOSSConfig.getAccessKeyId();
        String accessKeySecret = aliyunOSSConfig.getAccessKeySecret();
        String fileHost = aliyunOSSConfig.getFileHost();
        //返回的Url
        String returnUrl="";
        //审核上传文件是否符合规定格式
//        boolean isLegal=false;
//        for (String type:IMAGE_TYPE){
//            if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(),type)){
//                isLegal=true;
//                break;
//            }
//        }
//        if (!isLegal){
////            如果不正确返回错误状态码
//            return StatusCode.ERROR.getMsg();
//        }
        //获取文件的名称
        String originalFilename = file.getOriginalFilename();
        //截取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
//        最终保存文件名称
        String newFileName= UUID.randomUUID().toString().substring(0,7)+ fileType;
        //构建日期路径  ps ：oss目标文件夹/yyyy/MM/dd文件名称
        String filePath=new SimpleDateFormat("yyyy/MM/dd").format(new Date());
//        文件上传文件的路径
        String uploadUrl=fileHost+"/"+filePath+"/"+newFileName;
//        获取文件输入流
        InputStream inputStream=null;
        try{
            inputStream=file.getInputStream();

        }catch (IOException e){
            e.printStackTrace();
        }
        //文件上传到阿里云oss
//        ossClient.put
        ossClient.putObject(bucketNanme,uploadUrl,inputStream);//,meta
        returnUrl="http://"+bucketNanme+"."+endPoint+"/"+uploadUrl;
//        User user =(User) request.getSession().getAttribute(USER_LOGIN_STATE);
//        LambdaUpdateWrapper<User> queryWrapper = new LambdaUpdateWrapper<>();
//        queryWrapper.eq(User::getId,user.getId()).set(User::getAvatarUrl,returnUrl);
//        iUserService.update(queryWrapper);
        return ResultUtils.success(returnUrl);
    }
    @PostMapping("/test")
    public BaseResponse<String> test(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        String bucketNanme=aliyunOSSConfig.getBucketName();
        String endPoint = aliyunOSSConfig.getEndPoint();
        String accessKeyId = aliyunOSSConfig.getAccessKeyId();
        String accessKeySecret = aliyunOSSConfig.getAccessKeySecret();
        String fileHost = aliyunOSSConfig.getFileHost();
        //返回的Url
        String returnUrl="";
        //审核上传文件是否符合规定格式
//        boolean isLegal=false;
//        for (String type:IMAGE_TYPE){
//            if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(),type)){
//                isLegal=true;
//                break;
//            }
//        }
//        if (!isLegal){
////            如果不正确返回错误状态码
//            return StatusCode.ERROR.getMsg();
//        }
        //获取文件的名称
        String originalFilename = file.getOriginalFilename();
        //截取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
//        最终保存文件名称
        String newFileName= UUID.randomUUID().toString()+ fileType;
        //构建日期路径  ps ：oss目标文件夹/yyyy/MM/dd文件名称
        String filePath=new SimpleDateFormat("yyyy/MM/dd").format(new Date());
//        文件上传文件的路径
        String uploadUrl=fileHost+"/"+filePath+"/"+newFileName;
//        获取文件输入流
        InputStream inputStream=null;
        try{
            inputStream=file.getInputStream();

        }catch (IOException e){
            e.printStackTrace();
        }
        //文件上传到阿里云oss
//        ossClient.put
        ossClient.putObject(bucketNanme,uploadUrl,inputStream);//,meta
        returnUrl="http://"+bucketNanme+"."+endPoint+"/"+uploadUrl;
        return ResultUtils.success(null);

    }
}
