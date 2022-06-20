package com.fhs.pagex.service.xsimpl;

import com.fhs.common.constant.Constant;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.JsonUtils;
import com.fhs.common.utils.Logger;
import com.fhs.core.config.EConfig;
import com.fhs.core.exception.BusinessException;
import com.fhs.pagex.common.BeetlUtil;
import com.fhs.pagex.common.PageParamUtils;
import com.fhs.pagex.dto.PagexListSettDTO;
import com.fhs.pagex.service.HandelPageXService;
import com.fhs.pagex.service.IPageXService;
import com.fhs.pagex.service.PagexDataService;
import com.fhs.pagex.tag.grid.BaseGridTag;
import com.fhs.pagex.tag.grid.GridTagFactory;
import com.mybatis.jpa.common.ColumnNameUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 处理pagex列表页面的服务
 *
 * @ProjectName: framework_v2_idea2
 * @Package: com.fhs.pagex
 * @ClassName: PagexListService
 * @Author: JackWang
 * @CreateDate: 2018/11/30 0030 20:07
 * @UpdateUser: JackWang
 * @UpdateDate: 2018/11/30 0030 20:07
 * @Version: 1.0
 */
@Component
public class PagexListService implements IPageXService, InitializingBean {

    private static Map<String, Object> viewButton = new HashMap<>();
    private static Map<String, Object> updateButton = new HashMap<>();
    private static Map<String, Object> delButton = new HashMap<>();

    static {
        viewButton.put("name", "查看");
        viewButton.put("className", "viewBTN");
        viewButton.put("fun", "view");

        updateButton.put("name", "修改");
        updateButton.put("className", "updateBTN");
        updateButton.put("fun", "update");

        delButton.put("name", "删除");
        delButton.put("className", "deleteBTN");
    }

    private static final Logger LOG = Logger.getLogger(PagexListService.class);


