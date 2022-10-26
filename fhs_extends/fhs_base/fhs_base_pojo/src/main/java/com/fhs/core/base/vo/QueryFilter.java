package com.fhs.core.base.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaJoinQueryWrapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.StringUtils;
import com.fhs.core.base.po.BasePO;
import com.github.liangbaika.validate.exception.ParamsInValidException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import static com.fhs.common.utils.ReflectUtils.getDeclaredField;

@Data
@Slf4j
@Builder
@AllArgsConstructor
@ApiModel("用于自定义条件过滤")
public class QueryFilter<T> {

    private static final String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"

            + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

    /**
     * 表示忽略大小写
     */
    private static final Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

    private static final Set<String> OPEARTOR_SET = new HashSet<>();

    private static final String AND = "AND";

    private static final String OR = "OR";

    static {
        OPEARTOR_SET.add("=");
        OPEARTOR_SET.add(">");
        OPEARTOR_SET.add(">=");
        OPEARTOR_SET.add("<");
        OPEARTOR_SET.add("<=");
        OPEARTOR_SET.add("like");
    }

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

    /**
     * 安全字段，就算前端输入了这些字段后端也不拼接相关条件
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private Set<String> safeFieldsSet = new HashSet<>();


    public QueryFilter() {
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

    public LambdaJoinQueryWrapper<T> asWrapper(Class currentModelClass, String... safeFields) {
        if (safeFields != null) {
            safeFieldsSet = new HashSet<>(Arrays.asList(safeFields));
        }
        LambdaJoinQueryWrapper<T> queryWrapper = new LambdaJoinQueryWrapper<>(currentModelClass);
        Map<String, List<QueryField>> groupQueryField = this.groupQueryField();
        String groupRelation = this.getGroupRelation();
        groupQueryField.forEach((group, list) -> {
            List<QueryField> newListFields = list.stream().filter(queryField -> !safeFieldsSet.contains(queryField.getProperty())).collect(Collectors.toList());
            if (newListFields.isEmpty()) {
                return;
            }
            if (AND.equals(groupRelation)) {
                queryWrapper.and((x) -> {
                    newListFields.forEach((l) -> {
                        this.convertQueryField((LambdaJoinQueryWrapper<T>) x, l, currentModelClass);
                    });
                });
            } else {
                if (!OR.equals(groupRelation)) {
                    throw new ParamsInValidException("关联条件只能传AND 或者OR");
                }
                queryWrapper.or((x) -> {
                    newListFields.forEach((l) -> {
                        this.convertQueryField((LambdaJoinQueryWrapper<T>) x, l, currentModelClass);
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

    private List<SFunction> convertSortFieldList(List<FieldSort> list, Class<T> currentModelClass) {
        if (list == null) {
            return null;
        } else {
            List<SFunction> result = new ArrayList<>();
            for (int i = 0; i < list.size(); ++i) {
                result.add(getField(list.get(i).getProperty(), currentModelClass));
            }
            return result;
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
    public SFunction getField(String fieldName, Class<T> currentModelClass) {
        if (currentModelClass == null) {
            throw new ParamsInValidException("currentModelClass不能为null");
        }
        return getSFunction(currentModelClass,fieldName);
      /*  Map<String, ColumnCache> cacheMap = LambdaUtils.getColumnMap(currentModelClass);
        if (cacheMap == null) {
            throw new ParamsInValidException(currentModelClass + "找不到表字段对应关系，请检查mapper.xml是否已经存在并且目录正确");
        }
        if (!cacheMap.containsKey(fieldName.toUpperCase())) {
            throw new ParamsInValidException(fieldName + "不正确");
        }
        return cacheMap.get(fieldName.toUpperCase()).getColumn();*/
    }

    /**
     * 获取目标表的id 数据库字段名
     *
     * @param targetClassName 对方类名
     * @return id数据库字段名
     */
    public String getTargetKeyColumn(String targetClassName) {
        return TableInfoHelper.getTableInfo(getTargetClass(targetClassName)).getKeyColumn();
    }

    /**
     * 获取目标表的表名
     *
     * @param targetClassName 对方类名
     * @return 表名
     */
    public String getTargetTableName(String targetClassName) {
        return TableInfoHelper.getTableInfo(getTargetClass(targetClassName)).getTableName();
    }

    /**
     * 获取目标类
     *
     * @param targetClassName 类全名
     * @return 类对象
     */
    public Class getTargetClass(String targetClassName) {
        try {
            return Class.forName(targetClassName);
        } catch (ClassNotFoundException e) {
            log.error("类名错误", e);
            throw new ParamsInValidException("类名错误");
        }
    }

    /**
     * 将query filed 转换添加到  queryWrapper 中
     *
     * @param queryWrapper
     * @param queryField
     * @param currentModelClass
     */
    private void convertQueryField(LambdaJoinQueryWrapper<T> queryWrapper, QueryField queryField, Class<T> currentModelClass) {
        String r = queryField.getRelation();
        if (OR.equals(r)) {
            queryWrapper.or();
        }
        //如果是空或者null 字符串则不加此过滤条件
        if (CheckUtils.isNullOrEmpty(queryField.getValue()) || "null".equals(ConverterUtils.toString(queryField.getValue()))) {
            return;
        }
        SFunction field = null;
        field = getField(queryField.getProperty(), currentModelClass);
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
            case "between"://前端经常用的是 时间过滤，比如查询 2020-01-01 到2020-01-02 如果用between会是 >   2020-01-01 and 2020-01-02<
                Object[] objs = this.convert2ObjectArray(queryField.getValue());
                if (objs != null && objs.length > 0) {
                    Assert.isTrue(objs.length == 2, String.format("查询条件为between时，查询值必须为两个，但是传入的查询值为：%s", objs));
                    queryWrapper.ge(field, objs[0]);
                    queryWrapper.le(field, objs[1]);
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

    /**
     * 参数校验
     *
     * @param str ep: "or 1=1"
     */
    public static boolean isSqlValid(String str) {
        Matcher matcher = sqlPattern.matcher(str);
        if (matcher.find()) {
            //获取非法字符：or
            log.info("参数存在非法字符，请确认：" + matcher.group());
            return false;
        }
        return true;
    }

    /**
     * 可序列化
     */
    private static final int FLAG_SERIALIZABLE = 1;

    private static Map<String, SFunction> functionMap = new HashMap<>();

    public static SFunction getSFunction(Class<?> entityClass, String fieldName) {
        if (functionMap.containsKey(entityClass.getName() + fieldName)) {
            return functionMap.get(entityClass.getName() + fieldName);
        }
        Field field = getDeclaredField(entityClass, fieldName);
        if (field == null) {
            throw ExceptionUtils.mpe("This class %s is not have field %s ", entityClass.getName(), fieldName);
        }
        SFunction func = null;
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(field.getType(), entityClass);
        final CallSite site;
        String getFunName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            site = LambdaMetafactory.altMetafactory(lookup,
                    "invoke",
                    MethodType.methodType(SFunction.class),
                    methodType,
                    lookup.findVirtual(entityClass, getFunName, MethodType.methodType(field.getType())),
                    methodType, FLAG_SERIALIZABLE);
            func = (SFunction) site.getTarget().invokeExact();
            functionMap.put(entityClass.getName() + field, func);
            return func;
        } catch (Throwable e) {
            throw ExceptionUtils.mpe("This class %s is not have method %s ", entityClass.getName(), getFunName);
        }
    }
}