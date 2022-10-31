package com.fhs.generate.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.config.EConfig;
import com.fhs.core.exception.ParamException;
import com.fhs.generate.constant.GenerateConstant;
import com.fhs.generate.mapper.TableInfoMapper;
import com.fhs.generate.po.SystemTableGenerateConfigPO;
import com.fhs.generate.po.TableInfoPO;
import com.fhs.generate.service.SystemTableGenerateConfigService;
import com.fhs.generate.service.TableInfoService;
import com.fhs.generate.vo.ListFieldVO;
import com.fhs.generate.vo.TableInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

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

    @Autowired
    private SystemTableGenerateConfigService tableGenerateConfigService;

    @Override
    public TableInfoVO getTableInfo(String tableSchema, String tableName, String configType) {
        boolean isDev = ConverterUtils.toBoolean(EConfig.getOtherConfigPropertiesValue("isDevModel"));
        if (!isDev) {
            throw new ParamException("代码生成器仅仅开发模式可用，请配置isDevModel为true");
        }
        List<ListFieldVO> fieldList = null;
        try {
            fieldList = tableInfoMapper.getTableFields(tableSchema, tableName);
        } catch (Exception e) {
            log.error("获取表信息错误");
            if (e instanceof BadSqlGrammarException) {
                throw new ParamException("表不存在哥哥,库名字或者表名子写错了把！");
            }
            throw e;
        }

        fieldList = fieldList.stream().filter(fieldsVO -> {
            return (!IGNORE_SET.contains(fieldsVO.getName()));
        }).collect(Collectors.toList());
        SystemTableGenerateConfigPO tableConfig = tableGenerateConfigService.selectBean(SystemTableGenerateConfigPO.builder().dbName(tableSchema)
                .tableName(tableName).build());
        TableInfoVO tableInfoVO = new TableInfoVO();
        //列配置的话，把老数据拿出来
        if (GenerateConstant.CONFIG_TYPE_LIST_COLUMN.equals(configType)) {
            if (tableConfig != null) {
                if(tableConfig.getTableOperation()!=null){
                    tableInfoVO.setTableOperation(tableConfig.getTableOperation());
                }
                tableInfoVO.setFormConfig(tableConfig.getFormConfig());
                if (tableConfig.getListColumnSett() != null) {
                    List<ListFieldVO> oldSetts = JSONObject.parseArray(tableConfig.getListColumnSett(), ListFieldVO.class);
                    Map<String, ListFieldVO> fieldsVOMap = oldSetts.stream().collect(Collectors.toMap(e -> e.getName(), v -> v));
                    for (ListFieldVO fieldsVO : fieldList) {
                        if (fieldsVOMap.containsKey(fieldsVO.getName())) {
                            ListFieldVO old = fieldsVOMap.get(fieldsVO.getName());
                            fieldsVO.setIsListShow(old.getIsListShow());
                            fieldsVO.setIsDict(old.getIsDict());
                            fieldsVO.setDictCode(old.getDictCode());
                            fieldsVO.setElementType(old.getElementType());
                            fieldsVO.setLabel(old.getLabel());
                        }
                    }
                }
            }
        }

        tableInfoVO.setTableComment(tableInfoMapper.getTableComment(tableSchema, tableName));
        tableInfoVO.setTableSchema(tableSchema);
        tableInfoVO.setTableName(tableName);
        tableInfoVO.setFields(fieldList.toArray(new ListFieldVO[fieldList.size()]));
        return tableInfoVO;
    }


    @Override
    public List<TableInfoVO> findForList(TableInfoPO bean) {
        List<TableInfoPO> dos = baseMapper.selectList(bean.asWrapper());
        return pos2vos(dos);
    }
}
