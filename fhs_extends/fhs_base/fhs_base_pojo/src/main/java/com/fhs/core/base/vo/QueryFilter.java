package com.fhs.core.base.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.common.utils.*;
import com.fhs.core.base.anno.SafeField;
import com.github.liangbaika.validate.exception.ParamsInValidException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

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


    /**
     * 类似bean seacher的高级查询语法支持
     *
     * @param currentModelClass
     * @param <Z>
     * @return
     */
    public static <Z> QueryWrapper<Z> reqParam2Wrapper(Class<Z> currentModelClass) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> paramMap = new HashMap<>();
        Map<String, String[]> tempMap = request.getParameterMap();
        for (String key : tempMap.keySet()) {
            paramMap.put(key, request.getParameter(key));
        }
        // 获取所有字段
        List<String> fieldNames = ReflectUtils.getAllField(currentModelClass).stream().map(Field::getName).collect(Collectors.toList());
        QueryFilter<Z> queryFilter = new QueryFilter<>();
        for (String fieldName : fieldNames) {
            //有值并且不为空的才处理
            if (paramMap.containsKey(fieldName) && !StringUtils.isEmpty(ConverterUtils.toString(paramMap.get(fieldName)))) {
                QueryField queryField = new QueryField();
                //指定了运算符则使用指定的运算符，没有指定则使用=
                if (paramMap.containsKey(fieldName + "-op")) {
                    queryField.setOperation(ConverterUtils.toString(paramMap.get(fieldName + "-op")));
                } else {
                    queryField.setOperation("=");
                }
                queryField.setProperty(fieldName);
                queryField.setValue(paramMap.get(fieldName));
                queryFilter.getQuerys().add(queryField);
            }
        }
        //处理is null和 not_null
        for (Map.Entry<String, String> paramEntry : paramMap.entrySet()) {
            if ("is_null".equals(paramEntry.getValue()) || "not_null".equals(paramEntry.getValue())) {
                QueryField queryField = new QueryField();
                queryField.setOperation(ConverterUtils.toString(paramEntry.getValue()));
                queryField.setProperty(paramEntry.getKey().replace("-op", ""));
                queryFilter.getQuerys().add(queryField);
            }
        }
        return queryFilter.asWrapper(currentModelClass);
    }

    public QueryWrapper<T> asWrapper(Class currentModelClass, String... safeFields) {
        if (safeFields != null) {
            safeFieldsSet = new HashSet<>(Arrays.asList(safeFields));
        }
        List<Field> safeFieldList = ReflectUtils.getAnnotationField(currentModelClass, SafeField.class);
        if (!safeFieldList.isEmpty()) {
            safeFieldsSet.addAll(safeFieldList.stream().map(Field::getName).collect(Collectors.toList()));
        }
        QueryWrapper<T> queryWrapper = new QueryWrapper();
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
                        this.convertQueryField(x, l, currentModelClass);
                    });
                });
            } else {
                if (!OR.equals(groupRelation)) {
                    throw new ParamsInValidException("关联条件只能传AND 或者OR");
                }
                queryWrapper.or((x) -> {
                    newListFields.forEach((l) -> {
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

    private List<String> convertSortFieldList(List<FieldSort> list, Class<T> currentModelClass) {
        if (list == null) {
            return null;
        } else {
            List<String> result = new ArrayList<>();
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
    public String getField(String fieldName, Class<T> currentModelClass) {
        if (currentModelClass == null) {
            throw new ParamsInValidException("currentModelClass不能为null");
        }
        Map<String, ColumnCache> cacheMap = LambdaUtils.getColumnMap(currentModelClass);
        if (cacheMap == null) {
            throw new ParamsInValidException(currentModelClass + "找不到表字段对应关系，请检查mapper.xml是否已经存在并且目录正确");
        }
        if (!cacheMap.containsKey(fieldName.toUpperCase())) {
            throw new ParamsInValidException(fieldName + "不正确");
        }
        return cacheMap.get(fieldName.toUpperCase()).getColumn();
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
    private void convertQueryField(QueryWrapper<T> queryWrapper, QueryField queryField, Class<T> currentModelClass) {
        String r = queryField.getRelation();
        if (OR.equals(r)) {
            queryWrapper.or();
        }
        //如果是空或者null 字符串则不加此过滤条件
        if (CheckUtils.isNullOrEmpty(queryField.getValue()) || "null".equals(ConverterUtils.toString(queryField.getValue()))) {
            return;
        }
        String field = null;
        if (!StringUtils.isEmpty(queryField.getTarget())) {
            if (StringUtils.isEmpty(queryField.getField())) {
                throw new ParamsInValidException("当target不为空的时候field也一定不可以为空，字段:" + queryField.getProperty());
            }

            if (!OPEARTOR_SET.contains(queryField.getOperation())) {
                throw new ParamsInValidException("操作符不受支持:" + queryField.getOperation());
            }
            if (!isSqlValid(queryField.getValue() + "")) {
                throw new ParamsInValidException("字段值校验出SQL注入风险:" + queryField.getValue());
            }

            field = getField(queryField.getField(), currentModelClass);

            Object propValue = queryField.getValue();
            if ("like".equals(queryField.getOperation())) {
                propValue = "%" + propValue + "%";
            }
            //不是数字的时候加引号
            if (!CheckUtils.isNumber(propValue)) {
                propValue = "'" + propValue + "'";
            }
            //目标标字段
            String targetField = getField(queryField.getProperty(), getTargetClass(queryField.getTarget()));
            String sql = field + " in (select " + getTargetKeyColumn(queryField.getTarget()) + " from " + getTargetTableName(queryField.getTarget()) + " where " + targetField
                    + " " + queryField.getOperation() + " " + propValue + ")";
            queryWrapper.apply(sql);
            return;
        }
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

    /**
     * 处理特殊符号
     *
     * @param str
     * @return
     */
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

    public Map<String, Object> queryFieldsMap() {
        if (this.querys == null) {
            return new HashMap<>(0);
        }
        return this.querys.stream().collect(Collectors.toMap(QueryField::getProperty, QueryField::getValue));
    }
}
