package com.fhs.basics.service.impl;


import com.fhs.basics.constant.ExceptionConstant;
import com.fhs.basics.mapper.CommonLanguageMapper;
import com.fhs.basics.po.CommonLanguagePO;
import com.fhs.basics.service.CommonLanguageService;
import com.fhs.basics.vo.CommonLanguageVO;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.exception.BusinessException;
import com.fhs.core.exception.ParamException;
import com.github.liaochong.myexcel.core.SaxExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 语言设置表(CommonLanguage)表服务实现类
 *
 * @author LiYuLin
 * @since 2022-09-13 15:05:09
 */
@Service
@Namespace("common_language")
public class CommonLanguageServiceImpl extends BaseServiceImpl<CommonLanguageVO, CommonLanguagePO> implements CommonLanguageService {

    @Autowired
    private CommonLanguageMapper commonLanguageMapper;

    @Override
    @Transactional
    public List<String> importCommonLanguageInfo(MultipartFile multipartFile, Long userId) {
        List<String> msgList = new ArrayList<>();
        List<CommonLanguagePO> commonLanguagePOS = null;
        try {
            commonLanguagePOS = SaxExcelReader.of(CommonLanguagePO.class)
                    .sheet(0)
                    .rowFilter(row -> row.getRowNum() > 0)
                    .ignoreBlankRow()
                    .read(multipartFile.getInputStream());
            commonLanguageMapper.deleteAll();
            commonLanguagePOS.forEach(c -> {
                c.preInsert(userId);
            });
            this.batchInsert(commonLanguagePOS);
        } catch (IOException e) {
            throw new BusinessException(ExceptionConstant.EXCEL_FORMAT_ERROR);
        }
        msgList.add("导入成功");
        return msgList;
    }

    @Override
    public int selectByCommonLanguageVO(CommonLanguageVO commonLanguageVO) {
        return commonLanguageMapper.selectByCommonLanguageVO(commonLanguageVO);
    }
}
