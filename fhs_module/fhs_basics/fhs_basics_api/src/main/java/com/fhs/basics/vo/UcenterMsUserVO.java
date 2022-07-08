package com.fhs.basics.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fhs.common.utils.StringUtils;
import com.fhs.core.trans.vo.VO;
import com.fhs.basics.po.UcenterMsUserPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Map;

/**
 * 用户vo
 *
 * @author user
 * @date 2020-05-18 15:49:03
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UcenterMsUserVO", description = "UcenterMsUserVO参数")
public class UcenterMsUserVO extends UcenterMsUserPO implements VO {

    @ApiModelProperty("组织机构对应的公司id")
    private String companyId;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("部门名称")
    private String orgName;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("顶级单位")
    private String company;

    @ApiModelProperty("顶级单位的下一级单位")
    private String oilpro;

    @ApiModelProperty("三级单位")
    private String opeArea;

    @ApiModelProperty("四级单位")
    private String centralStat;

    @ApiModelProperty(value = "地图图层")
    private String mapLayer;

    private String xmin;

    private String ymin;

    private String xmax;

    private String ymax;

    @ApiModelProperty(value = "地图中心点")
    private String center;

    @ApiModelProperty(value = "地图放大级别")
    private String zoom;

    @ApiModelProperty(value = "用户在线时长")
    private Integer minuteTime;

    @ApiModelProperty(value = "用户在线时长 02h58")
    private String hourMinuteTime;
}
