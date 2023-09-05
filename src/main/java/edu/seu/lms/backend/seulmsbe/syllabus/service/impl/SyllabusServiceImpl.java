package edu.seu.lms.backend.seulmsbe.syllabus.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.assignment.entity.Assignment;
import edu.seu.lms.backend.seulmsbe.assignment.mapper.AssignmentMapper;
import edu.seu.lms.backend.seulmsbe.checkin.entity.Checkin;
import edu.seu.lms.backend.seulmsbe.checkin.mapper.CheckinMapper;
import edu.seu.lms.backend.seulmsbe.checkin.service.ICheckinService;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.discussion.entity.Discussion;
import edu.seu.lms.backend.seulmsbe.dto.*;
import edu.seu.lms.backend.seulmsbe.dto.DataVisualize.Homework;
import edu.seu.lms.backend.seulmsbe.file.entity.File;
import edu.seu.lms.backend.seulmsbe.file.mapper.FileMapper;
import edu.seu.lms.backend.seulmsbe.request.SyllabusCommonRequest;
import edu.seu.lms.backend.seulmsbe.request.SyllabusListHomeworkRequest;
import edu.seu.lms.backend.seulmsbe.request.SyllabusListRequest;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import edu.seu.lms.backend.seulmsbe.syllabus.mapper.SyllabusMapper;
import edu.seu.lms.backend.seulmsbe.syllabus.service.ISyllabusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static edu.seu.lms.backend.seulmsbe.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
@Service
public class SyllabusServiceImpl extends ServiceImpl<SyllabusMapper, Syllabus> implements ISyllabusService {
    @Autowired
    private SyllabusMapper syllabusMapper;
    @Autowired
    private CheckinMapper checkinMapper;
    @Autowired
    private ICheckinService iCheckinService;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public BaseResponse<SyllabusListDTO> listSyllabus(SyllabusListRequest syllabusListRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);

        int pagesize = syllabusListRequest.getPageSize();
        String courseid = syllabusListRequest.getCourseID();
        int curPage = syllabusListRequest.getCurrentPage();

        LambdaUpdateWrapper<Syllabus> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(Syllabus::getCurriculumID,courseid);

