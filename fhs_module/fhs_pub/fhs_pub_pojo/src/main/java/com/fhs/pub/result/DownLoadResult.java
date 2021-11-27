package com.fhs.pub.result;

import com.fhs.pub.po.PubFilePO;

public class DownLoadResult {

    private Integer rvCode = 0;

    private String rvMsg = "ok!";

    private PubFilePO serviceFile;

    public Integer getRvCode() {
        return rvCode;
    }

    public void setRvCode(Integer rvCode) {
        this.rvCode = rvCode;
    }

    public String getRvMsg() {
        return rvMsg;
    }

    public void setRvMsg(String rvMsg) {
        this.rvMsg = rvMsg;
    }

    public PubFilePO getServiceFile() {
        return serviceFile;
    }

    public void setServiceFile(PubFilePO serviceFile) {
        this.serviceFile = serviceFile;
    }
}
