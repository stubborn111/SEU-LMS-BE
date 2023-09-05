package edu.seu.lms.backend.seulmsbe.dto.DataVisualize;

import lombok.Data;

import java.util.List;

@Data
public class GeneralChartData {
    private List<CommonData> userDountChartData;
    private List<CommonData> pieChartData;
    private List<CommonData> discussionDountChartData;
    private List<AttendanceNum> lineChartData;
    private List<HomeworkNum> columnChartData;
}
