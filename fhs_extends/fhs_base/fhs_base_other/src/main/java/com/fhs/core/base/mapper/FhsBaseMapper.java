package com.fhs.core.base.mapper;

import com.mybatis.jpa.mapper.MybatisBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 注意不要优先使用本接口自己定义的方法
 * 优先使用MybatisBaseMapper 里面的方法
 * 因为里面的方法不需要自己写xml 写sql去实现
 *
 * @author wanglei
 * @version [版本号, 2015年5月27日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Mapper
public interface FhsBaseMapper<D> extends MybatisBaseMapper<D> {

}
