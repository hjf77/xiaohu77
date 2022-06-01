package com.fhs.supplier.vo;


import com.fhs.supplier.po.SupplierSupplierPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 供应商(SupplierSupplier)实体类
 *
 * @author liangxiaotao
 * @since 2022-05-31 14:14:57
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="供应商",description ="供应商")
public class SupplierSupplierVO extends SupplierSupplierPO implements VO {

 @ApiModelProperty(value = "地址")
 private Integer[]geographicalPosition;

 public Integer[] getGeographicalPosition() {
  this.geographicalPosition = new Integer[3];
  this.geographicalPosition[0] = this.getProvince();
  this.geographicalPosition[1] = this.getCity();
  this.geographicalPosition[2] = this.getArea();
  return geographicalPosition;
 }

 public void setGeographicalPosition(Integer[] geographicalPosition) {
  if(geographicalPosition == null || geographicalPosition.length == 0){
   return;
  }
  this.geographicalPosition = geographicalPosition;
  this.setProvince(this.geographicalPosition[0]);
  this.setCity(this.geographicalPosition[1]);
  this.setArea(this.geographicalPosition[2]);
 }
}
    
