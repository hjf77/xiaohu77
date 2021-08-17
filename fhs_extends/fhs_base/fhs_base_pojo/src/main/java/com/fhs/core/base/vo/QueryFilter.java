package com.fhs.core.base.vo;

import ch.qos.logback.core.pattern.ConverterUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.StringUtil;
import com.github.liangbaika.validate.exception.ParamsInValidException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Data
@Builder
@AllArgsConstructor
@ApiModel("用于自定义条件过滤")
public class QueryFilter<T> {

    private static final String AND = "AND";

    private static final String OR = "OR";

    @ApiModelProperty("分页信息")
    private FhsPager<T> pagerInfo;

    @ApiModelProperty(
            name = "sorter",
            notes = "排序字段"
    )
    private List<FieldSort> sorter = new ArrayList();
    @ApiModelProperty(
            name = "params",
            notes = "自定义查询"
    )
    private Map<String, Object> params = new LinkedHashMap();
    @ApiModelProperty(
            name = "querys",
            notes = "查询条件组"
    )
    private List<QueryField> querys = new ArrayList();
    @ApiModelProperty(
            name = "groupRelation",
            notes = "查询条件分组的关系",
            example = "AND"
    )
    private String groupRelation;

    private QueryFilter() {
        this.groupRelation = AND;
    }

    public static <T extends Model<T>> QueryFilter<T> build() {
        return new QueryFilter();
    }


    public QueryFilter<T> withSorter(FieldSort fieldSort) {
        this.sorter.add(fieldSort);
        return this;
    }

    public QueryFilter<T> withQuery(QueryField queryField) {
        this.querys.add(queryField);
        return this;
    }

    public QueryFilter<T> withParam(String key, Object value) {
        this.params.put(key, value);
        return this;
    }


    @JSONField(serialize = false)
    public Map<String, List<QueryField>> groupQueryField() {
        Map<String, List<QueryField>> map = new HashMap();
        this.querys.forEach((q) -> {
            String group = q.getGroup();
            List<QueryField> list = (List) map.get(group);
            if (list == null) {
                list = new ArrayList();
                map.put(group, list);
            }

            ((List) list).add(q);
        });
        return map;
    }

    public Map<Direction, List<FieldSort>> groupFieldSort() {
        Map<Direction, List<FieldSort>> map = new HashMap();
        this.sorter.forEach((q) -> {
            Direction direct = q.getDirection();
            List<FieldSort> list = (List) map.get(direct);
            if (list == null) {
                list = new ArrayList();
                map.put(direct, list);
            }

            ((List) list).add(q);
        });
        return map;
    }

    public QueryWrapper<T> asWrapper(Class currentModelClass) {
        QueryWrapper<T> queryWrapper = new QueryWrapper();
        Map<String, List<QueryField>> groupQueryField = this.groupQueryField();
        String groupRelation = this.getGroupRelation();
        groupQueryField.forEach((group, list) -> {
            if (AND.equals(groupRelation)) {
                queryWrapper.and((x) -> {
                    list.forEach((l) -> {
                        this.convertQueryField(x, l, currentModelClass);
                    });
                });
            } else {
                if (!OR.equals(groupRelation)) {
                    throw new ParamsInValidException("关联条件只能传AND 或者OR");
                }
                queryWrapper.or((x) -> {
                    list.forEach((l) -> {
                        this.convertQueryField(x, l, currentModelClass);
                    });
                });
            }

        });
        Map<Direction, List<FieldSort>> groupFieldSort = this.groupFieldSort();
        groupFieldSort.forEach((d, l) -> {
            if (Direction.DESC.equals(d)) {
                queryWrapper.orderByDesc(this.convertSortFieldList(l, currentModelClass));
            } else {
                queryWrapper.orderByAsc(this.convertSortFieldList(l, currentModelClass));
            }

        });

        return queryWrapper;
    }

    private static final String INJECTION_REGEX = "[A-Za-z0-9\\_\\-\\+\\.]+";
    private static final String WHERE_SQL_TAG = "whereSql";
    private static final String ORDER_SQL_TAG = "orderBySql";

    private String[] convertSortFieldList(List<FieldSort> list, Class<T> currentModelClass) {
        if (list == null) {
            return null;
        } else {
            String[] ary = new String[list.size()];
            for (int i = 0; i < list.size(); ++i) {
                ary[i] = getField(list.get(i).getProperty(), currentModelClass);
            }
            return ary;
        }
    }

