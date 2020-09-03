package com.fhs.pagex.beetl;

import org.beetl.core.Resource;
import org.beetl.core.ResourceLoader;
import org.beetl.core.exception.BeetlException;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 解决beetl有的时候找不到文件的锅
 */
public class FhsBeetlResource extends Resource {
    String path = null;
    File file = null;
    long lastModified = 0L;

    public FhsBeetlResource(String key, String path, ResourceLoader resourceLoader) {
        super(key, resourceLoader);
        this.path = path;
    }

    @Override
    public Reader openReader() {
        FhsBeetlClasspathResourceLoader loader = (FhsBeetlClasspathResourceLoader)this.resourceLoader;
        ClassLoader cs = loader.getClassLoader();
        URL url = cs.getResource(this.path);
        if (url == null) {
            url = this.resourceLoader.getClass().getResource(this.path);
        }
        if (url == null) {
            String tempPath = this.path.substring(1);
            url = cs.getResource(tempPath);
        }
        if (url == null) {
            BeetlException be = new BeetlException("TEMPLATE_LOAD_ERROR");
            be.pushResource(this);
            throw be;
        } else {
            InputStream is;
            try {
                is = url.openStream();
            } catch (IOException var9) {
                BeetlException be = new BeetlException("TEMPLATE_LOAD_ERROR");
                be.pushResource(this);
                throw be;
            }

            if (is == null) {
                BeetlException be = new BeetlException("TEMPLATE_LOAD_ERROR");
                be.pushResource(this);
                throw be;
            } else {
                if (url.getProtocol().equals("file")) {
                    try {
                        this.file = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
                    } catch (UnsupportedEncodingException var8) {
                        throw new RuntimeException(var8);
                    }

                    this.lastModified = this.file.lastModified();
                }

                try {
                    Reader br = new BufferedReader(new InputStreamReader(is, "UTF8"));
                    return br;
                } catch (UnsupportedEncodingException var7) {
                    return null;
                }
            }
        }
    }
    @Override
    public boolean isModified() {
        if (this.file != null) {
            return this.file.lastModified() != this.lastModified;
        } else {
            return false;
        }
    }
    @Override
    public String getId() {
        return this.id;
    }
}
