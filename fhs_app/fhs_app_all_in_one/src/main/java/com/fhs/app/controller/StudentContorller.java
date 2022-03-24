package com.fhs.app.controller;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.app.mapper.CurriculumStudentScoreMapper;
import com.fhs.app.po.CurriculumStudentScore;
import com.fhs.basics.dox.UcenterMsUserDO;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.config.EConfig;
import com.fhs.core.jsonfilter.anno.AutoArray;
import com.fhs.core.jsonfilter.serializer.Str2ArrayValueSerializer;
import com.fhs.core.result.HttpResult;
import com.fhs.core.trans.service.impl.TransService;
import com.fhs.module.base.context.UserContext;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ms/student")
public class StudentContorller {

    @Autowired
    private UcenterMsUserService ucenterMsUserService;

    @Autowired
    private CurriculumStudentScoreMapper curriculumStudentScoreMapper;

    @Autowired
    private TransService transService;

    /**
     * 老师的学生
     * @return
     */
    @GetMapping("list")
    public List<UcenterMsUserVO> teachersStudent(){
        LambdaQueryWrapper<UcenterMsUserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UcenterMsUserDO::getType,"student");
        wrapper.in(UcenterMsUserDO::getClassesId,UserContext.getSessionuser().getClassesId().split(","));
        return ucenterMsUserService.selectListMP(wrapper);
    }

    /**
     * 老师的学生分页
     * @return
     */
    @GetMapping("pager")
    public Pager<UcenterMsUserVO> pager(){
        LambdaQueryWrapper<UcenterMsUserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UcenterMsUserDO::getType,"student");
        wrapper.in(UcenterMsUserDO::getClassesId,UserContext.getSessionuser().getClassesId().split(","));
        List<UcenterMsUserVO> list =  ucenterMsUserService.selectListMP(wrapper);
        transService.transMore(list);
        Pager<UcenterMsUserVO> pager =  new Pager<>(list.size(),list);
        return pager;
    }

    /**
     * 审批
     * @return
     */
    @GetMapping("apply")
    public HttpResult apply(String id){
        CurriculumStudentScore score = curriculumStudentScoreMapper.selectById(id);
        score.setStatus("已通过");
        curriculumStudentScoreMapper.updateById(score);
        return HttpResult.success();
    }

    /**
     * 选修课
     * @return
     */
    @GetMapping("electiveCourses")
    public List<Map<String,String>> electiveCourses(String studentId){
        String electiveCourses = ucenterMsUserService.selectById(studentId).getElectiveCourses();
        electiveCourses = electiveCourses.replace("，",",");
        String[] electiveCoursesArray = electiveCourses.split(",");
        List<Map<String,String>> result = new ArrayList<>();
        Map<String,String> temp = null;
        for (String name : electiveCoursesArray) {
            temp = new HashMap<>();
            temp.put("name",name);
            result.add(temp);
        }
        return result;
    }

    /**
     * 跳转到选修课成绩列表
     * @return
     */
    @GetMapping("myElectiveCoursesScore")
    public void send2List(HttpServletResponse response){
        try {
            response.sendRedirect(EConfig.getPathPropertiesValue("basePath")
                    + "/ms/pagex/school_elective_courses_list.jsp?userId=" + UserContext.getSessionuser().getUserId());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
