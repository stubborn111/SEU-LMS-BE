package edu.seu.lms.backend.seulmsbe.Wiki.controller;


import edu.seu.lms.backend.seulmsbe.Wiki.mapper.WikiMapper;
import edu.seu.lms.backend.seulmsbe.Wiki.service.IWikiService;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.discussion.service.IDiscussionService;
import edu.seu.lms.backend.seulmsbe.dto.Wiki.WikiAdminListDTO;
import edu.seu.lms.backend.seulmsbe.dto.Wiki.WikiListDTO;
import edu.seu.lms.backend.seulmsbe.request.*;
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
    @Autowired
    private WikiMapper wikiMapper;

    /**
     * listWiki：获得所有的wiki条目
     * postAnswer：为指定问题提交答案
     * wikiQuestion：创建问题
     * mark：标记已解决
     * adminlist：管理员查看列表
     * wikiDelete：删除一个wiki
     */

    @PostMapping("/list")
    public BaseResponse<WikiListDTO> listWiki(@RequestBody WikiListRequest wikiListRequest, HttpServletRequest request){
        return iWikiService.listWiki(wikiListRequest,request);
    }

    @PostMapping("/answer")
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

    @PostMapping("/question")
    public BaseResponse<String> wikiQuestion(@RequestBody WikiQuestionRequest wikiQuestionRequest,HttpServletRequest request){
        return iWikiService.question(wikiQuestionRequest,request);
    }
    @PostMapping("/delete")
    public BaseResponse<String> wikiDelete(@RequestBody WikiIDRequest wikiIDRequest)
    {
        wikiMapper.deleteById(wikiIDRequest.getWikiID());
        return ResultUtils.success(null);
    }
}
