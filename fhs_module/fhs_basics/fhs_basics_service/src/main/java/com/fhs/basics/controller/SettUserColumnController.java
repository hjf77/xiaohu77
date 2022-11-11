package com.fhs.basics.controller;

import com.fhs.basics.context.UserContext;
import com.fhs.basics.po.SettUserColumnPO;
import com.fhs.basics.service.SettUserColumnService;
import com.fhs.basics.vo.SettUserColumnVO;
import com.fhs.core.result.HttpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户自定义列信息配置(SettUserColumn)表控制层
 *
 * @author wanglei
 * @since 2022-10-13 11:46:48
 */

@RestController
@Api(tags = {"用户自定义列信息配置"})
@RequestMapping("/ms/settUserColumn")
public class SettUserColumnController {

    @Autowired
    protected SettUserColumnService settUserColumnService;

    @PostMapping()
    @ApiOperation(value = "新增")
    public HttpResult<SettUserColumnVO> saveOrUpdate(@RequestBody @Validated SettUserColumnVO vo) {
        SettUserColumnPO queryParam = SettUserColumnPO.builder().namespace(vo.getNamespace()).build();
        queryParam.setCreateUser(UserContext.getSessionuser().getUserId());
        if(vo.getId() == null){
            settUserColumnService.insert(vo);
        }else{
            settUserColumnService.updateSelectiveById(vo);
        }
        return HttpResult.success(vo);
    }

    @GetMapping("/namespace/{namespace}")
    @ApiOperation(value = "根据namespace获取配置")
    public HttpResult<SettUserColumnVO> getByNamespace(@PathVariable("namespace") String namespace){
        SettUserColumnPO queryParam = SettUserColumnPO.builder().namespace(namespace).build();
        queryParam.setCreateUser(UserContext.getSessionuser().getUserId());
        List<SettUserColumnVO> userColumnVOList = settUserColumnService.selectListMP(queryParam.asWrapper());
        return HttpResult.success(userColumnVOList.isEmpty() ? null : userColumnVOList.get(0));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除配置数据")
    public HttpResult deleteById(@PathVariable("id") Long id){
        int i = settUserColumnService.deleteById(id);
        if(i > 0){
            return HttpResult.success();
        }
        return HttpResult.error(null,"删除自定义列数据失败");
    }

}
