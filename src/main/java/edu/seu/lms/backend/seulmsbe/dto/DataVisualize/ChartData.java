package edu.seu.lms.backend.seulmsbe.dto.DataVisualize;

import lombok.Data;

import java.util.List;

@Data
public class ChartData {
    private List<CommonData> dountChartData;
    private List<CommonData> pieChartData;
    private int gaugeChartData;
    private List<CourseAttendance> lineChartData;
    private List<Homework> groupedColumnChartData;

}
