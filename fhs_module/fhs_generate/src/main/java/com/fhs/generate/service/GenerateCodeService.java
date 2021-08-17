package com.fhs.generate.service;

import com.fhs.generate.vo.TableInfoVO;

/**
 * 代码生成服务
 */
public interface GenerateCodeService {

    /**
     * 生成代码
     * @param tableInfoVO 表信息
     * @return 返回vue代码的路径
     */
    String[] generateCode(TableInfoVO tableInfoVO);
}
