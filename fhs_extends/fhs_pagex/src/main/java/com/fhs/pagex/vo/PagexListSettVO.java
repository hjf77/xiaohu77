package com.fhs.pagex.vo;

import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.config.EConfig;
import com.fhs.core.trans.anno.Trans;
import com.mybatis.jpa.common.ColumnNameUtil;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Data;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * pagex 列表页面DTO
 * 本DTO是一个DOMAIN 里面包含部分业务逻辑
 *
 * @ProjectName: framework_v2_idea2
 * @Package: com.fhs.pagex.dto
 * @ClassName: PagexListSettDTO
 * @Author: JackWang
 * @CreateDate: 2018/11/30 0030 20:09
 * @UpdateUser: JackWang
 * @UpdateDate: 2018/11/30 0030 20:09
 * @Version: 1.0
 */
@Data
public class PagexListSettVO extends PagexBaseVO {

    /**
     * 翻译简写
     */
    private static Map<String, String> transAbbreviationMap = new HashMap<>();

    static {
        transAbbreviationMap.put("book", "wordbook");
        transAbbreviationMap.put("user", "sysUser");
        transAbbreviationMap.put("type", "classifyInfo");
        transAbbreviationMap.put("fuser", "ucenter:frontUser");
    }

    public static final Set<String> DEFAULT_FIELD_SET = new HashSet<>();

    static {
        DEFAULT_FIELD_SET.add("create_user");
        DEFAULT_FIELD_SET.add("update_user");
        DEFAULT_FIELD_SET.add("create_time");
        DEFAULT_FIELD_SET.add("update_time");
    }


    /**
     * 列表页面对象
     */
    private ScriptObjectMirror listPageObject;

    /**
     * 列表页面字段配置
     */
    private List<Map<String, Object>> listSett;

    /**
     * 过滤条件
     */
    private List<Map<String, Object>> filters;

    /**
     * 禁用掉的默认给提供的按钮集合
     * 默认会提供export,add,del,update,view,search 6个按钮
     */
    private List<String> disableButtons;

    /**
     * 扩展css
     */
    private List<String> excss;

    /**
     * 扩展js
     */
    private List<String> exjs;

    /**
     * 自定义的按钮，key为按钮名称 value为按钮点击时候需要调用的方法
     * 其中按钮方法以 R结尾 比如 viewR 会获取列表选中行的对象
     * 不以R结尾则是普通按钮
     */
    private List<Map<String, Object>> buttons;

    private Class poClass;

    /**
     * 系统默认支持的一些参数
     */
    private static Set<String> DEFAULT_KEY = new HashSet<>();

    static {
        DEFAULT_KEY.add("showField");
        DEFAULT_KEY.add("formart");
        DEFAULT_KEY.add("title");
        DEFAULT_KEY.add("trans");
        DEFAULT_KEY.add("key");
        DEFAULT_KEY.add("name");
        DEFAULT_KEY.add("isJoin");
        DEFAULT_KEY.add("namespace");
    }

    /**
     * 是的话就把删除，查看，编辑三个按钮放到最后一列上
     */
    private boolean isColumnButton;

    /**
     * 解析js 返回对象
     *
     * @param js js
     * @throws NoSuchMethodException 如果调用某些方法找不到
     * @throws ScriptException       脚本本身有问题
     */
    public PagexListSettVO(String js) throws NoSuchMethodException, ScriptException {
        super.initScriptEngine(js);
        listPageObject = (ScriptObjectMirror) scriptEngine.get("listPage");
        this.initButtons();
        this.initDisableButton();
        this.initFilter();
        this.initListSett();
        this.initOtherFunction();
        this.initIsColumnButton();
        this.initModelConfig();
        this.initExCSS();
        this.initExJS();
    }

    public void initExCSS() {
        Object excssStr = modelConfig.get("excss");
        if (!CheckUtils.isNullOrEmpty(excssStr)) {
            excss = Arrays.asList(excssStr.toString().split(","));
        }
    }

    public void initExJS() {
        Object exjsStr = modelConfig.get("exjs");
        if (!CheckUtils.isNullOrEmpty(exjsStr)) {
            exjs = Arrays.asList(exjsStr.toString().split(","));
        }
    }


    /**
     * 初始化列表字段设置
     *
     * @throws NoSuchMethodException
     * @throws ScriptException
     */
    public void initListSett() throws NoSuchMethodException, ScriptException {
        listSett = getListM("listFieldSett", listPageObject);
        String showField = null;
        for (Map<String, Object> field : listSett) {
            StringBuilder otherAttr = new StringBuilder();
            for (String key : field.keySet()) {
                if (!DEFAULT_KEY.contains(key)) {
                    otherAttr.append(key + ":'" + field.get(key) + "',");
                }
            }
            field.put("otherAttr", otherAttr.toString());
            field.put("camelName", ColumnNameUtil.underlineToCamel(ConverterUtils.toString(field.get("name"))));
            if (field.containsKey("showField")) {
                showField = ConverterUtils.toString(field.get("showField"));
                showField = "transMap." + ColumnNameUtil.underlineToCamel(showField.replace("transMap.", ""));
                field.put("showField", showField);
            }

        }
    }


