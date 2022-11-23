package com.fhs.flow.service.definition;

import cn.hutool.core.collection.CollUtil;
import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.comon.utils.CollectionUtils;
import com.fhs.flow.controller.admin.definition.vo.group.BpmUserGroupCreateReqVO;
import com.fhs.flow.controller.admin.definition.vo.group.BpmUserGroupPageReqVO;
import com.fhs.flow.controller.admin.definition.vo.group.BpmUserGroupUpdateReqVO;
import com.fhs.flow.convert.definition.BpmUserGroupConvert;
import com.fhs.flow.dal.dataobject.definition.BpmUserGroupPO;
import com.fhs.flow.dal.mysql.definition.BpmUserGroupMapper;
import com.fhs.flow.enums.CommonStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.fhs.flow.comon.exception.util.ServiceExceptionUtil.exception;
import static com.fhs.flow.enums.ErrorCodeConstants.USER_GROUP_IS_DISABLE;
import static com.fhs.flow.enums.ErrorCodeConstants.USER_GROUP_NOT_EXISTS;

/**
 * 用户组 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class BpmUserGroupServiceImpl implements BpmUserGroupService {

    @Resource
    private BpmUserGroupMapper userGroupMapper;

    @Override
    public Long createUserGroup(BpmUserGroupCreateReqVO createReqVO) {
        // 插入
        BpmUserGroupPO userGroup = BpmUserGroupConvert.INSTANCE.convert(createReqVO);
        userGroupMapper.insert(userGroup);
        // 返回
        return userGroup.getId();
    }

    @Override
    public void updateUserGroup(BpmUserGroupUpdateReqVO updateReqVO) {
        // 校验存在
        BpmUserGroupPO bpmUserGroupPO = this.validateUserGroupExists(updateReqVO.getId());
        // 更新
        BpmUserGroupPO updateObj = BpmUserGroupConvert.INSTANCE.convert(updateReqVO);
        updateObj.setCreateTime(bpmUserGroupPO.getCreateTime());
        userGroupMapper.updateById(updateObj);
    }

    @Override
    public void deleteUserGroup(Long id) {
        // 校验存在
        this.validateUserGroupExists(id);
        // 删除
        userGroupMapper.deleteById(id);
    }

    private BpmUserGroupPO validateUserGroupExists(Long id) {
        BpmUserGroupPO bpmUserGroupPO = userGroupMapper.selectById(id);
        if (bpmUserGroupPO == null) {
            throw exception(USER_GROUP_NOT_EXISTS);
        }
        return bpmUserGroupPO;
    }

    @Override
    public BpmUserGroupPO getUserGroup(Long id) {
        return userGroupMapper.selectById(id);
    }

    @Override
    public List<BpmUserGroupPO> getUserGroupList(Collection<Long> ids) {
        return userGroupMapper.selectBatchIds(ids);
    }


    @Override
    public List<BpmUserGroupPO> getUserGroupListByStatus(Integer status) {
        return userGroupMapper.selectListByStatus(status);
    }

    @Override
    public PageResult<BpmUserGroupPO> getUserGroupPage(BpmUserGroupPageReqVO pageReqVO) {
        return userGroupMapper.selectPage(pageReqVO);
    }

    @Override
    public void validUserGroups(Set<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得用户组信息
        List<BpmUserGroupPO> userGroups = userGroupMapper.selectBatchIds(ids);
        Map<Long, BpmUserGroupPO> userGroupMap = CollectionUtils.convertMap(userGroups, BpmUserGroupPO::getId);
        // 校验
        ids.forEach(id -> {
            BpmUserGroupPO userGroup = userGroupMap.get(id);
            if (userGroup == null) {
                throw exception(USER_GROUP_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(userGroup.getStatus())) {
                throw exception(USER_GROUP_IS_DISABLE, userGroup.getName());
            }
        });
    }

}
