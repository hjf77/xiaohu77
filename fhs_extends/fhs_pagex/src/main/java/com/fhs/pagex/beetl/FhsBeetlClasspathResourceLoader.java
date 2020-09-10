package com.fhs.pagex.beetl;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Resource;
import org.beetl.core.ResourceLoader;
import org.beetl.core.misc.BeetlUtil;

import java.net.URL;
import java.util.Map;

/**
 * 解决beetl偶尔404问题
 */
public class FhsBeetlClasspathResourceLoader  implements ResourceLoader {
    private String root;
    boolean autoCheck;
    protected String charset;
    String functionRoot;
    GroupTemplate gt;
    String functionSuffix;
    ClassLoader classLoader;

    public FhsBeetlClasspathResourceLoader() {
        this.root = null;
        this.autoCheck = false;
        this.charset = "UTF-8";
        this.functionRoot = "functions";
        this.gt = null;
        this.functionSuffix = "fn";
        this.classLoader = null;
        this.classLoader = this.getClass().getClassLoader();
        this.root = "";
    }

    public FhsBeetlClasspathResourceLoader(ClassLoader classLoader) {
        this.root = null;
        this.autoCheck = false;
        this.charset = "UTF-8";
        this.functionRoot = "functions";
        this.gt = null;
        this.functionSuffix = "fn";
        this.classLoader = null;
        this.classLoader = classLoader;
        this.root = "";
    }

    public FhsBeetlClasspathResourceLoader(ClassLoader classLoader, String root) {
        this.root = null;
        this.autoCheck = false;
        this.charset = "UTF-8";
        this.functionRoot = "functions";
        this.gt = null;
        this.functionSuffix = "fn";
        this.classLoader = null;
        this.classLoader = classLoader;
        this.root = this.checkRoot(root);
    }

    public FhsBeetlClasspathResourceLoader(ClassLoader classLoader, String root, String charset) {
        this(classLoader, root);
        this.charset = charset;
    }

    public FhsBeetlClasspathResourceLoader(String root) {
        this();
        this.root = this.checkRoot(root);
    }

    public FhsBeetlClasspathResourceLoader(String root, String charset) {
        this(root);
        this.charset = charset;
    }

    @Override
    public Resource getResource(String key) {
        Resource resource = new FhsBeetlResource(key, this.getChildPath(this.root, key), this);
        return resource;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isModified(Resource key) {
        return this.autoCheck ? key.isModified() : false;
    }

    public boolean isAutoCheck() {
        return this.autoCheck;
    }

    public void setAutoCheck(boolean autoCheck) {
        this.autoCheck = autoCheck;
    }

    public String getRoot() {
        return this.root;
    }
    @Override
    public void init(GroupTemplate gt) {
        Map<String, String> resourceMap = gt.getConf().getResourceMap();
        if (resourceMap.get("root") != null) {
            String temp = (String) resourceMap.get("root");
            temp = this.checkRoot(temp);
            this.root = this.getChildPath(this.root, temp);
        }

        if (this.charset == null) {
            this.charset = (String) resourceMap.get("charset");
        }

        this.functionSuffix = (String) resourceMap.get("functionSuffix");
        this.autoCheck = Boolean.parseBoolean((String) resourceMap.get("autoCheck"));
        this.functionRoot = (String) resourceMap.get("functionRoot");
    }

    @Override
    public boolean exist(String key) {
        String path = this.getChildPath(this.root, key);
        URL url = this.classLoader.getResource(path);
        if (url == null) {
            url = this.classLoader.getClass().getResource(path);
        }
        if (url == null) {
            String tempPath = path.substring(1);
            url = this.classLoader.getResource(tempPath);
        }

        return url != null;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String getResourceId(Resource resource, String id) {
        return resource == null ? id : BeetlUtil.getRelPath(resource.getId(), id);
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public String getInfo() {
        return "ClassLoader:" + this.classLoader + " Path:" + this.root;
    }

    protected String checkRoot(String path) {
        if (path != null && path.length() != 0 && !path.equals("/")) {
            if (path.endsWith("/")) {
                return path.substring(0, path.length() - 1);
            } else {
                return path.startsWith("/") ? path.substring(1, path.length()) : path;
            }
        } else {
            return "";
        }
    }

    protected String getChildPath(String path, String child) {
        if (child.length() == 0) {
            return path;
        } else {
            return child.startsWith("/") ? path + child : path + "/" + child;
        }
    }
}
