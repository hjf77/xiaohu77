package com.fhs.flow.service.definition;

import cn.hutool.core.lang.Assert;
import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.comon.utils.JsonUtils;
import com.fhs.flow.comon.utils.ValidationUtils;
import com.fhs.flow.controller.admin.definition.vo.form.BpmFormCreateReqVO;
import com.fhs.flow.controller.admin.definition.vo.form.BpmFormPageReqVO;
import com.fhs.flow.controller.admin.definition.vo.form.BpmFormUpdateReqVO;
import com.fhs.flow.convert.definition.BpmFormConvert;
import com.fhs.flow.dal.dataobject.definition.BpmFormPO;
import com.fhs.flow.dal.mysql.definition.BpmFormMapper;
import com.fhs.flow.enums.ErrorCodeConstants;
import com.fhs.flow.enums.definition.BpmModelFormTypeEnum;
import com.fhs.flow.service.definition.dto.BpmFormFieldRespDTO;
import com.fhs.flow.service.definition.dto.BpmModelMetaInfoRespDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static com.fhs.flow.comon.exception.util.ServiceExceptionUtil.exception;
import static com.fhs.flow.enums.ErrorCodeConstants.*;

/**
 * 动态表单 Service 实现类
 *
 * @author 风里雾里
 */
@Service
@Validated
public class BpmFormServiceImpl implements BpmFormService {

    @Resource
    private BpmFormMapper formMapper;

    @Override
    public Long createForm(BpmFormCreateReqVO createReqVO) {
        this.checkFields(createReqVO.getFields());
        // 插入
        BpmFormPO form = BpmFormConvert.INSTANCE.convert(createReqVO);
        formMapper.insert(form);
        // 返回
        return form.getId();
    }

    @Override
    public void updateForm(BpmFormUpdateReqVO updateReqVO) {
        this.checkFields(updateReqVO.getFields());
        // 校验存在
        BpmFormPO bpmFormPO = this.validateFormExists(updateReqVO.getId());
        // 更新
        BpmFormPO updateObj = BpmFormConvert.INSTANCE.convert(updateReqVO);
        updateObj.setCreateTime(bpmFormPO.getCreateTime());
        formMapper.updateById(updateObj);
    }

    @Override
    public void deleteForm(Long id) {
        // 校验存在
        this.validateFormExists(id);
        // 删除
        formMapper.deleteById(id);
    }

    private BpmFormPO validateFormExists(Long id) {
        BpmFormPO bpmFormPO = formMapper.selectById(id);
        if (bpmFormPO == null) {
            throw exception(FORM_NOT_EXISTS);
        }
        return bpmFormPO;
    }

    @Override
    public BpmFormPO getForm(Long id) {
        return formMapper.selectById(id);
    }

    @Override
    public List<BpmFormPO> getFormList() {
        return formMapper.selectList();
    }

    @Override
    public List<BpmFormPO> getFormList(Collection<Long> ids) {
        return formMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BpmFormPO> getFormPage(BpmFormPageReqVO pageReqVO) {
        return formMapper.selectPage(pageReqVO);
    }


    @Override
    public BpmFormPO checkFormConfig(String configStr) {
        BpmModelMetaInfoRespDTO metaInfo = JsonUtils.parseObject(configStr, BpmModelMetaInfoRespDTO.class);
        if (metaInfo == null || metaInfo.getFormType() == null) {
            throw exception(MODEL_DEPLOY_FAIL_FORM_NOT_CONFIG);
        }
        // 校验表单存在
        if (Objects.equals(metaInfo.getFormType(), BpmModelFormTypeEnum.NORMAL.getType())) {
            BpmFormPO form = getForm(metaInfo.getFormId());
            if (form == null) {
                throw exception(FORM_NOT_EXISTS);
            }
            return form;
        }
        return null;
    }

    private void checkKeyNCName(String key) {
        if (!ValidationUtils.isXmlNCName(key)) {
            throw exception(MODEL_KEY_VALID);
        }
    }
    /**
     * 校验 Field，避免 field 重复
     *
     * @param fields field 数组
     */
    private void checkFields(List<String> fields) {
        Map<String, String> fieldMap = new HashMap<>(); // key 是 vModel，value 是 label
        for (String field : fields) {
            BpmFormFieldRespDTO fieldDTO = JsonUtils.parseObject(field, BpmFormFieldRespDTO.class);
            Assert.notNull(fieldDTO);
            String oldLabel = fieldMap.put(fieldDTO.getVModel(), fieldDTO.getLabel());
            // 如果不存在，则直接返回
            if (oldLabel == null) {
                continue;
            }
            // 如果存在，则报错
            throw exception(ErrorCodeConstants.FORM_FIELD_REPEAT, oldLabel, fieldDTO.getLabel(), fieldDTO.getVModel());
        }
    }

}
