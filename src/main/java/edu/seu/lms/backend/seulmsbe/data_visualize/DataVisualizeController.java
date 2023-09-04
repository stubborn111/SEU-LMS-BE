package edu.seu.lms.backend.seulmsbe.data_visualize;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.lms.backend.seulmsbe.assignment.entity.Assignment;
import edu.seu.lms.backend.seulmsbe.assignment.mapper.AssignmentMapper;
import edu.seu.lms.backend.seulmsbe.checkin.mapper.CheckinMapper;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.dto.DataVisualize.*;
import edu.seu.lms.backend.seulmsbe.request.TeacherChartRequest;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import edu.seu.lms.backend.seulmsbe.syllabus.mapper.SyllabusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/data-visualize")
public class DataVisualizeController {
    @Autowired
    private SyllabusMapper syllabusMapper;
    @Autowired
    private CheckinMapper checkinMapper;
    @Autowired
    private AssignmentMapper assignmentMapper;
    @PostMapping("/teacher-chart")
    public BaseResponse<TeacherChartDTO> teacherChart(@RequestBody TeacherChartRequest teacherChartRequest, HttpServletRequest request){
        String courseID = teacherChartRequest.getCourseID();
        System.out.println(courseID);
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
            if (!assignment.isEmpty()) homework.setTask(assignment.get(0).getDescribe());
            if(assignmentMapper.getAvgScore(tt.getId())!=null) homework.setScore(assignmentMapper.getAvgScore(tt.getId()));
            else homework.setScore(0);
            homeworkList.add(homework);
        }
        chartDatatmp.setGroupedColumnChartData(homeworkList);

        dto.setChartData(chartDatatmp);
        return ResultUtils.success(dto);
    }

    @GetMapping("/general-overview")
    public BaseResponse<String> overview(HttpServletRequest request){

        return ResultUtils.success(null);
    }
}
