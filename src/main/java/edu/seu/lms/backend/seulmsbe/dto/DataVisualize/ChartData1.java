package edu.seu.lms.backend.seulmsbe.dto.DataVisualize;

import lombok.Data;

import java.util.List;

@Data
public class ChartData1 {
    private List<CommonData> dountChartData;//统计每个课程的人数
    private float gaugeChartData;//总出勤率
    private List<CommonData> pieChartData;//总作业等级
    private List<CommonData> lineChartData;//各课程出勤率
    private List<CommonData> columnChartData;//各课程作业均分
}
