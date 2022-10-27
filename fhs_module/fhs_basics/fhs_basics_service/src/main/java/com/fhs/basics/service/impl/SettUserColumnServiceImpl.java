package com.fhs.basics.service.impl;


import com.fhs.basics.po.SettUserColumnPO;
import com.fhs.basics.service.SettUserColumnService;
import com.fhs.basics.vo.SettUserColumnVO;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.annotation.Namespace;
import org.springframework.stereotype.Service;

/**
 * 用户自定义列信息配置(SettUserColumn)表服务实现类
 *
 * @author wanglei
 * @since 2022-10-13 11:46:47
 */
@Service
@Namespace("sett_user_column")
public class SettUserColumnServiceImpl extends BaseServiceImpl<SettUserColumnVO, SettUserColumnPO> implements SettUserColumnService {

}