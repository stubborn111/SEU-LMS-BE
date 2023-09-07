package edu.seu.lms.backend.seulmsbe.Wiki.controller;


import edu.seu.lms.backend.seulmsbe.Wiki.service.IWikiService;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.dto.Wiki.WikiAdminListDTO;
import edu.seu.lms.backend.seulmsbe.dto.Wiki.WikiListDTO;
import edu.seu.lms.backend.seulmsbe.request.CoursePageRequest;
import edu.seu.lms.backend.seulmsbe.request.PostAnswerRequest;
import edu.seu.lms.backend.seulmsbe.request.WikiListRequest;
import edu.seu.lms.backend.seulmsbe.request.WikiMarkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/wiki")
public class WikiController {
    @Autowired
    private IWikiService iWikiService;

    @PostMapping("/list")
    public BaseResponse<WikiListDTO> listWiki(@RequestBody WikiListRequest wikiListRequest, HttpServletRequest request){
        return iWikiService.listWiki(wikiListRequest,request);
    }

    @PostMapping("/postAnswer")
    public BaseResponse<String> postAnswer(@RequestBody PostAnswerRequest postAnswerRequest,HttpServletRequest request){
        return iWikiService.postAnswer(postAnswerRequest,request);
    }

    @PostMapping("/mark")
    public BaseResponse<String> mark(@RequestBody WikiMarkRequest wikiMarkRequest,HttpServletRequest request){
        return iWikiService.mark(wikiMarkRequest,request);
    }

    @PostMapping("/admin-list")
    public BaseResponse<WikiAdminListDTO> adminlist(@RequestBody CoursePageRequest coursePageRequest, HttpServletRequest request){
        return iWikiService.adminlist(coursePageRequest,request);
    }
}
