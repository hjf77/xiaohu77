package com.fhs.basics.controller;

import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.service.ServiceWordBookService;
import com.fhs.basics.service.ServiceWordbookAndGroupService;
import com.fhs.basics.service.ServiceWordbookGroupService;
import com.fhs.basics.vo.ServiceWordbookGroupVO;
import com.fhs.basics.vo.ServiceWordbookVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.JsonUtils;
import com.fhs.core.base.controller.BaseController;
import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.result.HttpResult;
import com.fhs.logger.Logger;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典管理controller
 *
 * @author jianbo.qin
 * @date 2020-05-18 16:52:33
 */
@RestController
@RequestMapping("ms/wordbook")
@Api(tags = {"字典"})
@ApiGroup(group = "group_default")
@LogNamespace(namespace = BaseTransConstant.WORD_BOOK, module = "字典管理")
public class ServiceWordbookController extends BaseController {

    private static final Logger LOG = Logger.getLogger(ServiceWordbookController.class);

    @Autowired
    private ServiceWordbookAndGroupService wordbookAndGroupService;

    @Autowired
    private ServiceWordBookService wordBookService;

    @Autowired
    private ServiceWordbookGroupService wordbookGroupService;

    @Autowired
    private RedisCacheService<String> redisCacheService;

    /**
     * 查询字典值
     *
     * @param request
     * @param reponse
     */
    @RequestMapping("findWordbookForPage")
    @LogMethod
    public Pager<ServiceWordbookVO> findWordbookForPage(ServiceWordbookVO wordbook, HttpServletRequest request, HttpServletResponse reponse) {
        PageSizeInfo pageSizeInfo = super.getPageSizeInfo();
        int count = wordBookService.findCountJpa(wordbook);
        List<ServiceWordbookVO> dataList = wordBookService.selectPage(wordbook, pageSizeInfo.getPageStart(), pageSizeInfo.getPageSize());
        return new Pager<ServiceWordbookVO>(count, dataList);
    }

    /**
     * 添加字典
     *
     * @param wordbook
     */
    @RequiresPermissions("wordbook:add")
    @RequestMapping("addWordbook")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> addWordbook(@Valid ServiceWordbookVO wordbook) {
        wordbookAndGroupService.addWordbook(wordbook);
        return HttpResult.success(true);
    }

    /**
     * 添加字典
     *
     * @param wordbook
     */
    @RequiresPermissions("wordbook:add")
    @PostMapping("addWordbookForVue")
    @ApiOperation("创建字典forVUE")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> addWordbookForVue(@RequestBody @Valid ServiceWordbookVO wordbook) {
        return addWordbook(wordbook);
    }


    /**
     * 修改字典
     *
     * @param wordbook
     */
    @RequiresPermissions("wordbook:update")
    @RequestMapping("updateWordbook")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> updateWordbook(@Valid ServiceWordbookVO wordbook) {
        wordbookAndGroupService.updateWordbook(wordbook);
        return HttpResult.success(true);
    }


    @RequiresPermissions("wordbook:update")
    @PutMapping("updateWordbookForVue")
    @ApiOperation("修改字典forVUE")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> updateWordbookForVue(@RequestBody @Valid ServiceWordbookVO wordbook) {
        return updateWordbook(wordbook);
    }

