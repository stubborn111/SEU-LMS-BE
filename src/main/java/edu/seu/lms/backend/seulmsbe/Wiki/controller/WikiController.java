package edu.seu.lms.backend.seulmsbe.Wiki.controller;


import edu.seu.lms.backend.seulmsbe.Wiki.service.IWikiService;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.discussion.service.IDiscussionService;
import edu.seu.lms.backend.seulmsbe.dto.WikiListDTO;
import edu.seu.lms.backend.seulmsbe.request.WikiListRequest;
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
}
