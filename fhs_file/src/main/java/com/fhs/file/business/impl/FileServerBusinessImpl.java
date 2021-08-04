package com.fhs.file.business.impl;

import com.fhs.file.bean.ServiceFile;
import com.fhs.file.business.FileServerBusiness;
import com.fhs.file.service.ServiceFileService;
import com.fhs.common.utils.*;
import com.fhs.file.storage.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileServerBusinessImpl implements FileServerBusiness {

    private static final Logger LOG = Logger.getLogger(FileServerBusinessImpl.class);

    @Autowired
    private ServiceFileService fileService;

    @Autowired
    private FileStorage fileStorage;

    @Override
    public ServiceFile uploadFile(MultipartFile fileData,String fileId) {
        ServiceFile sf = new ServiceFile();
        String fileName = fileData.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        fileId = fileId == null ? StringUtil.getUUID() : fileId;
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
    public List<ServiceFile> uploadFileForList(List<MultipartFile> allFileData,List<String> fileIds) {
        List<ServiceFile> rvList = new ArrayList<ServiceFile>();
        for (int i =0;i<allFileData.size();i++){
            String fileId = null;
            if(fileIds!=null && fileIds.size()>i){
                fileId = fileIds.get(i);
            }
            rvList.add(this.uploadFile(allFileData.get(i),fileId));
        }
        return rvList;
    }

    /**
     * @param sf
     * @return
     */
    private boolean insertDataToDB(ServiceFile sf) {
        return (fileService.insert(sf) > 0);
    }
}