    /**
     * 拼接html
     *
     * @param request   request
     * @param response  response
     * @param js        js配置
     * @param namespace 命名空间
     * @return 列表页HTML
     */
    @Override
    public String service(HttpServletRequest request, HttpServletResponse response, String js, String namespace) throws NoSuchMethodException, ScriptException {
        StringBuilder htmlBuilder = new StringBuilder();
        PagexListSettDTO listPageSett = PagexDataService.SIGNEL.getPagexListSettDTOFromCache(namespace);
        if (!listPageSett.getModelConfig().containsKey("dataGridUrl")) {
            listPageSett.getModelConfig().put("dataGridUrl", EConfig.getPathPropertiesValue("basePath") +
                    "/ms/x/" + namespace + "/findPager");
        }
        if (!listPageSett.getModelConfig().get("dataGridUrl").toString().contains("?")) {
            listPageSett.getModelConfig().put("dataGridUrl", listPageSett.getModelConfig().get("dataGridUrl") + "?1=1");
        }
        if (!listPageSett.getModelConfig().containsKey("delUrl")) {
            listPageSett.getModelConfig().put("delUrl", EConfig.getPathPropertiesValue("basePath") +
                    "/ms/x/" + namespace + "/del/");
        }
        StringBuilder formFieldSetts = new StringBuilder("");
        List<Map<String, Object>> fieldSettList = PagexDataService.SIGNEL.getPagexAddDTOFromCache(namespace).getFormFieldSett();
        for (Map<String, Object> field : fieldSettList) {
            formFieldSetts.append(field.get("type") + ",");
        }

        if (listPageSett.getExcss() != null) {
            formFieldSetts.append(",excss");
        }
        if (listPageSett.getExjs() != null) {
            formFieldSetts.append(",exjss");
        }
        // 普通的过滤条件参数 map包含name和val 2个key其中val为此过滤条件的获取值的代码
        List<Map<String, String>> filterParams = new ArrayList<>();
        List<Map<String, String>> filterParamsForBetween = new ArrayList<>();
        String filtersHtml = createFiltersHtml(request, response, listPageSett, filterParams, filterParamsForBetween);
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("filtersHtml", filtersHtml);
            paramMap.put("modelConfig", listPageSett.getModelConfig());
            paramMap.put("listFields", listPageSett.getListSett());
            paramMap.put("namespace", namespace);
            paramMap.put("formFieldTypes", formFieldSetts.toString());
            paramMap.put("excss", listPageSett.getExcss());
            paramMap.put("exjs", listPageSett.getExjs());
            paramMap.put("buttons", getListTopButtons(listPageSett.getButtons()));
            Set<String> disableButtons = new HashSet<>(listPageSett.getDisableButtons());
            String columnButtonsJson = getColumnButtons(listPageSett.getButtons(),namespace,disableButtons);
            paramMap.put("basicButtonsRule", getBasicButtonsRules(namespace, disableButtons));
            paramMap.put("operatorColumnWidth", getOperatorColumnWidth( columnButtonsJson,  disableButtons));
            paramMap.put("columnButtons", columnButtonsJson);
            paramMap.put("disableButtons", listPageSett.getDisableButtons());
            paramMap.put("filterParams", filterParams);
            paramMap.put("filterParamsForBetween", filterParamsForBetween);
            paramMap.put("otherFunctions", listPageSett.getOtherFunctions());
            String resultHtml = BeetlUtil.renderBeelt("/pagex/list_template.html", paramMap);
            PagexDataService.SIGNEL.getListPageHtmlCache().put(namespace, resultHtml);
            return resultHtml;
        } catch (Exception e) {
            LOG.error(this, e);
            throw new BusinessException("页面解析错误");
        }
    }

    /**
     * 操作列按钮宽度
     * @param columnButtonsJson
     * @param disableButtons
     * @return
     */
    public int getOperatorColumnWidth(String columnButtonsJson, Set<String> disableButtons){
        int width = 30;
        if (!disableButtons.contains("view")) {
            width = width + 90;
        }
        if (!disableButtons.contains("update")) {
            width = width + 90;
        }
        if (!disableButtons.contains("delete")) {
            width = width + 90;
        }
        if(columnButtonsJson.length() > 3){
            width = width + 70;
        }
        return width;
    }


    /**
     * 只有全局按钮才放到上面
     *
     * @param allButtons 所有按钮
     * @return 放到上面的按钮
     */
    public List<Map<String, Object>> getListTopButtons(List<Map<String, Object>> allButtons) {
        return allButtons.stream().filter(btn -> {
            //isRow 并且 isRow 属性 为true的过滤掉
            if (btn.containsKey("isRow") && Constant.STR_TRUE.equals(ConverterUtils.toString(btn.get("isRow")))) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
    }

    /**
     * 获取基础按钮
     *
     * @param namespace
     * @param disableButtons
     * @return
     */
    public String getBasicButtonsRules(String namespace, Set<String> disableButtons) {
        Map<String, Boolean> result = new HashMap<>();
        if (!disableButtons.contains("view")) {
            result.put("isHasView", SecurityUtils.getSubject().isPermitted(namespace + ":see"));
        }
        if (!disableButtons.contains("update")) {
            result.put("isHasUpdate", SecurityUtils.getSubject().isPermitted(namespace + ":update"));
        }
        if (!disableButtons.contains("delete")) {
            result.put("isHasDel", SecurityUtils.getSubject().isPermitted(namespace + ":del"));
        }
        return JsonUtils.map2json(result);
    }

    /**
     * 获取操作列上的按钮
     *
     * @param allButtons 所有按钮
     * @return 操作列上的按钮json - 前端可直接用
     */
    public String getColumnButtons(List<Map<String, Object>> allButtons, String namespace,Set<String> disableButtons) {
        List<Map<String, Object>> buttons = allButtons.stream().filter(btn -> {
            //isRow 并且 isRow 属性 为true的过滤掉
            if (!btn.containsKey("isRow") || !Constant.STR_TRUE.equals(ConverterUtils.toString(btn.get("isRow")))) {
                return false;
            }
            //设置了权限编码当前登录人没有权限编码的时候
            if (btn.containsKey("permissionsCode") && !SecurityUtils.getSubject().isPermitted(ConverterUtils.toString(btn.get("permissionsCode")))) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        if (!disableButtons.contains("delete") && SecurityUtils.getSubject().isPermitted(namespace + ":update")) {
            Map<String,Object> delBtn = new HashMap<>();
            delBtn.put("title","删除");
            delBtn.put("type","delete");
            buttons.add(delBtn);
        }
        return JsonUtils.list2json(buttons);
    }


    /**
     * 拼接ToolsBarHtml <div id="toolsbar"></div>
     *
     * @param pagexListSettDTO       列表页配置
     * @param filterParams           过滤条件获取值的代码配置
     * @param filterParamsForBetween 过滤条件获取值的代码配置-between条件处理
     * @return ToolsBar html
     */
    public String createFiltersHtml(HttpServletRequest request, HttpServletResponse response,
                                    PagexListSettDTO pagexListSettDTO, List<Map<String, String>> filterParams, List<Map<String, String>> filterParamsForBetween) {
        String type = null;
        Class gridTagClass = null;
        BaseGridTag gridTag = null;
        StringBuilder filtersBuilder = new StringBuilder();
        Map<String, Object> field = null;
        for (Map<String, Object> tempField : pagexListSettDTO.getFilters()) {
            field = new HashMap<>();
            field.putAll(tempField);
            type = ConverterUtils.toString(field.get("type"));
            gridTagClass = GridTagFactory.getTag(type);
            if (gridTagClass == null) {
                LOG.error("系统不支持的列表type:" + type);
                continue;
            }
            try {

                field.put("name", ColumnNameUtil.underlineToCamel(ConverterUtils.toString(field.get("name"))));
                gridTag = (BaseGridTag) gridTagClass.newInstance();
                gridTag.setTagSett(field, request, response);
                gridTag.initReloadParam(filterParams, filterParamsForBetween);
                filtersBuilder.append(gridTag.getHtmlForToolsBar());
            } catch (InstantiationException e) {
                LOG.error(this, e);
            } catch (IllegalAccessException e) {
                LOG.error(this, e);
            }
        }
        return filtersBuilder.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HandelPageXService.SIGEL.registerPageXService("list.jsp", this);
        HandelPageXService.SIGEL.registerPageXService("list.html", this);
    }
}
