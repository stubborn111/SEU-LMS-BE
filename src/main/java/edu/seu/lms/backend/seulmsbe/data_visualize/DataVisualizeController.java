package edu.seu.lms.backend.seulmsbe.data_visualize;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.mapper.StudentCurriculumMapper;
import edu.seu.lms.backend.seulmsbe.assignment.entity.Assignment;
import edu.seu.lms.backend.seulmsbe.assignment.mapper.AssignmentMapper;
import edu.seu.lms.backend.seulmsbe.checkin.mapper.CheckinMapper;
import edu.seu.lms.backend.seulmsbe.checkin.mapper.DateCountDao;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.discussion.mapper.DiscussionMapper;
import edu.seu.lms.backend.seulmsbe.dto.DataVisualize.*;
import edu.seu.lms.backend.seulmsbe.request.TeacherChartRequest;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import edu.seu.lms.backend.seulmsbe.syllabus.mapper.SyllabusMapper;
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
        tmp.setType("已签到");
        if(checkinMapper.getCheckedNum((latest.getId()))!=null){
            tmp.setValue(checkinMapper.getCheckedNum(latest.getId()));
        }else tmp.setValue(0);
        List<CommonData> tmp2 = new ArrayList<>();
        tmp2.add(tmp);
        tmp4.setType("未签到");
        tmp4.setValue(checkinMapper.getNotCheckedNum(latest.getId()));
        tmp2.add(tmp4);
        chartDatatmp.setDountChartData(tmp2);

        //第二张统计图（上节课的作业情况）
        List<CommonData> tmp3 = new ArrayList<>();
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

        //第三个元素（上次作业平均分）
        chartDatatmp.setGaugeChartData(assignmentMapper.getAvgScore(latest.getId()));

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
            if (!assignment.isEmpty()) homework.setTask(assignment.get(0).getContent());
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
}
