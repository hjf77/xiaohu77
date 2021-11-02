package com.fhs.app.test;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.valid.group.Add;
import com.github.liangbaika.validate.annations.AbcValidate;
import com.github.liangbaika.validate.enums.Check;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OneData extends BasePO<OneData> {
    //自定义的时候 fun=Custom；express= bean名字
    @AbcValidate(required = true, message = "姓名不可为空")
    @TableId
    private String name;


    @NotNull(groups = Add.class)
    private Integer age;


    @AbcValidate(required = true, fun = Check.le, express = "1")
    private Integer sex;
}
