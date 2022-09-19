package com.fhs.generate.component;

import com.fhs.common.utils.StringUtil;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.generate.service.GenerateCodeService;
import com.fhs.generate.vo.FormFiledVO;
import com.fhs.generate.vo.FormSettVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FormControlParser implements InitializingBean {

    private static final Set<String> INPUT_COMPONENT = new HashSet<>(Arrays.asList("input", "fc-editor", "inputNumber"));


    @Lazy
    @Autowired
    private GenerateCodeService generateCodeService;

    public FormSettVO.Control parseInput(FormFiledVO formFiledVO) {
        FormSettVO.Control control = new FormSettVO.Control();
        String type = formFiledVO.getProps().getString("type");
        ParamChecker.isNotNull(type,"输入框的类型必须要选");
        switch (type) {
            case "text":
                control.setType("text");
                break;
            case "textarea":
                control.setType("textarea");
                break;
            case "password":
                control.setType("password");
                break;
        }
        return control;
    }

    public FormSettVO.Control parseUpload(FormFiledVO formFiledVO) {
        FormSettVO.Control control = new FormSettVO.Control();
        String type = formFiledVO.getProps().getString("uploadType");
        ParamChecker.isNotNull(type,"上传组件的类型必须要选");
        switch (type) {
            case "uploadFileAsync":
                control.setType("uploadFileAsync");
                break;
            case "uploadFile":
                control.setType("uploadFile");
                break;
            case "uploadPicture":
                control.setType("uploadPicture");
                break;
        }
        return control;
    }




    /**
     * 格式化校验规则
     *
     * @param formFiledVO 表单字段
     * @return
     */
    public List<FormSettVO.Rule> parseRules(FormFiledVO formFiledVO) {
        List<FormSettVO.Rule> rules = new ArrayList<>();
        boolean isInput = INPUT_COMPONENT.contains(formFiledVO.getType());
        String trigger = isInput ? "blur" : "change";
        if (formFiledVO.getRequired() != null && formFiledVO.getRequired()) {
            //必填处理
            rules.add(FormSettVO.Rule.builder().required(true).message("请" + (isInput ? "输入" : "选择") + formFiledVO.getTitle())
                    .trigger(trigger).type(formFiledVO.getProps().getBooleanValue("multiple") ? "array" : null).build());
        }
        if (formFiledVO.getValidate() != null) {
            for (FormFiledVO.Validate validate : formFiledVO.getValidate()) {
                //内置规则
                if (!StringUtil.isEmpty(validate.getRuleCode())) {
                    rules.add(FormSettVO.Rule.builder().ruleCode(validate.getRuleCode()).trigger(trigger).build());
                    continue;
                }
                //自定义正则
                if (!StringUtil.isEmpty(validate.getPattern())) {
                    rules.add(FormSettVO.Rule.builder().pattern(validate.getPattern()).trigger(trigger)
                            .message(StringUtil.isEmpty(validate.getMessage()) ? (formFiledVO.getTitle() + "格式错误") : validate.getMessage()).build());
                    continue;
                }
                if (validate.getMax() != null || validate.getMin() != null) {
                    rules.add(FormSettVO.Rule.builder().trigger(trigger).max(validate.getMax()).min(validate.getMin())
                            .message(StringUtil.isEmpty(validate.getMessage()) ? (formFiledVO.getTitle() + "格式错误") : validate.getMessage()).build());
                    continue;
                }
            }
        }
        return rules;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        generateCodeService.regControlParser(this::parseInput, "input");
        generateCodeService.regControlParser(this::parseUpload, "upload");

    }
}