    /**
     * 初始化过滤字段设置
     *
     * @throws NoSuchMethodException
     * @throws ScriptException
     */
    public void initFilter() throws NoSuchMethodException, ScriptException {
        this.filters = getListM("filters", listPageObject);
    }

    /**
     * 初始化按钮设置
     *
     * @throws NoSuchMethodException
     * @throws ScriptException
     */
    public void initButtons() throws NoSuchMethodException, ScriptException {

        buttons = getListM("buttons", listPageObject);
        ;
        for (Map<String, Object> field : buttons) {
            field.put("camelName", ColumnNameUtil.underlineToCamel(ConverterUtils.toString(field.get("name"))));
        }
    }

    /**
     * 初始化哪些默认按钮禁用设置
     *
     * @throws NoSuchMethodException
     * @throws ScriptException
     */
    public void initDisableButton() throws NoSuchMethodException, ScriptException {
        this.disableButtons = getListS("disableButtons", listPageObject);
    }

    /**
     * 是否在列中添加操作按钮
     *
     * @throws NoSuchMethodException
     * @throws ScriptException
     */
    public void initIsColumnButton() throws NoSuchMethodException, ScriptException {
        this.isColumnButton = (Boolean) invocable.invokeMethod(listPageObject, "isColumnButton");
    }

    @Override
    Object getOtherFunctionJsObject() {
        return listPageObject;
    }

    /**
     * 将自己变成一个Java PO 的代码
     *
     * @return Java PO代码
     */
    public String formartJavaStr() {

        return "";
    }

    /**
     * 编译一个po的class
     *
     * @return
     */
    public synchronized Class<? extends BasePagexVO> getPoClass() {
        if (poClass != null) {
            return poClass;
        }
        String namespace = ConverterUtils.toString(this.getModelConfig().get("namespace"));
        String javaClassName = StringUtil.firstCharUpperCase(ColumnNameUtil.underlineToCamel(namespace));
        DynamicType.Builder dynamicBuilder = new ByteBuddy()
                //指定类名
                .with(new NamingStrategy.SuffixingRandom(javaClassName))
                //指定基类
                .subclass(BasePagexVO.class);
        Set<String> filedNameSet = new HashSet<>();
        filedNameSet.addAll(DEFAULT_FIELD_SET);
        filedNameSet.add(ConverterUtils.toString(this.getModelConfig().get("pkeyCamel")));
        dynamicBuilder =  dynamicBuilder.defineProperty(ConverterUtils.toString(ConverterUtils.toString(this.getModelConfig().get("pkeyCamel"))), String.class);
        //便利列表页面所有的行
        for (Map<String, Object> row : this.getListSett()) {
            if (filedNameSet.contains(row.get("name"))) {
                continue;
            }
            filedNameSet.add(ConverterUtils.toString(row.get("name")));
            AnnotationDescription apiModelPropertyDescription = null;
            //此列需要翻译
            if (row.containsKey("trans") && (!CheckUtils.isNullOrEmpty(row.get("trans")))) {
                String trans = ConverterUtils.toString(row.get("trans"));
                //如果已经默认集成了就不集成了，如果默认没集成，就给 trans 拼接
                if (transAbbreviationMap.containsKey(row.get("trans"))) {
                    trans = transAbbreviationMap.get(trans);
                }
                apiModelPropertyDescription = AnnotationDescription.Builder.ofType(Trans.class)
                        .define("type", trans)
                        .define("alias", ConverterUtils.toString(row.get("alias")))
                        .define("key", ConverterUtils.toString(row.get("key")))
                        .build();
                dynamicBuilder = dynamicBuilder.defineProperty(ConverterUtils.toString(row.get("camelName")), String.class)
                        .annotateField(apiModelPropertyDescription);
            } else {
                dynamicBuilder =  dynamicBuilder.defineProperty(ConverterUtils.toString(row.get("camelName")), String.class);
            }
        }
        poClass = saveAndLoad(dynamicBuilder.make());
        return poClass;
    }

    /**
     * 保存到本地目录，并且加载到类加载器中
     *
     * @param dynamicType
     */
    private Class saveAndLoad(DynamicType.Unloaded<?> dynamicType) {
        File path = new File(EConfig.getPathPropertiesValue("fileSavePath") + "/pagex/class");
        if (!path.exists()) {
            path.mkdirs();
        }
        //写入到本地目录
        try {
            dynamicType.saveIn(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dynamicType.load(Thread.currentThread().getContextClassLoader(),
                ClassLoadingStrategy.Default.INJECTION).getLoaded();
    }
}
