package edu.seu.lms.backend.seulmsbe.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.entity.StudentCurriculum;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.mapper.StudentCurriculumMapper;
import edu.seu.lms.backend.seulmsbe.assignment.mapper.AssignmentMapper;
import edu.seu.lms.backend.seulmsbe.checkin.mapper.CheckinMapper;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import edu.seu.lms.backend.seulmsbe.curriculum.mapper.CurriculumMapper;
import edu.seu.lms.backend.seulmsbe.curriculum.service.ICurriculumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.discussion.mapper.DiscussionMapper;
import edu.seu.lms.backend.seulmsbe.dto.Course.*;
import edu.seu.lms.backend.seulmsbe.event.mapper.EventMapper;
import edu.seu.lms.backend.seulmsbe.file.mapper.FileMapper;
import edu.seu.lms.backend.seulmsbe.message.entity.Message;
import edu.seu.lms.backend.seulmsbe.message.mapper.MessageMapper;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import edu.seu.lms.backend.seulmsbe.syllabus.mapper.SyllabusMapper;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import edu.seu.lms.backend.seulmsbe.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
public class CurriculumServiceImpl extends ServiceImpl<CurriculumMapper, Curriculum> implements ICurriculumService {
    @Autowired
    private CurriculumMapper curriculumMapper;

    @Autowired
    private IUserService userService;
    @Autowired
    StudentCurriculumMapper studentCurriculumMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    private SyllabusMapper syllabusMapper;
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private CheckinMapper checkinMapper;
    @Autowired
    private DiscussionMapper discussionMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private EventMapper eventMapper;
    public BaseResponse<CourseSearchDTO> studentsearchCourse(CourseSearchRequest courseSearchRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        //取出数据
        int pagesize = courseSearchRequest.getPageSize();
        String keyword = courseSearchRequest.getKeyword();
        int curPage = courseSearchRequest.getCurrentPage();
        //构建查询体
        LambdaUpdateWrapper<Curriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.like(Curriculum::getName,keyword);
        List<Curriculum> tmp = curriculumMapper.studentSearch(keyword,currentUser.getId(),pagesize*(curPage-1),pagesize);

        CourseSearchDTO dto = new CourseSearchDTO();
        //dto.setTotalNum((int)Page.getTotal());

        //List<Curriculum> tmp = Page.getRecords();
        List<CourseData2DTO> DTO = new ArrayList<>();

        for(Curriculum tt : tmp){
            CourseData2DTO temp = new CourseData2DTO();
            temp.setCourseID(tt.getId());
            temp.setCourseName(tt.getName());
            temp.setDescription(tt.getDescription());
            temp.setImgUrl(tt.getImgUrl());
            temp.setSemester(tt.getSemester());
            User teacher = userService.getuser(tt.getTeacherID());
            temp.setTeacherName(teacher.getNickname());
            temp.setTeacherAvatar(teacher.getAvatarUrl());
            DTO.add(temp);
        }
        dto.setList(DTO);
        if (curriculumMapper.getnum(keyword,currentUser.getId())!=null) dto.setTotalNum(curriculumMapper.getnum(keyword,currentUser.getId()));
        else dto.setTotalNum(0);
        return ResultUtils.success(dto);
    }




    @Override
    //分页返回用户的课程信息
    public BaseResponse<CourseListDTO> studentListCourse(CoursePageRequest coursePageRequest, HttpServletRequest request) {
        //取出数据
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        int pagesize = coursePageRequest.getPageSize();
        String userid = currentUser.getId();
        int curPage = coursePageRequest.getCurrentPage();
        //构建查询体
        LambdaUpdateWrapper<StudentCurriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(StudentCurriculum::getStudentID,userid);

        Page<StudentCurriculum> Page = studentCurriculumMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        CourseListDTO dto = new CourseListDTO();
        dto.setTotalNum((int)Page.getTotal());

        List<StudentCurriculum> tmp = Page.getRecords();
        List<CourseData2DTO> DTO = new ArrayList<>();
        for(StudentCurriculum studentCurriculum:tmp)
        {
            CourseData2DTO temp=new CourseData2DTO();
            String stuId=studentCurriculum.getCurriculumID();
            temp.setCourseID(studentCurriculum.getCurriculumID());
            Curriculum curriculum=curriculumMapper.selectById(stuId);
            System.out.println(curriculum);
            if(curriculum!=null) {
                temp.setDescription(curriculum.getDescription());
                temp.setImgUrl(curriculum.getImgUrl());
                temp.setSemester(curriculum.getSemester());
                temp.setCourseName(curriculum.getName());

                User teacher = userService.getuser(curriculum.getTeacherID());
                temp.setTeacherName(teacher.getNickname());
                temp.setTeacherAvatar(teacher.getAvatarUrl());
                DTO.add(temp);
            }
        }
        dto.setList(DTO);
        return ResultUtils.success(dto);
    }

