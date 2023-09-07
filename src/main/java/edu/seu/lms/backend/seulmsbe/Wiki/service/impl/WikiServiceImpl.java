package edu.seu.lms.backend.seulmsbe.Wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.Wiki.entity.Wiki;
import edu.seu.lms.backend.seulmsbe.Wiki.mapper.WikiMapper;
import edu.seu.lms.backend.seulmsbe.Wiki.service.IWikiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.dto.Wiki.WikiAdminListDTO;
import edu.seu.lms.backend.seulmsbe.dto.Wiki.WikiDTO;
import edu.seu.lms.backend.seulmsbe.dto.Wiki.WikiListDTO;
import edu.seu.lms.backend.seulmsbe.request.CoursePageRequest;
import edu.seu.lms.backend.seulmsbe.request.PostAnswerRequest;
import edu.seu.lms.backend.seulmsbe.request.WikiListRequest;
import edu.seu.lms.backend.seulmsbe.request.WikiMarkRequest;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import edu.seu.lms.backend.seulmsbe.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private UserMapper userMapper;
    @Override
    public BaseResponse<WikiListDTO> listWiki(WikiListRequest wikiListRequest, HttpServletRequest request) {

        int pagesize = wikiListRequest.getPageSize();
        int curPage = wikiListRequest.getCurrentPage();


        Page<Wiki> WikiPage = wikiMapper.selectPage(new Page<>(curPage,pagesize),null);
        List<Wiki> wikiList = wikiMapper.getList((curPage-1)*pagesize,pagesize);
        WikiListDTO dto = new WikiListDTO();
        dto.setTotalNum((int)WikiPage.getTotal());
        dto.setList(wikiList);
        return ResultUtils.success(dto);
    }

    @Override
    public BaseResponse<String> postAnswer(PostAnswerRequest postAnswerRequest, HttpServletRequest request) {
        LambdaUpdateWrapper<Wiki> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Wiki::getWikiID,postAnswerRequest.getWikiID())
                .set(Wiki::getAnswer,postAnswerRequest.getAnswer())
                .set(Wiki::getIsSolved,true)
                .set(Wiki::getTime, LocalDateTime.now());
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
        WikiAdminListDTO dto = new WikiAdminListDTO();
        int pageSize = coursePageRequest.getPageSize();
        int curPage = coursePageRequest.getCurrentPage();
        Page<Wiki> Page = wikiMapper.selectPage(new Page<>(curPage,pageSize),null);
        //List<Wiki> wikiList = Page.getRecords();
        List<Wiki> wikiList = wikiMapper.getList((curPage-1)*pageSize,pageSize);
        dto.setTotalNum((int) Page.getTotal());
        List<WikiDTO> wikiDTOList = new ArrayList<>();
        for(Wiki tmp:wikiList){
            WikiDTO temp = new WikiDTO();
            temp.setWikiID(tmp.getWikiID());
            temp.setTime(tmp.getTime().toString().replace("T"," "));
            temp.setAnswer(tmp.getAnswer());
            temp.setIsSolved(tmp.getIsSolved());
            temp.setQuestion(tmp.getQuestion());
            User user = userMapper.selectById(tmp.getFromUserID());
            temp.setFromUserAvatar(user.getAvatarUrl());
            temp.setFromUserName(user.getNickname());
            temp.setFromUserAccess(user.getAccess()==1?"student":"teacher");
            wikiDTOList.add(temp);
        }
        dto.setList(wikiDTOList);
        return ResultUtils.success(dto);
    }
}
