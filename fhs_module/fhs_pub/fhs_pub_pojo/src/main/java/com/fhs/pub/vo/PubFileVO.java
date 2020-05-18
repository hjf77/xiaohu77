package com.fhs.pub.vo;

import com.fhs.core.base.pojo.vo.VO;
import com.fhs.pub.dox.PubFileDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *  公共文件
 * @author user
 * @since 2019-05-18 11:20:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PubFileVO extends PubFileDO implements VO {

}
