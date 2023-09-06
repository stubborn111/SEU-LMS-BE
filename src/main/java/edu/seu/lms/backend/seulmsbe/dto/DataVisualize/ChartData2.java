package edu.seu.lms.backend.seulmsbe.dto.DataVisualize;

import lombok.Data;

import java.util.List;
@Data
public class ChartData2 {
    private List<CommonData> dountChartData;//统计每个课程的人数
    private int gaugeChartData;//总出勤率
    private List<CommonData> pieChartData;//总作业等级
    private List<CourseAddenceDTO> lineChartData;//各课程出勤率
    private List<CourseScoreDTO> columnChartData;//各课程作业均分
}
