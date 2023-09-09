package edu.seu.lms.backend.seulmsbe.data_visualize;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.api.R;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.mapper.StudentCurriculumMapper;
import edu.seu.lms.backend.seulmsbe.assignment.entity.Assignment;
import edu.seu.lms.backend.seulmsbe.assignment.mapper.AssignmentMapper;
import edu.seu.lms.backend.seulmsbe.checkin.mapper.CheckinMapper;
import edu.seu.lms.backend.seulmsbe.checkin.mapper.DateCountDao;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import edu.seu.lms.backend.seulmsbe.curriculum.mapper.CurriculumMapper;
import edu.seu.lms.backend.seulmsbe.discussion.mapper.DiscussionMapper;
import edu.seu.lms.backend.seulmsbe.dto.DataVisualize.*;
import edu.seu.lms.backend.seulmsbe.request.CourseNameRequest;
import edu.seu.lms.backend.seulmsbe.request.TeacherChartRequest;
import edu.seu.lms.backend.seulmsbe.request.TeacherIDRequest;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import edu.seu.lms.backend.seulmsbe.syllabus.mapper.SyllabusMapper;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.LocalTime.now;

@RestController
@RequestMapping("/data-visualize")
public class DataVisualizeController {
    @Autowired
    private SyllabusMapper syllabusMapper;
    @Autowired
    private CheckinMapper checkinMapper;
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StudentCurriculumMapper studentCurriculumMapper;
    @Autowired
    private DiscussionMapper discussionMapper;
    @Autowired
    private CurriculumMapper curriculumMapper;
    @PostMapping("/teacher-chart")
    public BaseResponse<TeacherChartDTO> teacherChart(@RequestBody TeacherChartRequest teacherChartRequest, HttpServletRequest request){
        String courseID = teacherChartRequest.getCourseID();
        //System.out.println(courseID);
        TeacherChartDTO dto = new TeacherChartDTO();
        ChartData chartDatatmp = new ChartData();
        Syllabus latest = syllabusMapper.getlatest(courseID);

        //第一个统计图（上节课的签到人数）
        CommonData tmp = new CommonData();
        CommonData tmp1 = new CommonData();
        CommonData tmp5 = new CommonData();
        CommonData tmp6 = new CommonData();
        CommonData tmp4 = new CommonData();
        List<CommonData> tmp2 = new ArrayList<>();
        if(latest==null)
        {
            CommonData bug=new CommonData();
            bug.setType("未签到");
            bug.setValue(0);
            tmp2.add(bug);
            CommonData bug0=new CommonData();
            bug0.setType("已签到");
            bug0.setValue(0);
            tmp2.add(bug0);
            chartDatatmp.setDountChartData(tmp2);
        }else {
            tmp.setType("已签到");
            if(checkinMapper.getCheckedNum((latest.getId()))!=null){
                tmp.setValue(checkinMapper.getCheckedNum(latest.getId()));
            }else tmp.setValue(0);

            tmp2.add(tmp);
            tmp4.setType("未签到");
            tmp4.setValue(checkinMapper.getNotCheckedNum(latest.getId()));
            tmp2.add(tmp4);
            chartDatatmp.setDountChartData(tmp2);
        }


        //第二张统计图（上节课的作业情况）
        List<CommonData> tmp3 = new ArrayList<>();
        if(latest==null)
        {
            CommonData bug=new CommonData();
            bug.setType("无作业");
            tmp3.add(bug);
            chartDatatmp.setPieChartData(tmp3);
        }else {
            tmp5.setType("已批改");
            tmp5.setValue(assignmentMapper.getStatus2num(latest.getId()));
            tmp3.add(tmp5);
            tmp6.setType("待批改");
            tmp6.setValue(assignmentMapper.getStatus1num(latest.getId()));
            tmp3.add(tmp6);
            tmp1.setType("未提交");
            tmp1.setValue(assignmentMapper.getStatus0num(latest.getId()));
            tmp3.add(tmp1);
            chartDatatmp.setPieChartData(tmp3);
        }


        //第三个元素（上次作业平均分）
        if(latest==null) chartDatatmp.setGaugeChartData(0);
        else {
            if(assignmentMapper.getAvgScore(latest.getId())==null) chartDatatmp.setGaugeChartData(0);
            else chartDatatmp.setGaugeChartData(assignmentMapper.getAvgScore(latest.getId()));
        }

        //第四个元素（历史到课率）
        LambdaUpdateWrapper<Syllabus> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Syllabus::getCurriculumID,courseID);
        List<Syllabus> syllabusList = syllabusMapper.selectList(lambdaUpdateWrapper);
        List<CourseAttendance> courseAttendanceList = new ArrayList<>();
        for(Syllabus tt:syllabusList){
            CourseAttendance courseAttendance = new CourseAttendance();
            courseAttendance.setChapter(tt.getTitle());
            double t1 = 0;
            double t2 = 0;
            if(checkinMapper.getCheckedNum((tt.getId()))!=null){
                t1 = checkinMapper.getCheckedNum(tt.getId());
            }else t1 = 0;
            if(checkinMapper.getNotCheckedNum((tt.getId()))!=null){
                t2 = checkinMapper.getNotCheckedNum(tt.getId());
            }else t2 = 0;
            double rate = 0;
            if(t1 !=0 || t2!=0)  rate = (t1 / (t1+t2))*100;
            courseAttendance.setAttendanceRate(rate);
            courseAttendanceList.add(courseAttendance);
        }
        chartDatatmp.setLineChartData(courseAttendanceList);

