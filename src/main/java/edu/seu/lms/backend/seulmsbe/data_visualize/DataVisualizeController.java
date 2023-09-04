package edu.seu.lms.backend.seulmsbe.data_visualize;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.lms.backend.seulmsbe.assignment.entity.Assignment;
import edu.seu.lms.backend.seulmsbe.assignment.mapper.AssignmentMapper;
import edu.seu.lms.backend.seulmsbe.checkin.mapper.CheckinMapper;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.dto.DataVisualize.*;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import edu.seu.lms.backend.seulmsbe.syllabus.mapper.SyllabusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public BaseResponse<TeacherChartDTO> teacherChart(String courseID, HttpServletRequest request){
        TeacherChartDTO dto = new TeacherChartDTO();
        ChartData chartDatatmp = new ChartData();
        Syllabus latest = syllabusMapper.getlatest(courseID);

        //第一个统计图（上节课的签到人数）
        CommonData tmp = new CommonData();
        tmp.setType("已签到");
        tmp.setValue(checkinMapper.getCheckedNum(latest.getId()));
        List<CommonData> tmp2 = new ArrayList<>();
        tmp2.add(tmp);
        tmp.setType("未签到");
        tmp.setValue(checkinMapper.getNotCheckedNum(latest.getId()));
        tmp2.add(tmp);
        chartDatatmp.setDountChartData(tmp2);

        //第二张统计图（上节课的作业情况）
        List<CommonData> tmp3 = new ArrayList<>();
        tmp.setType("已批改");
        tmp.setValue(assignmentMapper.getStatus2num(latest.getId()));
        tmp3.add(tmp);
        tmp.setType("待批改");
        tmp.setValue(assignmentMapper.getStatus1num(latest.getId()));
        tmp3.add(tmp);
        tmp.setType("未提交");
        tmp.setValue(assignmentMapper.getStatus0num(latest.getId()));
        tmp3.add(tmp);
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
            int t1 = checkinMapper.getCheckedNum(tt.getId());
            int t2 = checkinMapper.getNotCheckedNum(tt.getId());
            double rate = t1 / (t1+t2);
            courseAttendance.setAttendanceRate(rate);
            courseAttendanceList.add(courseAttendance);
        }
        chartDatatmp.setLineChartData(courseAttendanceList);

        //第五个元素（历史作业均分）
        List<Homework> homeworkList = new ArrayList<>();
        for(Syllabus tt:syllabusList){
            Homework homework = new Homework();
            homework.setChapter(tt.getTitle());
            Assignment assignment = assignmentMapper.selectOne(
                    new LambdaUpdateWrapper<Assignment>().eq(Assignment::getSyllabusID,tt.getId()));
            homework.setTask(assignment.getDescribe());
            homework.setScore(assignmentMapper.getAvgScore(tt.getId()));
            homeworkList.add(homework);
        }
        chartDatatmp.setGroupedColumnChartData(homeworkList);

        return ResultUtils.success(dto);
    }

    @GetMapping("/general-overview")
    public BaseResponse<String> overview(HttpServletRequest request){

        return ResultUtils.success(null);
    }
}
