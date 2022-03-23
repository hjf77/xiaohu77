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
import com.fhs.core.jsonfilter.anno.AutoArray;
import com.fhs.core.jsonfilter.serializer.Str2ArrayValueSerializer;
import com.fhs.core.result.HttpResult;
import com.fhs.module.base.context.UserContext;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ms/student")
public class StudentContorller {

    @Autowired
    private UcenterMsUserService ucenterMsUserService;

    @Autowired
    private CurriculumStudentScoreMapper curriculumStudentScoreMapper;

    @GetMapping("list")
    public List<UcenterMsUserVO> users(){
        LambdaQueryWrapper<UcenterMsUserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UcenterMsUserDO::getType,"student");
        wrapper.in(UcenterMsUserDO::getClassesId,UserContext.getSessionuser().getClassesId().split(","));
        return ucenterMsUserService.selectListMP(wrapper);
    }

    @GetMapping("pager")
    public Pager<UcenterMsUserVO> pager(){
        LambdaQueryWrapper<UcenterMsUserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UcenterMsUserDO::getType,"student");
        wrapper.in(UcenterMsUserDO::getClassesId,UserContext.getSessionuser().getClassesId().split(","));
        List<UcenterMsUserVO> list =  ucenterMsUserService.selectListMP(wrapper);
        Pager<UcenterMsUserVO> pager =  new Pager<>(list.size(),list);
        return pager;
    }

    @GetMapping("apply")
    public HttpResult apply(String id){
        CurriculumStudentScore score = curriculumStudentScoreMapper.selectById(id);
        score.setStatus("已通过");
        curriculumStudentScoreMapper.updateById(score);
        return HttpResult.success();
    }

}
