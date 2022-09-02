package com.fhs.generate.service.impl;

import com.fhs.common.constant.Constant;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.config.EConfig;
import com.fhs.core.exception.ParamException;
import com.fhs.generate.mapper.TableInfoMapper;
import com.fhs.generate.po.TableInfoPO;
import com.fhs.generate.service.TableInfoService;
import com.fhs.generate.vo.FieldsVO;
import com.fhs.generate.vo.TableInfoVO;
import feign.Param;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.sql.SQLSyntaxErrorException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TableInfoServiceImpl extends BaseServiceImpl<TableInfoVO, TableInfoPO> implements TableInfoService {

    /**
     * 默认忽略集合
     */
    private final static Set<String> IGNORE_SET = new HashSet<>();


    static {
        IGNORE_SET.add("create_user");
        IGNORE_SET.add("create_time");
        IGNORE_SET.add("update_user");
        IGNORE_SET.add("update_time");
        IGNORE_SET.add("is_delete");
    }

    @Autowired
    private TableInfoMapper tableInfoMapper;

    @Override
    public TableInfoVO getTableInfo(String tableSchema, String tableName,String configType) {
        boolean isDev = ConverterUtils.toBoolean(EConfig.getOtherConfigPropertiesValue("isDevModel"));
        if (!isDev) {
            throw new ParamException("代码生成器仅仅开发模式可用，请配置isDevModel为true");
        }
        List<FieldsVO> fieldList = null;
        try{
            fieldList = tableInfoMapper.getTableFields(tableSchema, tableName);
        }catch (Exception e){
            log.error("获取表信息错误");
            if(e instanceof BadSqlGrammarException){
                throw new ParamException("表不存在哥哥,库名字或者表名子写错了把！");
            }
            throw e;
        }

        fieldList = fieldList.stream().filter(fieldsVO -> {
            return (!IGNORE_SET.contains(fieldsVO.getName()));
        }).collect(Collectors.toList());
        initPageElementType(fieldList);
        TableInfoVO tableInfoVO = new TableInfoVO();
        tableInfoVO.setTableComment(tableInfoMapper.getTableComment(tableSchema, tableName));
        tableInfoVO.setTableSchema(tableSchema);
        tableInfoVO.setTableName(tableName);
        tableInfoVO.setFields(fieldList.toArray(new FieldsVO[fieldList.size()]));
        return tableInfoVO;
    }


    /**
     * 根据字段类型设置默认的长度
     *
     * @param fieldList
     */
    private void initPageElementType(List<FieldsVO> fieldList) {
        for (FieldsVO fieldsVO : fieldList) {
            fieldsVO.setIsForm(Constant.INT_TRUE);
            //如果长度大于200的默认设置多行文本框
            if (fieldsVO.getLength() > 200) {
                fieldsVO.setPageElementType("textarea");
                //带日期的默认是日期
            } else if (fieldsVO.getType().contains("date")) {
                fieldsVO.setPageElementType("dates");
                fieldsVO.getExtParam().put("formatVal", "yyyy-MM-dd");
                //数字的默认设置为下拉框，因为字典之类的都是下拉框
            } else if (fieldsVO.getType().contains("int")) {
                fieldsVO.setPageElementType("select");
            } else {
                fieldsVO.setPageElementType("text");
            }
        }
    }

    @Override
    public List<TableInfoVO> findForList(TableInfoPO bean) {
        List<TableInfoPO> dos = baseMapper.selectList(bean.asWrapper());
        return pos2vos(dos);
    }
}
