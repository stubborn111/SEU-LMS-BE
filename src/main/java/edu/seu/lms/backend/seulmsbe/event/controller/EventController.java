package edu.seu.lms.backend.seulmsbe.event.controller;


import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.dto.EventDataDTO;
import edu.seu.lms.backend.seulmsbe.dto.EventDataListDTO;
import edu.seu.lms.backend.seulmsbe.event.entity.Event;
import edu.seu.lms.backend.seulmsbe.event.mapper.EventMapper;
import edu.seu.lms.backend.seulmsbe.request.UserIDRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
@RestController
@RequestMapping("/calendar")
public class EventController {
    @Autowired
    EventMapper eventMapper;

    /**
     * 列出一位学生的所有ID
     * @param userIDRequest
     * @param request
     * @return
     */
    @PostMapping("list-events")
    BaseResponse<EventDataListDTO> listEvents(@RequestBody UserIDRequest userIDRequest, HttpServletRequest request)
    {
        List<Event> eventList=eventMapper.selectEventByUserID(userIDRequest.getUserID());
        List<EventDataDTO> dto=new ArrayList<>();
        EventDataListDTO DTO=new EventDataListDTO();
        for(Event tt:eventList)
        {
            EventDataDTO tem=new EventDataDTO();
            tem.setType(tt.getType());
            tem.setContent(tt.getContent());
            Instant instant = Timestamp.valueOf(tt.getDate().atTime(LocalTime.MIDNIGHT)).toInstant();
            Date date = Date.from(instant);
            tem.setDate(date);
            dto.add(tem);
        }
        DTO.setEventData(dto);
        return ResultUtils.success(DTO);
    }
}
