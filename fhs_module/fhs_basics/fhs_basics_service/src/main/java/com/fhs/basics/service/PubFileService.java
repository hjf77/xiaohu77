/*
 * 文 件 名:  ServiceFileService.java
 * 版    权:  sxpartner Technology International Ltd.
 * 描    述:  &lt;描述&gt;.
 * 修 改 人:  wanglei
 * 修改时间:  ${date}
 * 跟踪单号:  &lt;跟踪单号&gt;
 * 修改单号:  &lt;修改单号&gt;
 * 修改内容:  &lt;修改内容&gt;
 */
package com.fhs.basics.service;

import com.fhs.core.base.service.BaseService;
import com.fhs.basics.po.PubFilePO;
import com.fhs.basics.vo.PubFileVO;

/**
 * 文件CRUD service
 * 本服务不对外开放 对外方法参见PubFileService
 *
 * @author 王磊
 * @version [版本号, 2015/08/14 11:34:23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface PubFileService extends BaseService<PubFileVO, PubFilePO> {

    int deletePubFile(String fileId);

}
