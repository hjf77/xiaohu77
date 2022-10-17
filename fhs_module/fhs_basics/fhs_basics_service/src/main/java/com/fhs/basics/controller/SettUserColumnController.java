package com.fhs.basics.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fhs.basics.context.UserContext;
import com.fhs.basics.po.SettUserColumnPO;
import com.fhs.basics.service.SettUserColumnService;
import com.fhs.basics.vo.SettUserColumnVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.result.HttpResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * 用户自定义列信息配置(SettUserColumn)表控制层
 *
 * @author wanglei
 * @since 2022-10-13 11:46:48
 */

@RestController
@Api(tags = {"用户自定义列信息配置"})
@RequestMapping("/ms/settUserColumn")
public class SettUserColumnController{

    @Autowired
    protected SettUserColumnService settUserColumnService;

    @PostMapping()
    @ApiOperation(value = "新增")
    public HttpResult<Boolean> saveOrUpdate(@RequestBody @Validated SettUserColumnVO vo) {
        SettUserColumnPO queryParam = SettUserColumnPO.builder().namespace(vo.getNamespace()).build();
        queryParam.setCreateUser(UserContext.getSessionuser().getUserId());
        //先删除一下然后插入
        settUserColumnService.deleteBean(queryParam);
        settUserColumnService.insert(vo);
        return HttpResult.success(true);
    }

    @GetMapping("/namespace/{namespace}")
    @ApiOperation(value = "根据namespace获取配置")
    public HttpResult<SettUserColumnVO> getByNamespace(@PathVariable("namespace") String namespace){
        SettUserColumnPO queryParam = SettUserColumnPO.builder().namespace(namespace).build();
        queryParam.setCreateUser(UserContext.getSessionuser().getUserId());
        return HttpResult.success(settUserColumnService.selectBean(queryParam));
    }

}