        Page<Syllabus> Page = syllabusMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        SyllabusListDTO dto = new SyllabusListDTO();
        dto.setTotalNum((int)Page.getTotal());
//        if(currentUser.getAccess()==1){
//
//        }
//        if(currentUser.getAccess()==2){
//
//        }
        List<Syllabus> tmp = Page.getRecords();
        List<SyllabusDTO> DTO = new ArrayList<>();
        for(Syllabus tt : tmp){
            SyllabusDTO temp = new SyllabusDTO();
            temp.setSyllabusID(tt.getId());
            temp.setTitle(tt.getTitle());
//            String[] material = null;
//            String[] homework = null;
//            if(tt.getMaterials() != null){
//                material = tt.getMaterials().split("##");
//            }
//            if(tt.getAssiments() != null){
//                homework = tt.getAssiments().split("##");
//            }

            temp.setHaveHomework(tt.getAssiments()!=null);
            temp.setHaveMaterial(tt.getMaterials()!=null);
            //temp.setMeterials(material);
            if(currentUser.getAccess()==1){
                LambdaUpdateWrapper<Checkin> queryWrapper = new LambdaUpdateWrapper<>();
                queryWrapper.eq(Checkin::getSyllabusID,tt.getId());
                queryWrapper.eq(Checkin::getStudentID,currentUser.getId());
                Checkin checkintmp = checkinMapper.selectOne(queryWrapper);
                if(tt.getIsCheckedIn()==0) temp.setIsCheckedIn(0);
                else if(tt.getIsCheckedIn()==1 && checkintmp.getIsCheckedIn()==1) temp.setIsCheckedIn(1);
                else if(tt.getIsCheckedIn()==1 && checkintmp.getIsCheckedIn()==0) temp.setIsCheckedIn(2);
                else if(tt.getIsCheckedIn()==2 && checkintmp.getIsCheckedIn()==0) temp.setIsCheckedIn(3);
            }
            else if(currentUser.getAccess()==2){
                temp.setIsCheckedIn(tt.getIsCheckedIn());
            }
           // temp.setCheckedIn(tt.isCheckedIn());
            DTO.add(temp);
        }
        dto.setList(DTO);
        return ResultUtils.success(dto);
    }

    @Override
    public BaseResponse<Integer> checkin(SyllabusCommonRequest syllabusCommonRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String syllabusID = syllabusCommonRequest.getSyllabusID();
        Syllabus temp = syllabusMapper.selectById(syllabusID);
        if(temp.getIsCheckedIn() == 2){
            return ResultUtils.success(0);
        }
        else if(temp.getIsCheckedIn() == 1){
            LambdaUpdateWrapper<Checkin> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(Checkin::getSyllabusID,syllabusID).eq(Checkin::getStudentID,currentUser)
                    .set(Checkin::getIsCheckedIn,1);
            iCheckinService.update(lambdaUpdateWrapper);
            return ResultUtils.success(1);

        }
        else return ResultUtils.success(-1);
    }

    @Override
    public BaseResponse<MaterialListDTO> listMaterial(SyllabusCommonRequest syllabusCommonRequest, HttpServletRequest request) {
        String syllabusID = syllabusCommonRequest.getSyllabusID();
        Syllabus syllabustmp = syllabusMapper.selectById(syllabusID);
        if(syllabustmp.getMaterials()==null || syllabustmp.getMaterials().isEmpty()) return ResultUtils.error(777,"无文件",null);
        else {
            //String[] list = syllabustmp.getMaterials().split("##");
            MaterialListDTO dto = new MaterialListDTO();
            List<MaterialDTO> dtoList = new ArrayList<>();
            List<File> list = fileMapper.selectList(new LambdaUpdateWrapper<File>().eq(File::getSyllabusID,syllabusID));
            for(File tmp:list){
                MaterialDTO materialDTO = new MaterialDTO();
                //File tmp = fileMapper.selectById(t);
                materialDTO.setDescription(tmp.getDescription());
                materialDTO.setName(tmp.getName());
                materialDTO.setTime(tmp.getTime());
                materialDTO.setType(tmp.getType());
                materialDTO.setUrl(tmp.getUrl());
                materialDTO.setStatus(tmp.getStatus());
                dtoList.add(materialDTO);
            }
            dto.setFileList(dtoList);
            return ResultUtils.success(dto);
        }
    }

    @Override
    public BaseResponse<SyllabusHomeworkListDTO> listHomework(SyllabusListHomeworkRequest syllabusListHomeworkRequest, HttpServletRequest request) {
        String syllabusID = syllabusListHomeworkRequest.getSyllabusID();
        int curPage = syllabusListHomeworkRequest.getCurrentPage();
        int pageSize = syllabusListHomeworkRequest.getPageSize();
        LambdaUpdateWrapper<Assignment> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(Assignment::getSyllabusID,syllabusID);
        Page<Assignment> Page = assignmentMapper.selectPage(new Page<>(curPage,pageSize),queryMapper);
        List<Assignment> assignmentList = Page.getRecords();
        List<SyllabusHomeworkDTO> syllabusHomeworkDTOList = new ArrayList<>();
        for(Assignment tmp:assignmentList){
            SyllabusHomeworkDTO temp = new SyllabusHomeworkDTO();
            temp.setFileName(tmp.getName());
            temp.setFileType(tmp.getType());
            temp.setStatus(tmp.getStatus());
            temp.setFileUrl(tmp.getFile());
            User user = userMapper.selectById(tmp.getStudentID());
            temp.setStudentAvatar(user.getAvatarUrl());
            temp.setStudentNickName(user.getNickname());
            syllabusHomeworkDTOList.add(temp);
        }
        SyllabusHomeworkListDTO dto = new SyllabusHomeworkListDTO();
        dto.setTotalNum((int)Page.getTotal());
        dto.setList(syllabusHomeworkDTOList);
        SyllabusHomeworkInfoDTO infoDTO = new SyllabusHomeworkInfoDTO();
        Syllabus syllabustmp = syllabusMapper.selectById(syllabusID);
        if(syllabustmp.getAssiments()==null || syllabustmp.getAssiments().isEmpty()) infoDTO.setHomeworkName(syllabustmp.getAssiments());
        if(!assignmentList.isEmpty()){
            infoDTO.setHomeworkDescription(assignmentList.get(0).getContent());
        }
        infoDTO.setUncommittedNum(assignmentMapper.getStatus0num(syllabusID));
        infoDTO.setToBeCorrectedNum(assignmentMapper.getStatus1num(syllabusID));
        dto.setInfo(infoDTO);
        return ResultUtils.success(dto);
    }
}