    @Override
    //通过教师的id找到他所有的课程，返回课程id和name
    public BaseResponse<CourseListforTeacherDTO> listforteacher(CouseListforTeacherRequest couseListforTeacherRequest, HttpServletRequest request) {
        String teacherId=couseListforTeacherRequest.getTeacherID();
        LambdaUpdateWrapper<Curriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(Curriculum::getTeacherID,teacherId);
        List<Curriculum> curriculumList=curriculumMapper.selectList(queryMapper);
        List<CourseData3DTO> courseData3DTOList=new ArrayList<>();
        CourseListforTeacherDTO courseListforTeacherDTO=new CourseListforTeacherDTO();
        for(Curriculum curriculum:curriculumList)
        {
            CourseData3DTO courseData3DTO=new CourseData3DTO();
            courseData3DTO.setCourseID(curriculum.getId());
            courseData3DTO.setCourseName(curriculum.getName());
            courseData3DTOList.add(courseData3DTO);
        }
        courseListforTeacherDTO.setTabList(courseData3DTOList);
        return ResultUtils.success(courseListforTeacherDTO);
    }

    @Override
    public BaseResponse<CourseaddRequest> addCourse(CourseaddRequest courseaddRequest, HttpServletRequest request) {
        Curriculum curriculum=new Curriculum();
        curriculum.setId(UUID.randomUUID().toString().substring(0,7));
        curriculum.setName(courseaddRequest.getCourseName());
        curriculum.setSemester(courseaddRequest.getSemester());
        curriculum.setImgUrl(courseaddRequest.getImgUrl());
        String description=courseaddRequest.getUnit()+"##"+courseaddRequest.getCredit()+"##"+
                courseaddRequest.getTeachingTime()+"##"+courseaddRequest.getTeachingLocation()+"##"+
                courseaddRequest.getTeachingMethod()+"##"+courseaddRequest.getIntroduction();
        curriculum.setDescription(description);
        String teacherName=courseaddRequest.getTeacherName();
        LambdaUpdateWrapper<User> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(User::getId,teacherName);
        User teacher=userMapper.selectOne(queryMapper);
        curriculum.setTeacherID(teacher.getId());
        curriculumMapper.insertCurriculum(curriculum);
        return ResultUtils.success(null);
    }

    @Override
    public BaseResponse<CourseListDTO> teacehrList(CourseListRequest courseListRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        int pagesize =courseListRequest.getPageSize();
        String userid = courseListRequest.getUserID();
        int curPage = courseListRequest.getCurrentPage();
        //构建查询体
        LambdaUpdateWrapper<Curriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(Curriculum::getTeacherID,currentUser.getId());

        Page<Curriculum> Page = curriculumMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        List<Curriculum> curriculumList=Page.getRecords();
        CourseListDTO DTO=new CourseListDTO();
        List<CourseData2DTO> dto=new ArrayList<>();
        DTO.setTotalNum((int)Page.getTotal());
        for(Curriculum tt:curriculumList)
        {
            CourseData2DTO courseData2DTO=new CourseData2DTO();
            courseData2DTO.setCourseID(tt.getId());
            courseData2DTO.setCourseName(tt.getName());
            courseData2DTO.setDescription(tt.getDescription());
            courseData2DTO.setImgUrl(tt.getImgUrl());
            courseData2DTO.setSemester(tt.getSemester());
            User teacher=userService.getuser(tt.getTeacherID());
            courseData2DTO.setTeacherName(teacher.getNickname());
            courseData2DTO.setTeacherAvatar(teacher.getAvatarUrl());
            dto.add(courseData2DTO);
        }
        DTO.setList(dto);
        return ResultUtils.success(DTO);
    }

