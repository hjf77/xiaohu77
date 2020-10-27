package com.fhs.pub.ueditor.upload;

import com.fhs.core.config.EConfig;
import com.fhs.pub.ueditor.define.State;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public class Uploader {
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;

	/**
	 * 文件服务域名
	 */
	public static String basePath =  EConfig.getPathPropertiesValue("fileServiceUrl");

	/**
	 * 文件储存路径
	 */
	public static String fileSavePath  = EConfig.getPathPropertiesValue("fileSavePath");

	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
	}

	public final State doExec() throws Exception {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName),
					this.conf);
		} else {
			state = (State)BinaryUploader.save(this.request, this.conf, fileSavePath,basePath);
		}
		return state;
	}
}
