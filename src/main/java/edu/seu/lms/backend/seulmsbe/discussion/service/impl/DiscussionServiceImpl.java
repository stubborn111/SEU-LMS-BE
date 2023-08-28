package edu.seu.lms.backend.seulmsbe.discussion.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.discussion.entity.Discussion;
import edu.seu.lms.backend.seulmsbe.discussion.mapper.DiscussionMapper;
import edu.seu.lms.backend.seulmsbe.discussion.service.IDiscussionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.dto.DiscussionListDTO;
import edu.seu.lms.backend.seulmsbe.request.DiscussionListRequest;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static edu.seu.lms.backend.seulmsbe.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author szh
 * @since 2023-08-25
 */
@Service
public class DiscussionServiceImpl extends ServiceImpl<DiscussionMapper, Discussion> implements IDiscussionService {

    @Autowired
    private DiscussionMapper discussionMapper;
    @Override
    public BaseResponse<?> listall(DiscussionListRequest discussionListRequest, HttpServletRequest request) {

        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);

        int pagesize = discussionListRequest.getPageSize();
        String courseid = discussionListRequest.getCourseID();
        int curPage = discussionListRequest.getCurrentPage();

        LambdaUpdateWrapper<Discussion> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(Discussion::getFromUserID,currentUser.getId());
        queryMapper.eq(Discussion::getCurriculumID,courseid);

        Page<Discussion> discussionPage = discussionMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        DiscussionListDTO dto = new DiscussionListDTO(discussionPage,discussionPage.getTotal());

        return ResultUtils.success(dto);
    }
}