    @Override
    public BaseResponse<CourseListDescriptionDTO> listDescription(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        int access=currentUser.getAccess();
        CourseListDescriptionDTO DTO=new CourseListDescriptionDTO();
        List<CourseData3DTO> dto=new ArrayList<>();
        LambdaUpdateWrapper<Curriculum> queryWrapper=new LambdaUpdateWrapper<>();
        List<Curriculum> curriculumList=new ArrayList<>();
        if(access==0)//管理员
        {
            curriculumList=curriculumMapper.findAll();
        }
        else if (access==2)//老师
            {
            queryWrapper.eq(Curriculum::getTeacherID,currentUser.getId());
            curriculumList=curriculumMapper.selectList(queryWrapper);
        }
        for(Curriculum tt:curriculumList)
        {
            CourseData3DTO tem=new CourseData3DTO();
            tem.setCourseName(tt.getName());
            tem.setCourseID(tt.getId());
            dto.add(tem);
        }
        DTO.setDescriptionList(dto);
        return ResultUtils.success(DTO);
    }

    @Override
    public BaseResponse<CourseListDTO> teacherSearch(CourseSearchRequest courseSearchRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String userId=currentUser.getId();
        String keword=courseSearchRequest.getKeyword();
        int pagesize =courseSearchRequest.getPageSize();
        int curPage = courseSearchRequest.getCurrentPage();
        LambdaUpdateWrapper<Curriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(Curriculum::getTeacherID,userId);
        queryMapper.like(Curriculum::getName,keword);
        Page<Curriculum> Page=curriculumMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        CourseListDTO DTO=new CourseListDTO();
        DTO.setTotalNum((int)Page.getTotal());
        List<Curriculum> curriculumList=Page.getRecords();
        List<CourseData2DTO> dto=new ArrayList<>();
        for(Curriculum tt:curriculumList)
        {
            CourseData2DTO courseData2DTO=new CourseData2DTO();
            courseData2DTO.setCourseID(tt.getId());
            courseData2DTO.setCourseName(tt.getName());
            courseData2DTO.setDescription(tt.getDescription());
            courseData2DTO.setImgUrl(tt.getImgUrl());
            courseData2DTO.setSemester(tt.getSemester());
            User teacher=userService.getuser(tt.getTeacherID());
            courseData2DTO.setTeacherName(teacher.getNickname());
            courseData2DTO.setTeacherAvatar(teacher.getAvatarUrl());
            dto.add(courseData2DTO);
        }
        DTO.setList(dto);
        return ResultUtils.success(DTO);
    }

    @Override
    public BaseResponse<CourseNameDTO> getCourseName(CourseGetIntoRequest courseGetIntoRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String userId=currentUser.getId();
        LambdaUpdateWrapper<StudentCurriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(StudentCurriculum::getStudentID,userId);
        List<StudentCurriculum> list=studentCurriculumMapper.selectList(queryMapper);
        for(StudentCurriculum tt:list)
        {
            if(tt.getCurriculumID().equals(courseGetIntoRequest.getCourseID()))
            {
                CourseNameDTO DTO=new CourseNameDTO();
                DTO.setCourseName(curriculumMapper.getCurriculumById(tt.getCurriculumID()).getName());
                return ResultUtils.success(DTO);
            }
        }
        return ResultUtils.success(null);
    }

    @Override
    public BaseResponse<Curriculum> modifyCourse(CourseModifyRequest courseModifyRequest, HttpServletRequest request) {
        LambdaUpdateWrapper<Curriculum> queryMapper = new LambdaUpdateWrapper<>();
        User teacher=userMapper.selectUserByName(courseModifyRequest.getTeacherName());
        System.out.println(teacher);
        System.out.println(courseModifyRequest);
        queryMapper.eq(Curriculum::getId,courseModifyRequest.getCourseID());
        queryMapper.set(Curriculum::getName,courseModifyRequest.getCourseName())
                .set(Curriculum::getSemester,courseModifyRequest.getSemester())
                .set(Curriculum::getImgUrl,courseModifyRequest.getImgUrl())
                .set(Curriculum::getTeacherID,teacher.getId());
        update(queryMapper);
        return ResultUtils.success(null);
    }

