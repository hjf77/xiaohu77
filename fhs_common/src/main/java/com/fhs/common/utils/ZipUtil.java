package com.fhs.common.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 本程序为liujiduo创建，jackwong
 * 现在没发现什么中文问题，如果发现有乱码
 * 请参考-http://log-cd.iteye.com/blog/585647
 *
 * @author jackwong
 * @version [版本号, 2016年6月1日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class ZipUtil {


    /**
     * 将filePaths 文件集合打包到zipPath 这个zip文件中
     *
     * @param zipPath   压缩后的文件路径
     * @param filePaths 需要压缩的文件路径列表
     */
    public static void zip(String zipPath, String[] filePaths) {
        File target = new File(zipPath);
        if (target.exists()) {
            target.delete(); // 删除旧的文件
        }
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(target);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));
            // 添加对应的文件Entry
            addEntry(filePaths, zos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.closeQuietly(zos, fos);
        }
    }

    /**
     * 扫描添加文件Entry
     *
     * @param filePaths 文件路径
     * @param zos       Zip文件输出流
     * @throws IOException
     */
    private static void addEntry(String[] filePaths, ZipOutputStream zos)
            throws IOException {
        //未测试  update by cyx
        File tempFile = null;
        for (String filePath : filePaths) {
            tempFile = new File(filePath);
            byte[] buffer = new byte[1024 * 10];
            try(FileInputStream fis = new FileInputStream(tempFile);
                BufferedInputStream bis = new BufferedInputStream(fis, buffer.length)){
                int read = 0;
                zos.putNextEntry(new ZipEntry(tempFile.getName()));
                while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                    zos.write(buffer, 0, read);
                }
                zos.closeEntry();
            }
        }
    }

    /**
     * 解压文件  -- 这个防范没有按照咱们的需要定制
     *
     * @param filePath 压缩文件路径
     */
    public static void unzip(String filePath) {
        File source = new File(filePath);
        if (source.exists()) {
            ZipInputStream zis = null;
            BufferedOutputStream bos = null;
            try {
                zis = new ZipInputStream(new FileInputStream(source));
                ZipEntry entry = null;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.isDirectory()) {
                        continue;
                    }
                    File target = new File(source.getParent(), entry.getName());
                    if (!target.getParentFile().exists()) {
                        // 创建文件父目录
                        target.getParentFile().mkdirs();
                    }
                    // 写入文件
                    bos = new BufferedOutputStream(new FileOutputStream(target));
                    int read = 0;
                    byte[] buffer = new byte[1024 * 10];
                    while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.flush();
                }
                zis.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtil.closeQuietly(zis,bos);

            }
        }
    }

    public static void main(String[] args) {

        zip("d:/1.zip", new String[]{"d:/QQ图片20160112144921.jpg", "d:/1.doc"});
    }
}

/**
 * 用于关闭流对象
 *
 * @author jackwong
 * @version [版本号, 2016年6月1日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
class IOUtil {
    /**
     * 关闭一个或多个流对象
     *
     * @param closeables 可关闭的流对象列表
     * @throws IOException
     */
//    public static void close(Closeable... closeables) throws IOException {
//        if (closeables != null) {
//            for (Closeable closeable : closeables) {
//                if (closeable != null) {
//                    closeable.close();
//                }
//            }
//        }
//    }

    /**
     * 关闭一个或多个流对象
     *
     * @param closeables 可关闭的流对象列表
     */
    public static void closeQuietly(Closeable... closeables) {
        try {
            if (closeables != null) {
                for (Closeable closeable : closeables) {
                    if (closeable != null) {
                        closeable.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}