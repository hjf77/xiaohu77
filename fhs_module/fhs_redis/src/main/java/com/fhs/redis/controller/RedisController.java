package com.fhs.redis.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fhs.redis.config.RedisConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.workcenter.common.WorkcenterResult;
import cn.workcenter.common.response.WorkcenterResponseBodyJson;
import cn.workcenter.common.util.StringUtil;

import com.fhs.redis.service.RedisService;
import com.fhs.redis.service.ViewService;
import com.fhs.redis.util.Constant;
import com.fhs.redis.util.ConvertUtil;
import com.fhs.redis.util.Pagination;
import com.fhs.redis.util.QueryEnum;
import com.fhs.redis.util.RKey;
import com.fhs.redis.util.RedisApplication;
import com.fhs.redis.util.ztree.ZNode;

@Controller
@RequestMapping("/redis")
public class RedisController extends RedisApplication implements Constant {

    @Autowired
    private ViewService viewService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisConfigs redisConfigs;


    @RequestMapping(method = RequestMethod.GET)
    public Object home(HttpServletRequest request, HttpServletResponse response) {
        String defaultServerName = (String) (RedisApplication.redisServerCache.get(0) == null ? "" : RedisApplication.redisServerCache.get(0).get("name"));
        request.setAttribute("serverName", defaultServerName);
        request.setAttribute("dbIndex", DEFAULT_DBINDEX.get(REDISPROPERTIES_DBINDEX_PROFIXKEY));
        return "redirect:/redis/stringList/" + defaultServerName + "/" + DEFAULT_DBINDEX.get(REDISPROPERTIES_DBINDEX_PROFIXKEY);
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public Object index(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("viewPage", "home.html");

        String defaultServerName = (String) (RedisApplication.redisServerCache.get(0) == null ? "" : RedisApplication.redisServerCache.get(0).get("name"));
        request.setAttribute("serverName", defaultServerName);
        request.setAttribute("dbIndex", DEFAULT_DBINDEX.get(REDISPROPERTIES_DBINDEX_PROFIXKEY));
        return "page/ms/redis/main";
    }

    @RequestMapping(value = "/addServer", method = RequestMethod.POST)
    @ResponseBody
    public Object addServer(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam String host,
                            @RequestParam String name,
                            @RequestParam int port,
                            @RequestParam String password) {
        name = name.replace(" ", "");
        redisService.addRedisServer(name, host, port, password);

        return WorkcenterResponseBodyJson.custom().build();
    }

    @RequestMapping(value = "/serverTree", method = RequestMethod.GET)
    @ResponseBody
    public Object serverTree(HttpServletRequest request, HttpServletResponse response) {

        Set<ZNode> keysSet = viewService.getLeftTree();

        return keysSet;
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    @ResponseBody
    public Object refresh(HttpServletRequest request, HttpServletResponse response) {
        logCurrentTime("viewService.refresh(); start");
        viewService.refresh();
        logCurrentTime("viewService.refresh(); end");
        return WorkcenterResponseBodyJson.custom().build();
    }

    private void refreshByMode() {
        switch (refreshMode) {
            case manually:
                break;
            case auto:
                viewService.refresh();
                break;
        }
    }

    @RequestMapping(value = "/refreshMode", method = RequestMethod.POST)
    @ResponseBody
    public Object refreshMode(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam String mode) {

        viewService.changeRefreshMode(mode);

        return WorkcenterResponseBodyJson.custom().build();
    }

    @RequestMapping(value = "/changeShowType", method = RequestMethod.POST)
    @ResponseBody
    public Object changeShowType(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam String state) {

        viewService.changeShowType(state);
        return WorkcenterResponseBodyJson.custom().build();
    }

    @RequestMapping(value = "/stringList/{serverName}/{dbIndex}", method = RequestMethod.GET)
    public Object stringList(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable String serverName, @PathVariable String dbIndex) {

        refreshByMode();

        String queryKey = StringUtil.getParameterByDefault(request, "queryKey", MIDDLE_KEY);
        String queryKey_ch = QueryEnum.valueOf(queryKey).getQueryKeyCh();
        String queryValue = StringUtil.getParameterByDefault(request, "queryValue", EMPTY_STRING);
        String queryByKeyPrefixs = StringUtil.getParameterByDefault(request, "queryByKeyPrefixs", EMPTY_STRING);

        String[] keyPrefixs = request.getParameterValues("keyPrefixs");

        Pagination pagination = stringListPagination(request, queryKey, queryKey_ch, queryValue, queryByKeyPrefixs);

        logCurrentTime("viewService.getRedisKeys start");
        Set<RKey> redisKeys = viewService.getRedisKeys(pagination, serverName, dbIndex, keyPrefixs, queryKey, queryValue);
        logCurrentTime("viewService.getRedisKeys end");
        request.setAttribute("redisServers", redisServerCache);
        request.setAttribute("queryLabel_ch", queryKey_ch);
        request.setAttribute("queryLabel_en", queryKey);
        request.setAttribute("queryValue", queryValue);
        request.setAttribute("serverName", serverName);
        request.setAttribute("dbIndex", dbIndex);
        request.setAttribute("redisKeys", redisKeys);
        request.setAttribute("refreshMode", refreshMode.getLabel());
        request.setAttribute("change2ShowType", showType.getChange2());
        request.setAttribute("showType", showType.getState());
        request.setAttribute("pagination", pagination.createLinkTo());
        request.setAttribute("viewPage", "redis/list.html");
        return "page/ms/redis/main";
    }

    private Pagination stringListPagination(HttpServletRequest request, String queryKey, String queryKey_ch, String queryValue, String queryByKeyPrefixs) {
        Pagination pagination = getPagination(request);
        String url = "?" + "queryKey=" + queryKey + "&queryKey_ch=" + queryKey_ch + "&queryValue=" + queryValue;
        pagination.setLink_to(url);
        if (!StringUtil.isEmpty(queryByKeyPrefixs)) {

        }
        return pagination;
    }

    private Pagination getPagination(HttpServletRequest request) {
        String items_per_page = StringUtil.getParameterByDefault(request, "items_per_page", DEFAULT_ITEMS_PER_PAGE + "");
        String num_display_entries = StringUtil.getParameterByDefault(request, "num_display_entries", "3");
        String visit_page = StringUtil.getParameterByDefault(request, "visit_page", "0");
        String num_edge_entries = StringUtil.getParameterByDefault(request, "num_edge_entries", "2");
        String prev_text = StringUtil.getParameterByDefault(request, "prev_text", "Prev");
        String next_text = StringUtil.getParameterByDefault(request, "next_text", "Next");
        String ellipse_text = StringUtil.getParameterByDefault(request, "ellipse_text", "Next");
        String prev_show_always = StringUtil.getParameterByDefault(request, "prev_show_always", "true");
        String next_show_always = StringUtil.getParameterByDefault(request, "next_show_always", "true");

        Pagination pagination = new Pagination();
        pagination.setItems_per_page(Integer.parseInt(items_per_page));
        pagination.setNum_display_entries(Integer.parseInt(num_display_entries));
        pagination.setCurrent_page(Integer.parseInt(visit_page));
        pagination.setNum_edge_entries(Integer.parseInt(num_edge_entries));
        pagination.setPrev_text(prev_text);
        pagination.setNext_text(next_text);
        pagination.setEllipse_text(ellipse_text);
        pagination.setPrev_show_always(Boolean.parseBoolean(prev_show_always));
        pagination.setNext_show_always(Boolean.parseBoolean(next_show_always));

        return pagination;
    }

    @RequestMapping(value = "/KV", method = RequestMethod.POST)
    @ResponseBody
    public Object updateKV(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam String serverName, @RequestParam int dbIndex,
                           @RequestParam String dataType,
                           @RequestParam String key) {

        String[] value = request.getParameterValues("value");
        double[] score = ConvertUtil.convert2Double(request.getParameterValues("score"));
        String[] member = request.getParameterValues("member");
        String[] field = request.getParameterValues("field");
        serverName = serverName.replace(" ", "");
        redisService.addKV(serverName, dbIndex, dataType, key, value, score, member, field);

        return WorkcenterResponseBodyJson.custom().build();
    }

    @RequestMapping(value = "/KV", method = RequestMethod.GET)
    @ResponseBody
    public Object getKV(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam String serverName, @RequestParam int dbIndex,
                        @RequestParam String dataType,
                        @RequestParam String key) {

        WorkcenterResult result = (WorkcenterResult) redisService.getKV(serverName, dbIndex, dataType, key);

        return WorkcenterResponseBodyJson.custom().setAll(result, GETKV).build();
    }

    @RequestMapping(value = "/delKV", method = RequestMethod.POST)
    @ResponseBody
    public Object delKV(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam String serverName, @RequestParam int dbIndex,
                        @RequestParam String deleteKeys) {

        redisService.delKV(serverName, dbIndex, deleteKeys);

        return WorkcenterResponseBodyJson.custom().build();

    }
}
