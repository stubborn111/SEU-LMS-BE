package edu.seu.lms.backend.seulmsbe.syllabus.controller;


import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.dto.SyllabusListDTO;
import edu.seu.lms.backend.seulmsbe.request.SyllabusListRequest;
import edu.seu.lms.backend.seulmsbe.syllabus.service.ISyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
@RestController
@RequestMapping("/syllabus")
public class SyllabusController {
    @Autowired
    private ISyllabusService syllabusService;

    @GetMapping("/list")
    public BaseResponse<SyllabusListDTO> listSyllabus(@RequestBody SyllabusListRequest syllabusListRequest, HttpServletRequest request){
        return syllabusService.listSyllabus(syllabusListRequest,request);
    }
}