    /**
     * 获取数据库字段
     *
     * @param fieldName         Java字段名
     * @param currentModelClass 类
     * @return
     */
    @JSONField(serialize = false)
    private String getField(String fieldName, Class<T> currentModelClass) {
        if (currentModelClass == null) {
            return fieldName;
        }
        Field classField = ReflectionUtils.findField(currentModelClass, fieldName);
        if (classField != null) {
            if (classField.isAnnotationPresent(TableField.class)) {
                fieldName = classField.getAnnotation(TableField.class).value();
            } else if (classField.isAnnotationPresent(TableId.class)) {
                fieldName = classField.getAnnotation(TableId.class).value();
            }
        }
        return fieldName;
    }

    private void convertQueryField(QueryWrapper<T> queryWrapper, QueryField queryField, Class<T> currentModelClass) {
        String r = queryField.getRelation();
        if (OR.equals(r)) {
            queryWrapper.or();
        }
        String field = getField(queryField.getProperty(), currentModelClass);

        String operation = queryField.getOperation();
        switch (operation) {
            case "=":
                queryWrapper.eq(field, queryField.getValue());
                break;
            case "<":
                queryWrapper.lt(field, queryField.getValue());
                break;
            case ">":
                queryWrapper.gt(field, queryField.getValue());
                break;
            case "<=":
                queryWrapper.le(field, queryField.getValue());
                break;
            case ">=":
                queryWrapper.ge(field, queryField.getValue());
                break;
            case "!=":
                queryWrapper.ne(field, queryField.getValue());
                break;
            case "like":
                queryWrapper.like(field, queryField.getValue());
                break;
            case "like_l":
                queryWrapper.likeLeft(field, queryField.getValue());
                break;
            case "like_r":
                queryWrapper.likeRight(field, queryField.getValue());
                break;
            case "not_like_r":
                queryWrapper.apply(field + " not like '" + queryField.getValue() + "%'");
                break;
            case "is_null":
                queryWrapper.isNull(field);
                break;
            case "not_null":
                queryWrapper.isNotNull(field);
                break;
            case "in":
                Object[] values = this.convert2ObjectArray(queryField.getValue());
                if (values != null && values.length > 0) {
                    queryWrapper.in(field, this.convert2ObjectArray(queryField.getValue()));
                }
                break;
            case "find_in_set":
                queryWrapper.apply("FIND_IN_SET('" + queryField.getValue() + "'," + field + ")");
                break;
            case "find_in_set_in":
                if (queryField.getValue() != null) {
                    Object[] params = convert2ObjectArray(queryField.getValue());
                    StringBuilder whereBulder = new StringBuilder("(");
                    for (int i = 0; i < params.length; i++) {
                        if (i != 0) {
                            whereBulder.append(" OR ");
                        }
                        whereBulder.append(" FIND_IN_SET('" + params[i] + "'," + field + ") ");
                    }
                    whereBulder.append(" ) ");
                    queryWrapper.apply(whereBulder.toString());
                }
                break;
            case "ext":
                queryWrapper.apply(ConverterUtils.toString(queryField.getValue()));
                break;
            case "between":
                Object[] objs = this.convert2ObjectArray(queryField.getValue());
                if (objs != null && objs.length > 0) {
                    Assert.isTrue(objs.length == 2, String.format("查询条件为between时，查询值必须为两个，但是传入的查询值为：%s", objs));
                    queryWrapper.between(field, objs[0], objs[1]);
                }
        }

    }

    private Object[] convert2ObjectArray(Object obj) {
        if (obj == null) {
            return new Object[]{"''"};
        } else if (!(obj instanceof String)) {
            if (obj instanceof List) {
                List<Object> objList = (List) obj;
                return objList.toArray();
            } else if (obj instanceof Object[]) {
                return (Object[]) obj;
            } else {
                return obj instanceof Object ? new Object[]{obj} : null;
            }
        } else {
            String str = obj.toString();
            String[] split = str.split(",");

            for (int i = 0; i < split.length; ++i) {
                split[i] = this.handleQuotation(split[i]);
            }

            return split;
        }
    }

    private String handleQuotation(String str) {
        String ResultString = str;
        try {
            Pattern regex = Pattern.compile("^'(.*)'$");
            Matcher regexMatcher = regex.matcher(str);
            if (regexMatcher.find()) {
                ResultString = regexMatcher.group(1);
            }
        } catch (PatternSyntaxException var5) {
        }

        return ResultString;
    }

}
