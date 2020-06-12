package com.fhs.bislogger.api.context;

import com.fhs.bislogger.dox.LogOperatorExtParamDO;
import com.fhs.bislogger.vo.LogAddOperatorLogVO;
import com.fhs.bislogger.vo.LogHistoryDataVO;
import com.fhs.bislogger.vo.LogOperatorExtParamVO;
import com.fhs.common.utils.JsonUtils;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.cache.service.RedisCacheService;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志上下文
 * @author user
 * @date 2020-05-18 14:20:14
 */
public class BisLoggerContext {

    private static ThreadLocal<String> traceId = new ThreadLocal<>();

    private static ThreadLocal<List<LogOperatorExtParamVO>> logOperatorExtParamList = new ThreadLocal<>();

    private static ThreadLocal<List<LogHistoryDataVO>> logHistoryDataVOList = new ThreadLocal<>();

    /**
     * 模型map key namespace value 模型
     */
    private static Map<String, String> modeuleMap = new HashMap<>();

    public static void init(String traceId) {
        BisLoggerContext.traceId.set(traceId);
        BisLoggerContext.logOperatorExtParamList.set(new ArrayList<>());
        BisLoggerContext.logHistoryDataVOList.set(new ArrayList<>());
    }

    public static List<LogOperatorExtParamVO> getLogOperatorExtParamList() {
        return logOperatorExtParamList.get();
    }

    public static List<LogHistoryDataVO> getLogHistoryDataVOList() {
        return logHistoryDataVOList.get();
    }

    /**
     * 添加模块映射
     *
     * @param namespace namespace
     * @param module    中文名
     */
    public static void addModule(String namespace, String module) {
        modeuleMap.put(namespace, module);
    }

    public static String getTranceId() {
        return BisLoggerContext.traceId.get();
    }


    /**
     * 添加扩展参数
     * @param namespace
     * @param pkey
     * @param operatorType
     */
    public static void addExtParam(String namespace, Object pkey, int operatorType) {
        if (namespace == null || pkey == null || !isNeedLogger()) {
            return;
        }
        LogOperatorExtParamVO extParamVO = new LogOperatorExtParamVO();
        extParamVO.setPkey(pkey.toString());
        extParamVO.setNamespace(namespace);
        extParamVO.setMainId(getTranceId());
        extParamVO.setOperatorType(operatorType);
        logOperatorExtParamList.get().add(extParamVO);

    }


    public static void addHistoryData(VO vo, String namespace) {
        if (namespace == null || vo.getPkey() == null || !isNeedLogger()) {
            return;
        }
        LogHistoryDataVO historyDataVO = new LogHistoryDataVO();
        historyDataVO.setPkey(vo.getPkey().toString());
        historyDataVO.setData(formartJson(JsonUtils.bean2json(vo), vo.getClass()));
        historyDataVO.setNamespace(namespace);
        historyDataVO.setMainId(getTranceId());
        logHistoryDataVOList.get().add(historyDataVO);
    }

    /**
     * 格式化json
     *
     * @param json  json
     * @param clazz 类
     * @return
     */
    public static String formartJson(String json, Class clazz) {
        List<Field> fieldList = ReflectUtils.getAnnotationField(clazz, ApiModelProperty.class);
        ApiModelProperty apiModelProperty = null;
        for (Field field : fieldList) {
            apiModelProperty = field.getAnnotation(ApiModelProperty.class);
            json = json.replace(field.getName(), apiModelProperty.value());
        }
        return json;
    }

    public static void clear() {
        BisLoggerContext.logOperatorExtParamList.set(null);
        BisLoggerContext.logHistoryDataVOList.set(null);
        BisLoggerContext.traceId.set(null);

    }

    public static boolean isNeedLogger() {
        return getTranceId() != null;
    }

}
