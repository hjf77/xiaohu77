package com.fhs.basics.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.basics.api.anno.LogMethod;
import com.fhs.basics.constant.CommonMessageConstant;
import com.fhs.basics.constant.ExceptionConstant;
import com.fhs.basics.po.CommonLanguagePO;
import com.fhs.basics.service.CommonLanguageService;
import com.fhs.basics.vo.CommonLanguageExportVO;
import com.fhs.basics.vo.CommonLanguageVO;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Update;
import com.fhs.core.exception.BusinessException;
import com.fhs.core.exception.NotPremissionException;
import com.fhs.core.result.HttpResult;
import com.fhs.core.safe.repeat.anno.NotRepeat;
import com.fhs.module.base.controller.ModelSuperController;
import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.utils.AttachmentExportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 语言设置表(CommonLanguage)表控制层
 *
 * @author LiYuLin
 * @since 2022-09-13 15:05:09
 */

@RestController
@Api(tags = {"语言设置表"})
@RequestMapping("/language/commonLanguage")
public class CommonLanguageController extends ModelSuperController<CommonLanguageVO, CommonLanguagePO, Long> {
    @Autowired
    private CommonLanguageService commonLanguageService;

    @ApiOperation("语言信息设置导入")
    @PostMapping(value = "/importCommonLanguageInfo")
    public List<String> importCommonLanguageInfo(@RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new BusinessException(ExceptionConstant.FILE_NOT_EMPTY_CODE);
        }
        return commonLanguageService.importCommonLanguageInfo(multipartFile, this.getSessionuser().getUserId());
    }

    @ApiOperation("获取所有语言")
    @PostMapping(value = "/groupByCommonLanguage")
    public Map<String, Map<String, String>> groupByCommonLanguage() {
        List<CommonLanguageVO> commonLanguageVOS = commonLanguageService.select();
        Map<String, Map<String, String>> result = new HashMap<>();
        result.put("en", new HashMap<>());
        result.put("zh", new HashMap<>());
        result.put("ar", new HashMap<>());
        for (CommonLanguageVO commonLanguageVO : commonLanguageVOS) {
            result.get("en").put(commonLanguageVO.getName(), commonLanguageVO.getValuesEn());
            result.get("zh").put(commonLanguageVO.getName(), commonLanguageVO.getValuesZh());
            result.get("ar").put(commonLanguageVO.getName(), commonLanguageVO.getValuesAr());
        }
        return result;
    }

    @ApiOperation("语言信息导出")
    @PostMapping(value = "/exportCommonLanguage")
    public void exportCommonLanguage(HttpServletResponse response) throws Exception {
        if (this.isPermitted("add")) {
            List<CommonLanguageVO> commonLanguageVOS = commonLanguageService.selectListMP(new LambdaQueryWrapper<CommonLanguagePO>().orderByDesc(CommonLanguagePO::getCreateTime));
            List<CommonLanguageExportVO> commonLanguageExportVOS = new ArrayList<>();
            commonLanguageVOS.forEach(c -> {
                CommonLanguageExportVO commonLanguageExportVO = new CommonLanguageExportVO();
                commonLanguageExportVO.setIndex(commonLanguageExportVOS.size() + 1);
                BeanUtils.copyProperties(c, commonLanguageExportVO);
                commonLanguageExportVOS.add(commonLanguageExportVO);
            });
            //导出
            String languageTag = commonLanguageService.getLanguageTag();
            try (Workbook workbook = DefaultExcelBuilder.of(CommonLanguageExportVO.class)
                    .build(commonLanguageExportVOS)) {
                AttachmentExportUtil.export(workbook, "语言信息一览表_en", response);
                if (languageTag.equals(CommonMessageConstant.languag_zh_CN)) {
                    AttachmentExportUtil.export(workbook, "语言信息一览表", response);
                } else if (languageTag.equals(CommonMessageConstant.languag_ar)) {
                    AttachmentExportUtil.export(workbook, "语言信息一览表_ar", response);
                }

            } catch (IOException e) {
                throw new BusinessException(ExceptionConstant.EXPORT_FAIL);
            }
        } else {
            throw new NotPremissionException();
        }
    }


    @Override
    @NotRepeat
    @ResponseBody
    @PostMapping
    @ApiOperation("新增")
    @LogMethod(type = 0, voParamIndex = 0)
    public HttpResult<Boolean> add(@RequestBody @Validated({Add.class}) CommonLanguageVO commonLanguageVO) {
        if (commonLanguageVO instanceof BasePO) {
            int count = commonLanguageService.selectByCommonLanguageVO(commonLanguageVO);
            if (count > 0) {
                throw new BusinessException(ExceptionConstant.LANGUAGE_MESSAGE_NOT_REPEAT);
            }
            commonLanguageVO.preInsert(this.getSessionuser().getUserId());
            this.baseService.insert(commonLanguageVO);
            return HttpResult.success(true);
        } else {
            throw new NotPremissionException();
        }
    }

    @ApiOperation("下载导入模板")
    @PostMapping(value = "/downLoadExcelTemplate")
    public void downLoadExcelTemplate(HttpServletResponse response) {
        try {
            String languageTag = commonLanguageService.getLanguageTag();
            String fileName = "语言信息一览表_en.xlsx";
            if (languageTag.equals(CommonMessageConstant.languag_zh_CN)) {
                fileName = "语言信息一览表.xlsx";
            } else if (languageTag.equals(CommonMessageConstant.languag_ar)) {
                fileName = "语言信息一览表_ar.xlsx";
            }
            response.setHeader("Content-disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            String filePath = "/mapper/template/" + fileName;
            filePath = URLDecoder.decode(filePath, "UTF-8");
            InputStream inputStream = this.getClass().getResourceAsStream(filePath);
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = inputStream.read(b)) != -1) {
                out.write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception ex) {
            log.error("获取路径错误:", ex);
        }

    }


    @Override
    @ResponseBody
    @PutMapping
    @ApiOperation("修改")
    @LogMethod(type = 1, voParamIndex = 0)
    public HttpResult<Boolean> update(@RequestBody @Validated({Update.class}) CommonLanguageVO commonLanguageVO) {
        if (this.isPermitted("update")) {
            int count = commonLanguageService.selectByCommonLanguageVO(commonLanguageVO);
            if (count > 0) {
                throw new BusinessException(ExceptionConstant.LANGUAGE_MESSAGE_NOT_REPEAT);
            }
            this.beforSave(commonLanguageVO, false);
            this.baseService.updateById(commonLanguageVO);
            return HttpResult.success(true);
        } else {
            throw new NotPremissionException();
        }
    }
}
