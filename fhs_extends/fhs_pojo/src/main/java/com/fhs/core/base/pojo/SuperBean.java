package com.fhs.core.base.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fhs.common.utils.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有的VO DO都需要继承此类
 *
 * @param <T>
 * @author user
 * @date 2020-05-19 11:51:55
 */
@Data
@Slf4j
@SuppressWarnings({"serial", "rawtypes"})
public class SuperBean<T extends SuperBean> extends BaseObject<T> {

    /**
     * 翻译map 给transervice用的
     */
    @TableField(exist = false)
    private Map<String, String> transMap = new HashMap<>();

    /**
     * 数据权限
     */
    @TableField(exist = false)
    @JSONField(serialize = false)
    private Map<String, String> dataPermissin = new HashMap<>();

    /**
     * 配合mybatis jpa between注解过滤条件使用
     */
    @TableField(exist = false)
    @JSONField(serialize = false)
    private Map<String, String> between = new HashMap<>();

    /**
     * 配合mybatis jpa in注解使用
     */
    @TableField(exist = false)
    @JSONField(serialize = false)
    private Map<String, String> inFilter = new HashMap<>();

    @TableField(exist = false)
    @JSONField(serialize = false)
    private Map<String, Object> userInfo = new HashMap<>();

    /**
     * 高级搜索过滤条件
     */
    @TableField(exist = false)
    @JSONField(serialize = false)
    private String extAdvanceFilterParam;

    @TableField(exist = false)
    @JSONField(serialize = false)
    private Integer start;

    @TableField(exist = false)
    @JSONField(serialize = false)
    private Integer PageSize;


    /**
     * 将一组过滤条件添加到in中
     *
     * @param field   字段名字
     * @param inParam 参数集合
     */
    public void add2In(String field, List<String> inParam) {
        inFilter.put(field, StringUtil.getStrToIn(inParam));
    }


    private static final Map<String, String> SIMPLE_OPERATOR = new HashMap();

    static {
        SIMPLE_OPERATOR.put(POJOConstant.EQ, " = ");
        SIMPLE_OPERATOR.put(POJOConstant.LIKE, " LIKE ");
        SIMPLE_OPERATOR.put(POJOConstant.NEQ, " != ");
        SIMPLE_OPERATOR.put(POJOConstant.BIGGER_EQ, " >= ");
        SIMPLE_OPERATOR.put(POJOConstant.LESS_EQ, " <= ");
        SIMPLE_OPERATOR.put(POJOConstant.LESS, " < ");
        SIMPLE_OPERATOR.put(POJOConstant.START_WITH, " LIKE ");
        SIMPLE_OPERATOR.put(POJOConstant.END_WITH, " LIKE ");
        SIMPLE_OPERATOR.put(POJOConstant.BIGGER, " > ");
    }

    /**
     * 获取高级搜索的where条件
     *
     * @return
     */
    @JSONField(serialize = false)
    public String getAdvanceSearchSql() {
        if (extAdvanceFilterParam == null) {
            return null;
        }
        JSONObject extAdvanceFilterParamJson = JSON.parseObject(extAdvanceFilterParam);
        JSONArray extAdvanceFilterParamArray = extAdvanceFilterParamJson.getJSONArray("extAdvanceFilterParamArray");
        JSONObject tempAFilter = null;
        Field field = null;
        String sqlField = null;
        String tempVal = null;
        StringBuilder whereSql = new StringBuilder(" AND (");
        boolean isHashWhere = false;
        for (int i = 0; i < extAdvanceFilterParamArray.size(); i++) {
            tempAFilter = extAdvanceFilterParamArray.getJSONObject(i);
            field = ReflectUtils.getDeclaredField(this.getClass(), tempAFilter.getString("name"));
            if (field == null) {
                log.error("字段不存在:" + field);
                continue;
            }
            sqlField = getSqlField(field);
            tempVal = formartVal(field, tempAFilter.get("val"), tempAFilter.getString("filterType"), tempAFilter.getString("searchKeyType")
                    , tempAFilter.getString("fieldName"));
            if (sqlField == null || tempVal == null || !SIMPLE_OPERATOR.containsKey(tempAFilter.getString("filterType"))) {
                log.error("条件不满足，无法拼接此字段，详情请打断点:" + field);
                continue;
            }
            if (whereSql.length() > 6) {
                whereSql.append(tempAFilter.getString("connector")+" ");
            }
            whereSql.append(sqlField + ("searchKey".equals(field.getName()) ? " LIKE " : SIMPLE_OPERATOR.get(tempAFilter.getString("filterType"))) + tempVal + " ");
            isHashWhere = true;
        }
        if(!isHashWhere){
            return "";
        }
        whereSql.append( ")");
        return whereSql.toString();
    }

    /**
     * 格式化值
     *
     * @param field      lambdaSett
     * @param val        值
     * @param filterType
     * @return 值的sql格式
     */
    protected String formartVal(Field field, Object val, String filterType, String searchKeyType, String fieldName) {
        if (val == null || "null".equals(val)) {
            return "null";
        }
        Class<?> type = field.getType();
        String result = null;
        // 字符串直接是字段名
        if (!CheckUtils.isNullOrEmpty(searchKeyType)) {
            if ("str".equals(searchKeyType)) {
                return " CONCAT('%','\"" + fieldName + "\"','%','" + val + "','%') ";
            } if ("streq".equals(searchKeyType)) {
                return " CONCAT('%','\"" + fieldName + "\":\"" + val + "\"','%') ";
            } else if ("date".equals(searchKeyType)) {
                return " CONCAT('%','\"" + fieldName + "\":\"" + val + "','%') ";
            } else if ("int".equals(searchKeyType)) {
                return " CONCAT('%','\"" + fieldName + "\":\"" + val + "\"','%') ";
            }
        }
        //只有字符串才有like 需要特殊处理
        if (type == String.class) {
            if (POJOConstant.LIKE.equals(filterType)) {
                result = " CONCAT('%','" + val + "','%') ";
            } else if (POJOConstant.START_WITH.equals(filterType)) {
                result = " CONCAT('" + val + "','%') ";
            } else if (POJOConstant.END_WITH.equals(filterType)) {
                result = " CONCAT('%','" + val + "') ";
            } else {
                result = "'" + val + "'";
            }
        } else if (type == Number.class || Number.class.isAssignableFrom(type)) {
            result = ConverterUtils.toString(val);
        } else if (type == Date.class || Date.class.isAssignableFrom(type)) {
            Date dateVal = DateUtils.parseStr(ConverterUtils.toString(val));
            return "FROM_UNIXTIME(" + (dateVal.getTime() / 1000) + ")";
        } else {
            log.warn("格式不支持:" + field + val);
            return null;
        }
        return result;
    }


    public String getSqlField(Field field) {
        if (field.isAnnotationPresent(TableField.class)) {
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField.exist()) {
                return tableField.value();
            } else {

                return null;
            }
        } else if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            return column.name();
        }
        return null;
    }

}
