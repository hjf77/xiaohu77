package com.fhs.basics.service.impl;

import com.fhs.common.utils.DateUtils;
import com.fhs.common.utils.StringUtils;
import com.fhs.core.logger.Logger;
import com.fhs.basics.po.PubFilePO;
import com.fhs.basics.service.FileServerBusiness;
import com.fhs.basics.service.FileStorage;
import com.fhs.basics.service.PubFileService;
import com.fhs.basics.vo.PubFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class FileServerBusinessImpl implements FileServerBusiness{

    private static final Logger LOG = Logger.getLogger(FileServerBusinessImpl.class);

    @Autowired
    private PubFileService fileService;

    @Autowired
    private FileStorage fileStorage;

    @Override
    public PubFileVO uploadFile(MultipartFile fileData) {
        PubFileVO sf = new PubFileVO();
        String fileName = fileData.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String fileId = StringUtils.getUUID();
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
    public List<PubFileVO> uploadFileForList(List<MultipartFile> allFileData) {
        List<PubFileVO> rvList = new ArrayList<PubFileVO>();
        for (MultipartFile fileData : allFileData) {
            rvList.add(this.uploadFile(fileData));
        }
        return rvList;
    }

    /**
     * @param sf
     * @return
     */
    private boolean insertDataToDB(PubFilePO sf) {
        return (fileService.insertSelective(sf) > 0);
    }


}
