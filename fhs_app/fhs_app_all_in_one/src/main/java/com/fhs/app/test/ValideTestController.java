package com.fhs.app.test;

import com.fhs.core.result.HttpResult;
import com.fhs.core.valid.group.Add;
import com.github.liangbaika.validate.annations.ValidateParam;
import com.github.liangbaika.validate.annations.ValidateParams;
import com.github.liangbaika.validate.enums.Check;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v")
public class ValideTestController {
    /**
     * 单字段简单验证
     *
     * @param name
     * @return
     */
    @GetMapping("test0")
    @ValidateParam(value = Check.Length, argName = "name", express = "2,6",msg="name不可为空")
    public Object test0(String name) {
        return Boolean.TRUE;
    }

    /**
     * 多字段验证
     * 建议 如果字段过多（超过三个）使用自定义方式 更简洁
     * @param name
     * @param age
     * @param idcard
     * @return
     */
    @ValidateParams(
            value = {
                    @ValidateParam(value = Check.NotEmpty, argName = "name",msg = "姓名不可为空"),
                    @ValidateParam(value = Check.Number, argName = "age",msg = "年龄必须为数字"),
                    @ValidateParam(value = Check.isIDCard, argName = "idcard",msg = "idcard 必须为身份证格式"),
            }
    )
    @GetMapping("test1")
    public Object test1(String name, Integer age, Integer idcard) {
        return Boolean.TRUE;
    }

    /**
     * 实体验证 javax-validation验证方式
     *
     * @param oneData
     * @return
     */
    @PostMapping("test2")
    public Object test2(@RequestBody @Valid OneData oneData) {
        return oneData;
    }


    /**
     * 对象多级验证，混合验证
     * 0.7 版本开始支持无限级 0.7版本之前的只支持2级
     * 0.8.8开始支持重复注解
     * @param oneData
     * @return
     */
    @PostMapping("test3")
    @ValidateParam(value = Check.NotEmpty, argName = "oneData.name")
    @ValidateParam(value = Check.Number, argName = "oneData.age")
    public Object test3(@RequestBody @Validated(Add.class) OneData oneData) {
        return oneData;
    }


    /**
     * 自定义验证
     *
     * @param oneData
     * @return
     */
    @PostMapping("test4")
    @ValidateParam(value = Check.Custom, argName = "oneData.name", express = "nameValidater")
    public Object test4(@RequestBody OneData oneData) {
        return oneData;
    }

    @GetMapping("/test6")
    @ValidateParam(required = false, value = Check.NotNull, argName = "age")
    public HttpResult test6(String name, Integer age) {
        return HttpResult.success();
    }

    @GetMapping("/test8")
    @ValidateParams(
            value = {
                    @ValidateParam(value = Check.NotNull, argName = "name"),
                    @ValidateParam(required = false, value = Check.NotNull, argName = "age")
            },
            anded = false
    )
    public HttpResult test8(String name, Integer age) {
        return HttpResult.success();
    }
}
