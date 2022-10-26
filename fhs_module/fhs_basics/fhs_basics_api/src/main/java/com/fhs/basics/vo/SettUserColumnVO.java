package com.fhs.basics.vo;


import com.fhs.basics.po.SettUserColumnPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户自定义列信息配置(SettUserColumn)实体类
 *
 * @author wanglei
 * @since 2022-10-13 11:46:49
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户自定义列信息配置", description = "用户自定义列信息配置")
public class SettUserColumnVO extends SettUserColumnPO implements VO {

}
    
