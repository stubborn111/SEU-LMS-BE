package edu.seu.lms.backend.seulmsbe.checkin.mapper;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DateCountDao {
    private LocalDate checkin_date;
    private Integer total_checkins;
}
