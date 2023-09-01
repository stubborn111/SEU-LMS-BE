package edu.seu.lms.backend.seulmsbe;

import edu.seu.lms.backend.seulmsbe.Student_Curriculum.entity.StudentCurriculum;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.mapper.StudentCurriculumMapper;
import edu.seu.lms.backend.seulmsbe.Wiki.entity.Wiki;
import edu.seu.lms.backend.seulmsbe.Wiki.mapper.WikiMapper;
import edu.seu.lms.backend.seulmsbe.curriculum.entity.Curriculum;
import edu.seu.lms.backend.seulmsbe.curriculum.mapper.CurriculumMapper;
import edu.seu.lms.backend.seulmsbe.curriculum.service.ICurriculumService;
import edu.seu.lms.backend.seulmsbe.curriculum.service.impl.CurriculumServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class SeuLmsBeApplicationTests {
    @Autowired
    private CurriculumMapper curriculumMapper;

    @Autowired
    private StudentCurriculumMapper studentCurriculumMapper;

    @Autowired
    private WikiMapper wikiMapper;
    @Test
    void contextLoads() {
    }

    @Test
    public void insertcourse(){
        ArrayList<String> courses = new ArrayList<String>();
        // 添加20个课程名称到ArrayList中
        courses.add("计算机科学导论");
        courses.add("数据结构与算法");
        courses.add("操作系统原理");
        courses.add("数据库系统");
        courses.add("软件工程");
        courses.add("人工智能");
        courses.add("机器学习");
        courses.add("深度学习");
        courses.add("计算机网络");
        courses.add("网络安全");
        courses.add("编译原理");
        courses.add("计算机图形学");
        courses.add("数字逻辑与计算机组成");
        courses.add("微处理器原理与应用");
        courses.add("嵌入式系统设计");
        courses.add("移动应用开发");
        courses.add("云计算与大数据");
        courses.add("物联网技术与应用");
        courses.add("区块链原理与实践");
        courses.add("计算机科学前沿话题");
        Integer i = 2;
        for(String tmp:courses){
            Curriculum temp = new Curriculum();
            temp.setId(i.toString());
            temp.setName(tmp);
            temp.setTeacherID("222");
            curriculumMapper.insertCurriculum(temp);

            StudentCurriculum sc = new StudentCurriculum();
            sc.setId(i.toString());
            sc.setCurriculumID(i.toString());
            sc.setStudentID("111");
            studentCurriculumMapper.insert(sc);

            i = i+1;
        }
    }

    @Test
    public void insertWiki(){
        for(Integer i = 1;i<=20;i++){
            Wiki temp = new Wiki();
            temp.setQuestion("question"+i.toString());
            temp.setAnswer("answer"+i.toString());
            wikiMapper.insert(temp);
        }
    }
}
