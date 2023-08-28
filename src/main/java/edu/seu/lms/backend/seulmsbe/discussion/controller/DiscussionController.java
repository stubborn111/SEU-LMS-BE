package edu.seu.lms.backend.seulmsbe.discussion.controller;


import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.discussion.service.IDiscussionService;
import edu.seu.lms.backend.seulmsbe.dto.DiscussionDTO;
import edu.seu.lms.backend.seulmsbe.dto.DiscussionListDTO;
import edu.seu.lms.backend.seulmsbe.request.DiscussionListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
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
@RequestMapping("/discussion")
public class DiscussionController {
    @Autowired
    private IDiscussionService discussionService;
    @GetMapping("/list")
    public BaseResponse<?> listdiscussion(@RequestBody DiscussionListRequest discussionListRequest, HttpServletRequest request){
        return discussionService.listall(discussionListRequest,request);
    }
}
