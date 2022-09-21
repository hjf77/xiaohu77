package com.fhs.basics.mapper;


import com.fhs.basics.po.CommonLanguagePO;
import com.fhs.basics.vo.CommonLanguageVO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 语言设置表(CommonLanguage)表数据库访问层
 *
 * @author LiYuLin
 * @since 2022-09-13 15:05:09
 */
@Repository
public interface CommonLanguageMapper extends FhsBaseMapper<CommonLanguagePO> {

    void deleteAll();

    int selectByCommonLanguageVO(CommonLanguageVO commonLanguageVO);
}
