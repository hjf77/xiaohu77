package com.fhs.generate.vo;


import com.fhs.generate.po.SystemTableGenerateConfigPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 表生成代码配置(SystemTableGenerateConfig)实体类
 *
 * @author wanglei
 * @since 2022-09-07 14:50:23
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="表生成代码配置",description ="表生成代码配置")
public class SystemTableGenerateConfigVO extends SystemTableGenerateConfigPO implements VO {
    
 }
    
