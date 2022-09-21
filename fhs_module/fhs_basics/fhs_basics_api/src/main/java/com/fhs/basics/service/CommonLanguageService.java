package com.fhs.basics.service;


import com.fhs.basics.po.CommonLanguagePO;
import com.fhs.basics.vo.CommonLanguageVO;
import com.fhs.core.base.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 语言设置表(CommonLanguage)}表服务接口
 *
 * @author LiYuLin
 * @since 2022-09-13 15:05:44
 */
public interface CommonLanguageService extends BaseService<CommonLanguageVO, CommonLanguagePO> {


    List<String> importCommonLanguageInfo(MultipartFile multipartFile, Long userId);

    int selectByCommonLanguageVO(CommonLanguageVO commonLanguageVO);
}
