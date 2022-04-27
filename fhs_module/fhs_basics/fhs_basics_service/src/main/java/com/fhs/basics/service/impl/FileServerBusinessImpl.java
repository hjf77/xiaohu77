package com.fhs.basics.service.impl;

import com.fhs.basics.service.FileServerBusiness;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.DateUtils;
import com.fhs.common.utils.StringUtils;
import com.fhs.core.db.ds.DataSource;
import com.fhs.basics.po.PubFilePO;
import com.fhs.basics.service.FileStorage;
import com.fhs.basics.service.PubFileService;
import com.fhs.basics.vo.PubFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@Service
@DataSource("basic")
public class FileServerBusinessImpl implements FileServerBusiness {


    @Autowired
    private PubFileService fileService;

    @Autowired
    private FileStorage fileStorage;

    @Override
    public PubFileVO uploadFile(MultipartFile fileData,String fileId) {
        PubFileVO sf = new PubFileVO();
        String fileName = fileData.getOriginalFilename();
        if (fileName.contains(" ")){
            fileName = fileName.replaceAll(" ", ""); //去掉所有空格，包括首尾、中间
        }
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        fileId = CheckUtils.isNullOrEmpty(fileId) ? StringUtils.getUUID() : fileId;
        String currentDate = DateUtils.getCurrentDateStr("yyyy-MM-dd");
        sf.setFileId(fileId);
        sf.setFileName(fileName);
        sf.setFileSuffix(suffix);
        sf.setUploadDate(currentDate);
        fileStorage.uploadFile(sf, fileData);
        this.insertDataToDB(sf);
        return sf;
    }

    @Override
    public PubFileVO uploadInputStream(InputStream inputStream) {
        PubFileVO sf = new PubFileVO();
        String suffix = ".jpg";
        String fileName = StringUtils.getUUID()+suffix;
        String currentDate = DateUtils.getCurrentDateStr("yyyy-MM-dd");
        sf.setFileId(StringUtils.getUUID());
        sf.setFileName(fileName);
        sf.setFileSuffix(suffix);
        sf.setUploadDate(currentDate);
        fileStorage.uploadInputStream(sf, inputStream);
        this.insertDataToDB(sf);
        return sf;
    }

    /**
     * @param sf
     * @return
     */
    private boolean insertDataToDB(PubFilePO sf) {
        return (fileService.insertSelective(sf) > 0);
    }


}
