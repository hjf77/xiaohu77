package com.fhs.file.action;

import com.fhs.common.utils.DateUtils;
import com.fhs.common.utils.JsonUtils;
import com.fhs.common.utils.Logger;
import com.fhs.core.base.action.BaseAction;
import com.fhs.file.bean.ServiceFile;
import com.fhs.file.business.FileServerBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qiuhang
 * @des 文件上传action
 */
@Controller
@RequestMapping("upload")
public class UploadAction extends BaseAction<ServiceFile> {

    private static final Logger LOG = Logger.getLogger(DownLoadAction.class);

    @Autowired
    private FileServerBusiness fileServerBusiness;

    /**
     * 文件
     *
     * @param Filedata
     * @return
     */
    @RequestMapping(value = "file", method = RequestMethod.POST)
    public void uploadFile(@RequestParam MultipartFile Filedata, HttpServletRequest request,
                           HttpServletResponse response) {
        if (Filedata == null) {
            super.outToClient(false, response);
        }
        LOG.infoMsg("开始上传文件,当前时间为{}", DateUtils.getCurrentDateStr(DateUtils.DATETIME_PATTERN));
        String fileId = request.getParameter("filedId");
        String fileName = Filedata.getOriginalFilename();
        LOG.infoMsg("文件ID{},{}", fileId, fileName);
        ServiceFile file = fileServerBusiness.uploadFile(Filedata, fileId);
        LOG.infoMsg("结束上传文件,结束时间为{}", DateUtils.getCurrentDateStr(DateUtils.DATETIME_PATTERN));
        super.outWriteJson(JsonUtils.bean2json(file), response);
    }


}
