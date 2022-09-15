package com.fhs.generate.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fhs.generate.vo.ListFieldVO;
import com.fhs.generate.vo.ListSettVO;
import com.fhs.generate.vo.TableInfoVO;

/**
 * 代码生成服务
 */
public interface GenerateCodeService {

    /**
     * 获取列表json
     * @param tableInfoVO
     * @return
     */
    ListSettVO getListJson(TableInfoVO tableInfoVO);

    /**
     * 生成代码
     *
     * @param tableInfoVO 表信息
     * @return 返回vue代码的路径
     */
    String[] generateCode(TableInfoVO tableInfoVO);

    /**
     * 注册过滤条件
     *
     * @param parser 格式化方法实现
     * @param type   类型
     */
    void regFilterParser(SFunction<ListFieldVO, ListSettVO.Filter> parser, String... types);
}
