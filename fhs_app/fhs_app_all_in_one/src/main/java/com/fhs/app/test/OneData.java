package com.fhs.app.test;

import com.fhs.core.valid.group.Add;
import com.github.liangbaika.validate.annations.AbcValidate;
import com.github.liangbaika.validate.enums.Check;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OneData {
    //自定义的时候 fun=Custom；express= bean名字
    @AbcValidate(required = true, message = "姓名不可为空")
    private String name;


    @NotNull(groups = Add.class)
    private Integer age;


    @AbcValidate(required = true, fun = Check.le, express = "1")
    private Integer sex;
}
