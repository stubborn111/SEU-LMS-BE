package edu.seu.lms.backend.seulmsbe.syllabus.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.lms.backend.seulmsbe.common.BaseResponse;
import edu.seu.lms.backend.seulmsbe.common.ResultUtils;
import edu.seu.lms.backend.seulmsbe.dto.SyllabusDTO;
import edu.seu.lms.backend.seulmsbe.dto.SyllabusListDTO;
import edu.seu.lms.backend.seulmsbe.request.SyllabusListRequest;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import edu.seu.lms.backend.seulmsbe.syllabus.mapper.SyllabusMapper;
import edu.seu.lms.backend.seulmsbe.syllabus.service.ISyllabusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.lms.backend.seulmsbe.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
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
public class SyllabusServiceImpl extends ServiceImpl<SyllabusMapper, Syllabus> implements ISyllabusService {
    @Autowired
    private SyllabusMapper syllabusMapper;
    @Override
    public BaseResponse<SyllabusListDTO> listSyllabus(SyllabusListRequest syllabusListRequest, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);

        int pagesize = syllabusListRequest.getPageSize();
        String courseid = syllabusListRequest.getCourseID();
        int curPage = syllabusListRequest.getCurrentPage();

        LambdaUpdateWrapper<Syllabus> queryMapper = new LambdaUpdateWrapper<>();
        queryMapper.eq(Syllabus::getCurriculumID,courseid);

        Page<Syllabus> Page = syllabusMapper.selectPage(new Page<>(curPage,pagesize),queryMapper);
        SyllabusListDTO dto = new SyllabusListDTO();
        dto.setTotalNum((int)Page.getTotal());
        List<Syllabus> tmp = Page.getRecords();
        List<SyllabusDTO> DTO = new ArrayList<>();
        for(Syllabus tt : tmp){
            SyllabusDTO temp = new SyllabusDTO();
            temp.setSyllabusID(tt.getId());
            temp.setTitle(tt.getTitle());
            String[] material = null;
            String[] homework = null;
            if(tt.getMaterials() != null){
                material = tt.getMaterials().split("##");
            }
            if(tt.getAssiments() != null){
                homework = tt.getAssiments().split("##");
            }

            temp.setHomework(homework);
            temp.setMeterials(material);
            temp.setCheckedIn(tt.isCheckedIn());
            DTO.add(temp);
        }
        dto.setList(DTO);
        return ResultUtils.success(dto);
    }
}
