package com.fhs.generate.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fhs.generate.vo.*;

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
     * 格式化form的json
     * @param tableInfoVO
     * @return
     */
    FormSettVO getFormJson(TableInfoVO tableInfoVO);

    /**
     * 生成代码
     *
     * @param generateCodeVO 表信息
     * @return 返回vue代码的路径
     */
    String generateCode(GenerateCodeVO generateCodeVO);

    /**
     * 注册过滤条件
     *
     * @param parser 格式化方法实现
     * @param types   类型
     */
    void regFilterParser(SFunction<ListFieldVO, ListSettVO.Filter> parser, String... types);

    /**
     * 注册表单处理
     * @param parser 格式化方法实现
     * @param types 类型
     */
    void regControlParser(SFunction<FormFiledVO, FormSettVO.Control> parser, String... types);
}