    @Override
    public BaseResponse<CourseTeacherDTO> getTeacherInfo(CourseGetIntoRequest courseGetIntoRequest, HttpServletRequest request) {
        Curriculum curriculum=curriculumMapper.getCurriculumById(courseGetIntoRequest.getCourseID());
        String teacherID=curriculum.getTeacherID();
        User user=userService.getuser(teacherID);
        CourseTeacherDTO DTO=new CourseTeacherDTO();
        DTO.setTeacherEmail(user.getEmail());
        DTO.setTeacherPhone(user.getPhone());
        return ResultUtils.success(DTO);
    }

    @Override
    public BaseResponse<CourseInfoDTO> getInto(CourseGetIntoRequest courseGetIntoRequest, HttpServletRequest request) {
        String courseID=courseGetIntoRequest.getCourseID();
        Curriculum curriculum=curriculumMapper.getCurriculumById(courseID);
        CourseGetinfoDTO DTO=new CourseGetinfoDTO();
        CourseDescriptionDTO dto=new CourseDescriptionDTO();
        DTO.setCourseName(curriculum.getName());
        DTO.setSemester(curriculum.getSemester());
        DTO.setImgUrl(curriculum.getImgUrl());
        User teacehr=userService.getuser(curriculum.getTeacherID());
        DTO.setTeacherAvatar(teacehr.getAvatarUrl());
        DTO.setTeacherEmail(teacehr.getEmail());
        DTO.setTeacherName(teacehr.getNickname());
        DTO.setTeacherPhone(teacehr.getPhone());
        String[] tem=new String[6];
        if(curriculum.getDescription()==null)
        {
            tem=null;
        }else {
            tem=curriculum.getDescription().split("##");
            if(tem.length == 6){
                dto.setUnit(tem[0]);
                dto.setCredit(tem[1]);
                dto.setTeachingTime(tem[2]);
                dto.setTeachingLocation(tem[3]);
                dto.setTeachingMethod(tem[4]);
                dto.setIntroduction(tem[5]);
                DTO.setDescription(dto);
            }
            else {
                dto.setUnit("暂无");
                dto.setCredit("暂无");
                dto.setTeachingTime("暂无");
                dto.setTeachingLocation("暂无");
                dto.setTeachingMethod("暂无");
                dto.setIntroduction("暂无");
                DTO.setDescription(dto);
            }
        }


        CourseInfoDTO courseInfoDTO = new CourseInfoDTO();
        courseInfoDTO.setCourseData(DTO);
        return ResultUtils.success(courseInfoDTO);
    }

    @Override
    public BaseResponse<CourseAdminDTO> adminList(CourseAdminRequest courseAdminRequest, HttpServletRequest request) {
        String keyword=courseAdminRequest.getCourseName();
        String teachername=courseAdminRequest.getTeacherName();
        int pagesize =courseAdminRequest.getPageSize();
        int curPage = courseAdminRequest.getCurrentPage();
        CourseAdminDTO DTO=new CourseAdminDTO();
        System.out.println(keyword);
        System.out.println(teachername);
        List<Curriculum> curriculumList;
        if(keyword==null&&teachername==null) {
            curriculumList = curriculumMapper.SearchCourse("","",pagesize*(curPage-1),pagesize);
            DTO.setTotalNum(curriculumMapper.getNumAdmin0());
        }else {
            System.out.println(111);
            if (keyword==null&&teachername!=null)
            {
                curriculumList = curriculumMapper.SearchByNameAndTeacher("",teachername,pagesize*(curPage-1),pagesize);
                DTO.setTotalNum(curriculumMapper.getnumAdmin("",teachername));
            }else if(keyword!=null&&teachername==null){
                curriculumList = curriculumMapper.SearchByNameAndTeacher(keyword,"",pagesize*(curPage-1),pagesize);
                DTO.setTotalNum(curriculumMapper.getnumAdmin(keyword,""));
            }else {
                curriculumList = curriculumMapper.SearchByNameAndTeacher(keyword,teachername,pagesize*(curPage-1),pagesize);
                DTO.setTotalNum(curriculumMapper.getnumAdmin(keyword,teachername));
            }
        }


        List<CourseAdminDataDTO> dto1=new ArrayList<>();
        for(Curriculum tt:curriculumList)
        {
            CourseAdminDataDTO dto=new CourseAdminDataDTO();
            User teacher=userService.getuser(tt.getTeacherID());
            dto.setKey(tt.getId());
            String[] tem=new String[6];
            if(tt.getDescription()==null)
            {
                tem=null;
                dto.setDescription(null);
            }else {
                tem=tt.getDescription().split("##");
                if(tem.length == 6){
                    dto.setDescription(tem[3]);
                }

            }

            dto.setSemester(tt.getSemester());
            dto.setCourseName(tt.getName());
            if(teacher.getAvatarUrl()!=null) dto.setTeacherAvatar(teacher.getAvatarUrl());
            dto.setCourseID(tt.getId());
            dto.setTeacherName(teacher.getNickname());
            dto.setImgUrl(tt.getImgUrl());
            dto1.add(dto);
        }
        DTO.setList(dto1);
        return ResultUtils.success(DTO);
    }

