package edu.seu.lms.backend.seulmsbe.Wiki.service;

import edu.seu.lms.backend.seulmsbe.Wiki.entity.Wiki;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.dto.Wiki.WikiAdminListDTO;
import edu.seu.lms.backend.seulmsbe.dto.Wiki.WikiListDTO;
import edu.seu.lms.backend.seulmsbe.request.*;
import edu.seu.lms.backend.seulmsbe.dto.Wiki.WikiAdminListDTO;
import edu.seu.lms.backend.seulmsbe.dto.Wiki.WikiListDTO;
import edu.seu.lms.backend.seulmsbe.request.CoursePageRequest;
import edu.seu.lms.backend.seulmsbe.request.PostAnswerRequest;
import edu.seu.lms.backend.seulmsbe.request.WikiListRequest;
import edu.seu.lms.backend.seulmsbe.request.WikiMarkRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
public interface IWikiService extends IService<Wiki> {
    public BaseResponse<WikiListDTO> listWiki(WikiListRequest wikiListRequest, HttpServletRequest request);

    BaseResponse<String> postAnswer(PostAnswerRequest postAnswerRequest, HttpServletRequest request);

    BaseResponse<String> mark(WikiMarkRequest wikiMarkRequest, HttpServletRequest request);

    BaseResponse<WikiAdminListDTO> adminlist(CoursePageRequest coursePageRequest, HttpServletRequest request);

    BaseResponse<String> question(WikiQuestionRequest wikiQuestionRequest, HttpServletRequest request);
}
