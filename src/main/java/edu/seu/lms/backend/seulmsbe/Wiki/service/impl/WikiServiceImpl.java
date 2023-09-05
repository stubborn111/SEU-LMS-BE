package edu.seu.lms.backend.seulmsbe.Wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.Wiki.entity.Wiki;
import edu.seu.lms.backend.seulmsbe.Wiki.mapper.WikiMapper;
import edu.seu.lms.backend.seulmsbe.Wiki.service.IWikiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.dto.WikiAdminListDTO;
import edu.seu.lms.backend.seulmsbe.dto.WikiListDTO;
import edu.seu.lms.backend.seulmsbe.request.CoursePageRequest;
import edu.seu.lms.backend.seulmsbe.request.PostAnswerRequest;
import edu.seu.lms.backend.seulmsbe.request.WikiListRequest;
import edu.seu.lms.backend.seulmsbe.request.WikiMarkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
@Service
public class WikiServiceImpl extends ServiceImpl<WikiMapper, Wiki> implements IWikiService {
    @Autowired
    private WikiMapper wikiMapper;
    @Override
    public BaseResponse<WikiListDTO> listWiki(WikiListRequest wikiListRequest, HttpServletRequest request) {

        int pagesize = wikiListRequest.getPageSize();
        int curPage = wikiListRequest.getCurrentPage();


        Page<Wiki> WikiPage = wikiMapper.selectPage(new Page<>(curPage,pagesize),null);

        WikiListDTO dto = new WikiListDTO();
        dto.setTotalNum((int)WikiPage.getTotal());
        dto.setList(WikiPage.getRecords());
        return ResultUtils.success(dto);
    }

    @Override
    public BaseResponse<String> postAnswer(PostAnswerRequest postAnswerRequest, HttpServletRequest request) {
        LambdaUpdateWrapper<Wiki> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Wiki::getWikiID,postAnswerRequest.getWikiID())
                .set(Wiki::getAnswer,postAnswerRequest.getAnswer());
        update(lambdaUpdateWrapper);
        return ResultUtils.success(null);
    }

    @Override
    public BaseResponse<String> mark(WikiMarkRequest wikiMarkRequest, HttpServletRequest request) {
        LambdaUpdateWrapper<Wiki> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Wiki::getWikiID,wikiMarkRequest.getWikiID())
                .set(Wiki::getIsSolved,wikiMarkRequest.getIsSolved());
        update(lambdaUpdateWrapper);
        return ResultUtils.success(null);
    }

    @Override
    public BaseResponse<WikiAdminListDTO> adminlist(CoursePageRequest coursePageRequest, HttpServletRequest request) {
        return null;
    }
}
