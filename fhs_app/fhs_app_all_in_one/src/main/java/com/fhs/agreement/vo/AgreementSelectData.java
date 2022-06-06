package com.fhs.agreement.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel("编辑的时候数据下拉数据回显")
public class AgreementSelectData {

    @ApiModelProperty("商品规格")
    private List<Goodspecification> specificationId;

    @Data
    public static class Goodspecification{
        private int id;
        private String title;
    }
}