    @Override
    public BaseResponse<CourseListStudentDTO> listStudent(CourseListStudent2Request courseListStudentRequest, HttpServletRequest request) {
        int curPage = courseListStudentRequest.getCurrentPage();
        int pagesize = courseListStudentRequest.getPageSize();
        String keyword = courseListStudentRequest.getNickName();
        String courseID = courseListStudentRequest.getCourseID();
        int num;
        List<User> userList;
        if(keyword==null){
            num = curriculumMapper.getListStudentNum("",courseID);
            userList = curriculumMapper.listStudent("",courseID,(curPage-1)*pagesize,pagesize);
        }
        else {
            num = curriculumMapper.getListStudentNum(keyword,courseID);
            userList = curriculumMapper.listStudent(keyword,courseID,(curPage-1)*pagesize,pagesize);
        }
        CourseListStudentDTO dto = new CourseListStudentDTO();
        dto.setTotalNum(num);
        List<CourseStudentDTO> courseStudentDTOList =new ArrayList<>();
        for(User tmp:userList){
            CourseStudentDTO temp = new CourseStudentDTO();
            temp.setAvatarUrl(tmp.getAvatarUrl());
            temp.setId(tmp.getId());
            temp.setName(tmp.getNickname());
            temp.setPhone(tmp.getPhone());
            temp.setEmail(tmp.getEmail());
            courseStudentDTOList.add(temp);
        }
        dto.setList(courseStudentDTOList);
        return ResultUtils.success(dto);
    }

    @Override
    public BaseResponse<String> sendNotice(SendNoticeRequest sendNoticeRequest, HttpServletRequest request) {
        String courseID=sendNoticeRequest.getData().getCourseID();
        String content=sendNoticeRequest.getData().getAnswer();
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String fromUserID=currentUser.getId();
        LambdaUpdateWrapper<StudentCurriculum> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(StudentCurriculum::getCurriculumID,courseID);
        List<StudentCurriculum> list=studentCurriculumMapper.selectList(queryMapper);
        for (StudentCurriculum tt:list)
        {
            Message message=new Message();
            message.setId(UUID.randomUUID().toString().substring(0,7));
            message.setContent(content);
            message.setFromUserID(fromUserID);
            message.setToUserID(tt.getStudentID());
            messageMapper.insertMessage(message);
        }
        User teacher=userMapper.selectById(curriculumMapper.selectById(courseID).getTeacherID());
        Message message=new Message();
        message.setId(UUID.randomUUID().toString().substring(0,7));
        message.setContent(content);
        message.setFromUserID(fromUserID);
        message.setToUserID(teacher.getId());
        messageMapper.insertMessage(message);
        return ResultUtils.success(null);
    }

    @Override
    public void deleteCourseByID(String courseID) {
        List<Syllabus> syllabusList=syllabusMapper.getSyllabusByCurriculumID(courseID);
        for (Syllabus tt:syllabusList)
        {
            fileMapper.deleteBySyllabusID(tt.getId());
            assignmentMapper.deleteBySyllabusID(tt.getId());
            checkinMapper.deleteBySyllabusID(tt.getId());
            eventMapper.deleteBySyllabusID(tt.getId());
        }
        curriculumMapper.deleteById(courseID);
        studentCurriculumMapper.deleteByCourseID(courseID);
        syllabusMapper.deleteByCourseID(courseID);
        discussionMapper.deleteByCurriculumID(courseID);
    }

}
