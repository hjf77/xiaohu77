/*
 * 文 件 名:  ServiceFileServiceImpl.java
 * 版    权:  sxpartner Technology International Ltd.
 * 描    述:  &lt;描述&gt;.
 * 修 改 人:  wanglei
 * 修改时间:  ${date}
 * 跟踪单号:  &lt;跟踪单号&gt;
 * 修改单号:  &lt;修改单号&gt;
 * 修改内容:  &lt;修改内容&gt;
 */
package com.fhs.basics.service.impl;

import com.fhs.basics.mapper.PubFileMapper;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.db.ds.DataSource;
import com.fhs.basics.po.PubFilePO;
import com.fhs.basics.service.PubFileService;
import com.fhs.basics.vo.PubFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 文件CRUD service
 *
 * @author 王磊
 * @version [版本号, 2015/08/14 11:34:23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
@DataSource("basic")
public class PubFileServiceImpl extends BaseServiceImpl<PubFileVO, PubFilePO> implements PubFileService {

    @Autowired
    private PubFileMapper pubFileMapper;

    @Override
    public int deletePubFile(String fileId) {
        return pubFileMapper.deletePubFile(fileId);
    }
}
