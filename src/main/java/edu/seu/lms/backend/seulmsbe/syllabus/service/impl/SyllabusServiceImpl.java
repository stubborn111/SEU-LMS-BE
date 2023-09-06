package edu.seu.lms.backend.seulmsbe.syllabus.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.entity.StudentCurriculum;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.mapper.StudentCurriculumMapper;
import edu.seu.lms.backend.seulmsbe.assignment.entity.Assignment;
import edu.seu.lms.backend.seulmsbe.assignment.mapper.AssignmentMapper;
import edu.seu.lms.backend.seulmsbe.checkin.entity.Checkin;
import edu.seu.lms.backend.seulmsbe.checkin.mapper.CheckinMapper;
import edu.seu.lms.backend.seulmsbe.checkin.service.ICheckinService;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.dto.*;
import edu.seu.lms.backend.seulmsbe.event.entity.Event;
import edu.seu.lms.backend.seulmsbe.event.mapper.EventMapper;
import edu.seu.lms.backend.seulmsbe.file.entity.File;
import edu.seu.lms.backend.seulmsbe.file.mapper.FileMapper;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import edu.seu.lms.backend.seulmsbe.syllabus.mapper.SyllabusMapper;
import edu.seu.lms.backend.seulmsbe.syllabus.service.ISyllabusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @Autowired
    private StudentCurriculumMapper studentCurriculumMapper;
    @Autowired
    private EventMapper eventMapper;
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
            temp.setTime(tt.getTime().toString().replace("T"," "));
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
    public BaseResponse<Integer> checkin(CheckInRequest checkInRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String syllabusID = checkInRequest.getSyllabusID();
        String psw = checkInRequest.getCheckInPsw();
        Syllabus temp = syllabusMapper.selectById(syllabusID);
        if(temp.getIsCheckedIn() == 2){
            return ResultUtils.success(-1);
        }
        else if(temp.getIsCheckedIn() == 1 && psw.equals(temp.getCheckInPsw())){
            LambdaUpdateWrapper<Checkin> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(Checkin::getSyllabusID,syllabusID).eq(Checkin::getStudentID,currentUser.getId())
                    .set(Checkin::getIsCheckedIn,1);
            iCheckinService.update(lambdaUpdateWrapper);
            return ResultUtils.success(1);
        }
        else if(temp.getIsCheckedIn() == 1 && psw != temp.getCheckInPsw()){
            return ResultUtils.success(0);
        }
        else return ResultUtils.success(2);
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

    @Override
    public BaseResponse<String> haveCheckIn(HaveCheckInRequest haveCheckInRequest, HttpServletRequest request) {
        String syllabusID = haveCheckInRequest.getSyllabusID();
        String psw = haveCheckInRequest.getPassword();
        Syllabus syllabus = syllabusMapper.selectById(syllabusID);
        LambdaUpdateWrapper<Syllabus> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Syllabus::getId,syllabusID).set(Syllabus::getCheckInPsw,psw).set(Syllabus::getIsCheckedIn,1);
        update(lambdaUpdateWrapper);
        LambdaUpdateWrapper<StudentCurriculum> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper1.eq(StudentCurriculum::getCurriculumID,syllabus.getCurriculumID());
        List<StudentCurriculum> studentCurriculumList =  studentCurriculumMapper.selectList(lambdaUpdateWrapper1);
        for(StudentCurriculum tmp:studentCurriculumList){
            User user = userMapper.selectById(tmp.getStudentID());
            Checkin checkin = new Checkin();
            checkin.setSyllabusID(syllabusID);
            checkin.setIsCheckedIn(0);
            checkin.setTime(LocalDate.now());
            checkin.setID(UUID.randomUUID().toString().substring(0,7));
            checkin.setStudentID(user.getId());
            checkinMapper.insert(checkin);
        }
        return ResultUtils.success(null);
    }

    @Override
    public BaseResponse<String> checkinStop(SyllabusCommonRequest syllabusCommonRequest, HttpServletRequest request) {
        LambdaUpdateWrapper<Syllabus> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Syllabus::getId,syllabusCommonRequest.getSyllabusID()).set(Syllabus::getIsCheckedIn,2);
        update(lambdaUpdateWrapper);
        return ResultUtils.success(null);
    }

    @Override
    public BaseResponse<String> homeworkPublish(HomeworkPublishRequest homeworkPublishRequest, HttpServletRequest request) {
        String syllabusID = homeworkPublishRequest.getSyllabusID();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(homeworkPublishRequest.getDeadline());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String str = homeworkPublishRequest.getDeadline()+" 23:59:59";
        Date datetime = null;
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            datetime = format2.parse(str);
        } catch (ParseException e) {

        }
        Syllabus syllabus= syllabusMapper.selectById(syllabusID);
        LambdaUpdateWrapper<Syllabus> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Syllabus::getId,syllabusID).set(Syllabus::getAssiments,homeworkPublishRequest.getHomeworkName());
        update(lambdaUpdateWrapper);
        LambdaUpdateWrapper<StudentCurriculum> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper1.eq(StudentCurriculum::getCurriculumID,syllabus.getCurriculumID());
        List<StudentCurriculum> studentCurriculumList = studentCurriculumMapper.selectList(lambdaUpdateWrapper1);
        ZoneId zoneId = ZoneId.systemDefault();
        for(StudentCurriculum sc:studentCurriculumList){
            User user = userMapper.selectById(sc.getStudentID());
            Assignment assignment = new Assignment();
            assignment.setSyllabusID(syllabusID);
            assignment.setID(UUID.randomUUID().toString().substring(0,7));
            assignment.setStudentID(user.getId());
            assignment.setStatus(0);
            assignment.setContent(homeworkPublishRequest.getHomeworkDescription());
            assignment.setTime(datetime.toInstant().atZone(zoneId).toLocalDateTime());
            assignmentMapper.insert(assignment);
            Event event = new Event();
            event.setId(UUID.randomUUID().toString().substring(0,7));
            event.setDate(date.toInstant().atZone(zoneId).toLocalDate());
            event.setContent(homeworkPublishRequest.getHomeworkName());
            event.setType("assignment");
            event.setUserID(user.getId());
            event.setContent(homeworkPublishRequest.getHomeworkName());
            eventMapper.insert(event);
        }
        return ResultUtils.success(null);
    }
}
