package com.fhs.pub.ueditor.hunter;

import com.fhs.common.spring.FhsSpringContextUtil;
import com.fhs.pub.ueditor.define.AppInfo;
import com.fhs.pub.ueditor.define.BaseState;
import com.fhs.pub.ueditor.define.MultiState;
import com.fhs.pub.ueditor.define.State;
import com.fhs.pub.service.PubFileService;
import com.fhs.pub.vo.PubFileVO;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


public class FileManager {

    private String dir = null;
    private String rootPath = null;
    private String[] allowFiles = null;
    private int count = 0;

    /**
     * 文件服务域名
     */
    public static String basePath;

    /**
     * 文件储存路径
     */
    public static String fileSavePath;


    public FileManager(Map<String, Object> conf) {

        this.rootPath = (String) conf.get("rootPath");
        this.dir = this.rootPath + (String) conf.get("dir");
        this.allowFiles = this.getAllowFiles(conf.get("allowFiles"));
        this.count = (Integer) conf.get("count");

    }

    public State listFile(int index) {
        File dir = new File(fileSavePath);
        State state = null;

        if (!dir.exists()) {
            return new BaseState(false, AppInfo.NOT_EXIST);
        }

        if (!dir.isDirectory()) {
            return new BaseState(false, AppInfo.NOT_DIRECTORY);
        }

        Collection<File> list = FileUtils.listFiles(dir, this.allowFiles, true);

        if (index < 0 || index > list.size()) {
            state = new MultiState(true);
        } else {
            Object[] fileList = Arrays.copyOfRange(list.toArray(), index, index + this.count);
            state = this.getState(fileList);
        }

        state.putInfo("start", index);
        state.putInfo("total", list.size());

        return state;

    }

    private State getState(Object[] files) {

        MultiState state = new MultiState(true);
        BaseState fileState = null;

        File file = null;
        PubFileService service = (PubFileService) FhsSpringContextUtil.getBeansByClass(PubFileService.class);
        for (Object obj : files) {
            if (obj == null) {
                break;
            }
            file = (File) obj;
            fileState = new BaseState(true);
            String fileId = file.getName().substring(0, file.getName().indexOf("."));
            PubFileVO serviceFile = service.selectById(fileId);
            fileState.putInfo("url", basePath + "downLoad/file?fileId=" + fileId);
            fileState.putInfo("original", serviceFile.getFileName());
            fileState.putInfo("type", serviceFile.getFileSuffix().replace(".", ""));
            state.addState(fileState);
        }

        return state;

    }

    private String getPath(File file) {
//		String path = file.getAbsolutePath();
//		return "/"+path.substring(this.rootPath.length() , path.length());
        return "";
    }

    private String[] getAllowFiles(Object fileExt) {

        String[] exts = null;
        String ext = null;

        if (fileExt == null) {
            return new String[0];
        }

        exts = (String[]) fileExt;

        for (int i = 0, len = exts.length; i < len; i++) {

            ext = exts[i];
            exts[i] = ext.replace(".", "");

        }

        return exts;

    }

}
