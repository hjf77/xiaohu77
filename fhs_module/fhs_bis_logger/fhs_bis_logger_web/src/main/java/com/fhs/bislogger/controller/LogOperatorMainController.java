package com.fhs.bislogger.controller;

import com.fhs.bislogger.dox.LogOperatorMainDO;
import com.fhs.bislogger.vo.LogOperatorMainVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

import java.util.*;

/**
 * (LogOperatorMain)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */

@RestController
@Api(tags = {""})
@RequestMapping("/ms/logOperatorMain")
public class LogOperatorMainController extends ModelSuperController<LogOperatorMainVO, LogOperatorMainDO> {

    /**
     * 模型缓存,用于前端下拉
     */
    private List<LogOperatorMainVO> modelSelectCache = new ArrayList<>();
    /**
     * 用于刷新缓存1个小时刷新一次
     */
    private Date modelSelectCachedTime = new Date();


    /**
     * key namespace value 是module
     */
    private Map<String, String> namespaceModuleMap = new HashMap<>();

    public List<LogOperatorMainVO> getModuleSelect(){
        if(modelSelectCache.isEmpty()){
            //doto 执行sql
        }else{
            //判断当前时间和  modelSelectCachedTime 是否大于一个小时,是的话执行刷新缓存
            new Thread(()->{
                //查询..
            }).start();
        }
        return modelSelectCache;
    }

}