    /**
     * 删除字典
     *
     * @param wordbook
     */
    @RequiresPermissions("wordbook:del")
    @RequestMapping("delWordbook")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_DEL, voParamIndex = 0)
    public HttpResult<Boolean> delWordbook(ServiceWordbookVO wordbook) {
        wordbookAndGroupService.delWordbook(wordbook);
        return HttpResult.success(true);
    }

    @RequiresPermissions("wordbook:del")
    @DeleteMapping("delWordbookForVue")
    @ApiOperation("删除字典forVUE")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_DEL, voParamIndex = 0)
    public HttpResult<Boolean> delWordbookForVue(Integer id) {
        ServiceWordbookVO vo = new ServiceWordbookVO();
        vo.setWordbookId(id);
        return delWordbook(vo);
    }


    /**
     * 根据id获取单个字典
     *
     * @param wordbook
     * @param request
     * @param response
     */
    @RequiresPermissions("wordbook:see")
    @RequestMapping("getWordbookBean")
    @LogMethod
    public ServiceWordbookVO getWordbookBean(ServiceWordbookVO wordbook, HttpServletRequest request, HttpServletResponse response) {
        ServiceWordbookVO bean = wordbookAndGroupService.getWordbookBean(wordbook);
        return bean;
    }

    /**
     * 根据id获取单个字典类型
     *
     * @param request
     * @param response
     */
    @RequiresPermissions("wordbook:see")
    @RequestMapping("getWordbookGroupBean")
    @LogMethod
    public ServiceWordbookGroupVO getWordbookGroupBean(ServiceWordbookGroupVO wordbookGroup, HttpServletRequest request,
                                                       HttpServletResponse response) {
        return wordbookAndGroupService.getWordbookGroupBean(wordbookGroup);
    }

    /**
     * 查询字典类型
     */
    @RequestMapping("findWordbookGroupForPage")
    @LogMethod
    public Pager<ServiceWordbookGroupVO> findWordbookGroupForPage(ServiceWordbookGroupVO wordbookGroup) {
        PageSizeInfo pageSizeInfo = super.getPageSizeInfo();
        int count = wordbookGroupService.findCountJpa(wordbookGroup);
        List<ServiceWordbookGroupVO> dataList = wordbookGroupService.selectPage(wordbookGroup, pageSizeInfo.getPageStart(), pageSizeInfo.getPageSize());
        return new Pager<ServiceWordbookGroupVO>(count, dataList);
    }

    /**
     * 添加字典类型
     *
     * @param wordbookGroup
     * @param request
     */
    @RequiresPermissions("wordbook:add")
    @RequestMapping("addWordbookGroup")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> addWordbookGroup(ServiceWordbookGroupVO wordbookGroup, HttpServletRequest request) {
        wordbookGroup.preInsert(((UcenterMsUserVO) request.getSession().getAttribute(Constant.SESSION_USER)).getUserId());
        wordbookAndGroupService.addWordbookGroup(wordbookGroup);
        return HttpResult.success(true);
    }

    @RequiresPermissions("wordbook:add")
    @PostMapping("addWordbookGroupForVue")
    @ApiOperation("新增字典分组for vue")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> addWordbookGroupForVue(ServiceWordbookGroupVO wordbookGroup, HttpServletRequest request) {
        return addWordbookGroup(wordbookGroup, request);
    }


    /**
     * 修改字典类型
     *
     * @param wordbookGroup
     */
    @RequiresPermissions("wordbook:update")
    @RequestMapping("updateWordbookGroup")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> updateWordbookGroup(ServiceWordbookGroupVO wordbookGroup) {
        wordbookAndGroupService.updateWordbookGroup(wordbookGroup);
        return HttpResult.success(true);
    }

    @RequiresPermissions("wordbook:update")
    @PutMapping("updateWordbookGroupForVue")
    @ApiOperation("修改字典分组for vue")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> updateWordbookGroupForVue(ServiceWordbookGroupVO wordbookGroup) {
        return updateWordbookGroup(wordbookGroup);
    }

    /**
     * 删除字典类型
     *
     * @param wordbookGroup
     */
    @RequiresPermissions("wordbook:del")
    @PostMapping("delWordbookGroup")
    @LogMethod(type = LoggerConstant.OPERATOR_TYPE_DEL, voParamIndex = 0)
    public HttpResult<Boolean> delWordbookGroup(ServiceWordbookGroupVO wordbookGroup) {
        wordbookAndGroupService.delWordbookGroup(wordbookGroup);
        return HttpResult.success(true);
    }

    @RequiresPermissions("wordbook:del")
    @DeleteMapping("delWordbookGroupForVue")
    @ApiOperation("删除字典分组for vue")
    @LogMethod(type = LoggerConstant.OPERATOR_TYPE_DEL, voParamIndex = 0)
    public HttpResult<Boolean> delWordbookGroupForVue(Integer id) {
        ServiceWordbookGroupVO vo = new ServiceWordbookGroupVO();
        vo.setGroupId(id);
        return delWordbookGroup(vo);
    }


    /**
     * 刷新redis缓存
     *
     * @param request
     * @param response
     */
    @RequiresPermissions("wordbook:refreshRedisCache")
    @RequestMapping("refreshRedisCache")
    @ResponseBody
    public HttpResult<Boolean> refreshRedisCache(ServiceWordbookVO wordbook, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> message = new HashMap<>();
        message.put("transType", "wordbook");
        message.put("wordbookGroupCode", wordbook.getWordbookGroupCode());
        redisCacheService.convertAndSend("trans", JsonUtils.map2json(message));
        wordbookAndGroupService.refreshRedisCache(wordbook);
        return HttpResult.success(true);
    }

    /**
     * 查询数据
     *
     * @return addok
     */
    @RequestMapping("getData")
    public List<ServiceWordbookVO> getData(String wordbookGroupCode) {
        return wordBookService.getWordBookList(wordbookGroupCode);
    }
}
