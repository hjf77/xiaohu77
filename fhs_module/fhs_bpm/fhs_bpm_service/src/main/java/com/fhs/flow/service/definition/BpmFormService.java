package com.fhs.flow.service.definition;

import cn.hutool.core.collection.CollUtil;
import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.comon.utils.CollectionUtils;
import com.fhs.flow.controller.admin.definition.vo.form.BpmFormCreateReqVO;
import com.fhs.flow.controller.admin.definition.vo.form.BpmFormPageReqVO;
import com.fhs.flow.controller.admin.definition.vo.form.BpmFormUpdateReqVO;
import com.fhs.flow.dal.dataobject.definition.BpmFormPO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * 动态表单 Service 接口
 *
 * @author  @风里雾里
 */
public interface BpmFormService {

    /**
     * 创建动态表单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createForm(@Valid BpmFormCreateReqVO createReqVO);

    /**
     * 更新动态表单
     *
     * @param updateReqVO 更新信息
     */
    void updateForm(@Valid BpmFormUpdateReqVO updateReqVO);

    /**
     * 删除动态表单
     *
     * @param id 编号
     */
    void deleteForm(Long id);

    /**
     * 获得动态表单
     *
     * @param id 编号
     * @return 动态表单
     */
    BpmFormPO getForm(Long id);

    /**
     * 获得动态表单列表
     *
     * @return 动态表单列表
     */
    List<BpmFormPO> getFormList();

    /**
     * 获得动态表单列表
     *
     * @param ids 编号
     * @return 动态表单列表
     */
    List<BpmFormPO> getFormList(Collection<Long> ids);

    /**
     * 获得动态表单 Map
     *
     * @param ids 编号
     * @return 动态表单 Map
     */
    default Map<Long, BpmFormPO> getFormMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return CollectionUtils.convertMap(this.getFormList(ids), BpmFormPO::getId);
    }

    /**
     * 获得动态表单分页
     *
     * @param pageReqVO 分页查询
     * @return 动态表单分页
     */
    PageResult<BpmFormPO> getFormPage(BpmFormPageReqVO pageReqVO);

    /**
     * 校验流程表单已配置
     *
     * @param configStr  configStr 字段
     * @return 流程表单
     */
    BpmFormPO checkFormConfig(String  configStr);

}