        //第五个元素（历史作业均分）
        List<Homework> homeworkList = new ArrayList<>();
        for(Syllabus tt:syllabusList){
            Homework homework = new Homework();
            homework.setChapter(tt.getTitle());
//            LambdaUpdateWrapper<Assignment> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
//            lambdaUpdateWrapper1.eq(Assignment::getSyllabusID,tt.getId());
            List<Assignment> assignment = assignmentMapper.getlist(tt.getId());
            if (!assignment.isEmpty())
            {
                Syllabus s=syllabusMapper.selectById(assignment.get(0).getSyllabusID());
                homework.setTask(s.getAssignmentContent());
            }
            if(assignmentMapper.getAvgScore(tt.getId())!=null) homework.setScore(assignmentMapper.getAvgScore(tt.getId()));
            else homework.setScore(0);
            homeworkList.add(homework);
        }
        chartDatatmp.setGroupedColumnChartData(homeworkList);

        dto.setChartData(chartDatatmp);
        return ResultUtils.success(dto);
    }

    @GetMapping("/general-overview")
    public BaseResponse<OverviewDTO> overview(HttpServletRequest request){
        OverviewDTO dto = new OverviewDTO();
        GeneralChartData generalChartData = new GeneralChartData();

        //第一张图
        List<CommonData> userDountChartDatatmp = new ArrayList<>();
        CommonData tmp1 = new CommonData();
        tmp1.setValue(userMapper.getStatus1Num()==null?0: userMapper.getStatus1Num());
        tmp1.setType("学生");
        userDountChartDatatmp.add(tmp1);
        CommonData tmp2 = new CommonData();
        tmp2.setValue(userMapper.getStatus2Num()==null?0: userMapper.getStatus2Num());
        tmp2.setType("教师");
        userDountChartDatatmp.add(tmp2);
        CommonData tmp3 = new CommonData();
        tmp3.setValue(userMapper.getStatus0Num()==null?0: userMapper.getStatus0Num());
        tmp3.setType("管理员");
        userDountChartDatatmp.add(tmp3);
        generalChartData.setUserDountChartData(userDountChartDatatmp);

        //第二张图
        List<CommonData> pieChartDatatmp = new ArrayList<>();
        CommonData tmp4 = new CommonData();
        tmp4.setValue(studentCurriculumMapper.get0_20Num()==null?0:studentCurriculumMapper.get0_20Num());
        tmp4.setType("0-20人");
        pieChartDatatmp.add(tmp4);
        CommonData tmp5 = new CommonData();
        tmp5.setValue(studentCurriculumMapper.get0_20Num()==null?0:studentCurriculumMapper.get20_40Num());
        tmp5.setType("20-40人");
        pieChartDatatmp.add(tmp5);
        CommonData tmp6 = new CommonData();
        tmp6.setValue(studentCurriculumMapper.get0_20Num()==null?0:studentCurriculumMapper.getOver40Num());
        tmp6.setType("大于40人");
        pieChartDatatmp.add(tmp6);
        generalChartData.setPieChartData(pieChartDatatmp);

        //第三张图
        List<CommonData> discussionDountChartDatatmp = new ArrayList<>();
        CommonData tmp7 = new CommonData();
        tmp7.setType("主贴");
        tmp7.setValue(discussionMapper.getDiscussionNum());
        discussionDountChartDatatmp.add(tmp7);
        CommonData tmp8 = new CommonData();
        tmp8.setType("回复");
        tmp8.setValue(discussionMapper.getReplyNum());
        discussionDountChartDatatmp.add(tmp8);
        generalChartData.setDiscussionDountChartData(discussionDountChartDatatmp);

        //第四张图
        List<AttendanceNum> attendanceNums = new ArrayList<>();
        AttendanceNum attendanceNum1 = new AttendanceNum();
//        DateCountDao dateCountDao1 = checkinMapper.getCheckIntodayNum();
//        if(dateCountDao1!=null){
//            attendanceNum1.setDate(dateCountDao1.getCheckin_date().toString());
//            attendanceNum1.setAttendance(dateCountDao1.getTotal_checkins());
//        }
//        else{
//            Date now1 = new Date();
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            attendanceNum1.setDate(simpleDateFormat.format(now1));
//            attendanceNum1.setAttendance(0);
//        }
        Date now1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        attendanceNum1.setDate(simpleDateFormat.format(now1));
        attendanceNum1.setAttendance(checkinMapper.getCheckIntodayNum());
        attendanceNums.add(attendanceNum1);

        AttendanceNum attendanceNum2 = new AttendanceNum();
//        DateCountDao dateCountDao2 = checkinMapper.getCheckInYesterdayNum();
//        if(dateCountDao2!=null){
//            attendanceNum2.setDate(dateCountDao2.getCheckin_date().toString());
//            attendanceNum2.setAttendance(dateCountDao2.getTotal_checkins());
//        }
//        else{
//            Date now2 = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
//            //now2.minusDays(1);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            attendanceNum2.setDate(simpleDateFormat.format(now2));
//            attendanceNum2.setAttendance(0);
//        }
        Date now2 = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
        //now2.minusDays(1);
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        attendanceNum2.setDate(simpleDateFormat.format(now2));
        attendanceNum2.setAttendance(checkinMapper.getCheckInYesterdayNum());
        attendanceNums.add(attendanceNum2);

        AttendanceNum attendanceNum3 = new AttendanceNum();
//        DateCountDao dateCountDao3 = checkinMapper.getCheckIntwodayNum();
//        if(dateCountDao3!=null){
//            attendanceNum3.setDate(dateCountDao3.getCheckin_date().toString());
//            attendanceNum3.setAttendance(dateCountDao3.getTotal_checkins());
//        }
//        else{
//            Date now3 = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 2);
//            //now2.minusDays(1);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            attendanceNum3.setDate(simpleDateFormat.format(now3));
//            attendanceNum3.setAttendance(0);
//        }
        Date now3 = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 2);
        //now2.minusDays(1);
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        attendanceNum3.setDate(simpleDateFormat.format(now3));
        attendanceNum3.setAttendance(checkinMapper.getCheckIntwodayNum());
        attendanceNums.add(attendanceNum3);

        generalChartData.setLineChartData(attendanceNums);

        //第五张
        List<HomeworkNum> homeworkNumList = new ArrayList<>();
        HomeworkNum homeworkNumtmp = new HomeworkNum();
        Date today = new Date();
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        homeworkNumtmp.setDate(simpleDateFormat.format(today));
        homeworkNumtmp.setNum(assignmentMapper.getToday());
        homeworkNumList.add(homeworkNumtmp);

        HomeworkNum homeworkNumtmp2 = new HomeworkNum();
        //LocalDate nowtime = LocalDate.from(now());
        Date nowtime = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
        //nowtime.minusDays(1);

        homeworkNumtmp2.setDate(simpleDateFormat.format(nowtime));
        homeworkNumtmp2.setNum(assignmentMapper.getYesterday());
        homeworkNumList.add(homeworkNumtmp2);

        HomeworkNum homeworkNumtmp3 = new HomeworkNum();
        //LocalDate nowtime2 = LocalDate.from(now());
        //nowtime.minusDays(2);
        Date nowtime3 = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 2);
        homeworkNumtmp3.setDate(simpleDateFormat.format(nowtime3));
        homeworkNumtmp3.setNum(assignmentMapper.getTwoday());
        homeworkNumList.add(homeworkNumtmp3);

        generalChartData.setColumnChartData(homeworkNumList);

        dto.setChartData(generalChartData);
        return ResultUtils.success(dto);
    }
    @PostMapping("teacher-statistics")
    public BaseResponse<TeacherStatisticsDTO> teacherStatistics(@RequestBody TeacherIDRequest teacherIDRequest,HttpServletRequest request)
    {
        String id=teacherIDRequest.getTeacherID();
        TeacherStatisticsDTO DTO=new TeacherStatisticsDTO();
        ChartData2 dto=new ChartData2();
        //第一个图表
        List<CommonData> chartData1=new ArrayList<>();
        List<Curriculum> curriculumList=curriculumMapper.selectCurriculumByteacher(id);
        for(Curriculum tt:curriculumList)
        {
            CommonData tem=new CommonData();
            tem.setType(tt.getName());
            if(studentCurriculumMapper.getNumofCourse(tt.getId())==0) tem.setValue(0);
            else tem.setValue(studentCurriculumMapper.getNumofCourse(tt.getId()));
            chartData1.add(tem);
        }
        dto.setDountChartData(chartData1);
        //第二个图表
        int checkin0=0;
        int notcheckin0=0;
        for (Curriculum tt:curriculumList)
        {
            List<Syllabus> syllabusList=syllabusMapper.getSyllabusByCourseID(tt.getId());
            for(Syllabus ss:syllabusList)
            {
                checkin0=checkin0+checkinMapper.getCheckinBySylNum(1, ss.getId());
                notcheckin0=notcheckin0+checkinMapper.getCheckinBySylNum(0, ss.getId());
            }
        }
        if(checkin0+notcheckin0==0) dto.setGaugeChartData(0);
        else dto.setGaugeChartData((checkin0*100)/(checkin0+notcheckin0));
        //第三个图表
        List<CommonData> chartData3=new ArrayList<>();
        int[] nums=new int[5];
        for(int i=0;i<5;i++) nums[i]=0;
        for (Curriculum tt:curriculumList)
        {
            List<Integer> list=getNumByCourseID(tt.getId());
            Integer[] lists=list.toArray(new Integer[0]);
            nums[0]=nums[0]+lists[0];
            nums[1]=nums[1]+lists[1];
            nums[2]=nums[2]+lists[2];
            nums[3]=nums[3]+lists[3];
            nums[4]=nums[4]+lists[4];
        }
        CommonData tem8=new CommonData();
        tem8.setType("90-100");
        tem8.setValue(nums[0]);
        chartData3.add(tem8);
        CommonData tem2=new CommonData();
        tem2.setType("80-90");
        tem2.setValue(nums[1]);
        chartData3.add(tem2);
        CommonData tem3=new CommonData();
        tem3.setType("60-80");
        tem3.setValue(nums[2]);
        chartData3.add(tem3);
        CommonData tem4=new CommonData();
        tem4.setType("其他");
        tem4.setValue(nums[3]);
        chartData3.add(tem4);
        CommonData tem5=new CommonData();
        tem5.setType("未提交");
        tem5.setValue(nums[4]);
        chartData3.add(tem5);
        dto.setPieChartData(chartData3);
        //第四个图表
        List<CourseAddenceDTO> chartData4=new ArrayList<>();
        int checkin=0;
        int notcheckin=0;
        for (Curriculum tt:curriculumList)
        {
            List<Syllabus> syllabusList=syllabusMapper.getSyllabusByCourseID(tt.getId());

            CourseAddenceDTO tem1=new CourseAddenceDTO();
            tem1.setCourse(tt.getName());
            checkin=0;
            notcheckin=0;
            for(Syllabus aa:syllabusList)
            {
                checkin=checkin+checkinMapper.getCheckinBySylNum(1, aa.getId());
                notcheckin=notcheckin+checkinMapper.getCheckinBySylNum(0, aa.getId());
            }
            if(checkin+notcheckin==0)
            {
                tem1.setAddendance(100);
            }
            else {
                tem1.setAddendance((checkin*100)/(notcheckin+checkin));
            }
            chartData4.add(tem1);
        }
        dto.setLineChartData(chartData4);
        //第五个表格
        List<CourseScoreDTO> chartData5=new ArrayList<>();
        int score=0;
        int num=0;
        for(Curriculum tt:curriculumList)
        {
            List<Syllabus> syllabusList=syllabusMapper.getSyllabusByCourseID(tt.getId());
            CourseScoreDTO tem1=new CourseScoreDTO();
            tem1.setCourse(tt.getName());
            score=0;
            num=0;
            for(Syllabus ss:syllabusList)
            {
                if(assignmentMapper.getAllScore(ss.getId())!=null)
                {
                    score=score+assignmentMapper.getAllScore(ss.getId());
                    num=num+assignmentMapper.getAssignmentNum(ss.getId());
                }
            }
            if(num==0)
            {
                tem1.setScore(0);
            }else {
                tem1.setScore(score/num);
            }
            chartData5.add(tem1);
        }
        dto.setColumnChartData(chartData5);
        DTO.setChartData(dto);
        return ResultUtils.success(DTO);
    }


    @PostMapping("course-statistics")
    public BaseResponse<CourseStatisticsDTO> courseStatistics(@RequestBody CourseNameRequest courseNameRequest,HttpServletRequest request)
    {
        String name=courseNameRequest.getCourseName();
        CourseStatisticsDTO DTO=new CourseStatisticsDTO();
        ChartData3 dto=new ChartData3();
        List<Curriculum> curriculumList=curriculumMapper.selectCurriculumByName(name);
        //第一个图表
        List<CommonData> chartData1=new ArrayList<>();
        for (Curriculum tt:curriculumList)
        {
            CommonData tem=new CommonData();
            User teacher=userMapper.selectById(tt.getTeacherID());
            tem.setType(teacher.getNickname());
            tem.setValue(studentCurriculumMapper.getCourseNum(tt.getId()));
            chartData1.add(tem);
        }
        dto.setDountChartData(chartData1);
        //第二个图表
        int checkin=0;
        int notcheckin=0;
        for (Curriculum tt:curriculumList)
        {
            List<Syllabus> syllabusList=syllabusMapper.getSyllabusByCourseID(tt.getId());
            for(Syllabus ss:syllabusList)
            {
                checkin=checkin+checkinMapper.getCheckinBySylNum(1, ss.getId());
                notcheckin=notcheckin+checkinMapper.getCheckinBySylNum(0, ss.getId());
            }
        }
        if(checkin+notcheckin==0){
            dto.setGaugeChartData(0);
        }else{
            dto.setGaugeChartData((checkin*100)/(checkin+notcheckin));
        }
        //第三个图表
        List<CommonData> chartData3=new ArrayList<>();
        int[] nums=new int[5];
        for (Curriculum tt:curriculumList)
        {
            List<Integer> list=getNumByCourseID(tt.getId());
            Integer[] lists=list.toArray(new Integer[0]);
            nums[0]=nums[0]+lists[0];
            nums[1]=nums[1]+lists[1];
            nums[2]=nums[2]+lists[2];
            nums[3]=nums[3]+lists[3];
            nums[4]=nums[4]+lists[4];
        }
        CommonData tem1=new CommonData();
        tem1.setType("90-100");
        tem1.setValue(nums[0]);
        chartData3.add(tem1);
        CommonData tem2=new CommonData();
        tem2.setType("80-90");
        tem2.setValue(nums[1]);
        chartData3.add(tem2);
        CommonData tem3=new CommonData();
        tem3.setType("60-80");
        tem3.setValue(nums[2]);
        chartData3.add(tem3);
        CommonData tem4=new CommonData();
        tem4.setType("其他");
        tem4.setValue(nums[3]);
        chartData3.add(tem4);
        CommonData tem5=new CommonData();
        tem5.setType("未提交");
        tem5.setValue(nums[4]);
        chartData3.add(tem5);
        dto.setPieChartData(chartData3);
        //第四个图表
        List<TeacherAttendance> chartdata4=new ArrayList<>();

        for(Curriculum tt:curriculumList)
        {
            TeacherAttendance tem=new TeacherAttendance();
            User teacher=userMapper.selectById(tt.getTeacherID());
            tem.setTeacher(teacher.getNickname());
            List<Syllabus> syllabusList=syllabusMapper.getSyllabusByCourseID(tt.getId());
            int checkin0=0;
            int notcheckin0=0;
            for(Syllabus ss:syllabusList)
            {
                checkin0=checkin0+checkinMapper.getCheckinBySylNum(1, ss.getId());
                notcheckin0=notcheckin0+checkinMapper.getCheckinBySylNum(0, ss.getId());
            }
            if(checkin0+notcheckin0==0){
                tem.setAttendance(0);
            }else{
                tem.setAttendance((checkin0*100)/(checkin0+notcheckin0));
            }
            chartdata4.add(tem);
        }
        dto.setLineChartData(chartdata4);
        //第五个图表
        List<TeacherScore> chartdata5=new ArrayList<>();
        for(Curriculum tt:curriculumList)
        {
            TeacherScore tem=new TeacherScore();
            User teacher=userMapper.selectById(tt.getTeacherID());
            tem.setTeacher(teacher.getNickname());
            List<Syllabus> syllabusList=syllabusMapper.getSyllabusByCourseID(tt.getId());
            int score=0;
            int num=0;
            for (Syllabus ss:syllabusList)
            {
                if(assignmentMapper.getAllScore(ss.getId())!=null)
                {
                    score=score+assignmentMapper.getAllScore(ss.getId());
                    num=num+assignmentMapper.getAssignmentNum(ss.getId());
                }
            }
            if(num==0)
            {
                tem.setScore(0);
            }else {
                tem.setScore(score/num);
            }
            chartdata5.add(tem);
        }
        dto.setColumnChartData(chartdata5);
        DTO.setChartData(dto);
        return ResultUtils.success(DTO);
    }
    List<Integer> getNumByCourseID(String courseID)
    {
        List<Integer> list=new ArrayList<>();
        int[] nums=new int[5];
        List<Syllabus> syllabusList=syllabusMapper.getSyllabusByCourseID(courseID);
        for(int i=0;i<5;i++) nums[i]=0;
        for(Syllabus ss:syllabusList)
        {
            nums[0]=nums[0]+assignmentMapper.getScoreNum(90,100, ss.getId())+assignmentMapper.getScoreOneNum(100,ss.getId());
            nums[1]=nums[1]+assignmentMapper.getScoreNum(80,90, ss.getId());
            nums[2]=nums[2]+assignmentMapper.getScoreNum(60,80, ss.getId());
            nums[3]=nums[3]+assignmentMapper.getScoreNum(0,60, ss.getId());
            nums[4]=nums[4]+assignmentMapper.getNotCheckinNum(ss.getId());
        }
        for(int i:nums) list.add(i);
        return list;
    }
}

