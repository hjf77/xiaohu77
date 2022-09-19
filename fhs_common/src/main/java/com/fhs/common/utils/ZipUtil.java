package com.fhs.common.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;

/**
 * 本程序为liujiduo创建，wanglei
 * 现在没发现什么中文问题，如果发现有乱码
 * 请参考-http://log-cd.iteye.com/blog/585647
 *
 * @author wanglei
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
            try (FileInputStream fis = new FileInputStream(tempFile);
                 BufferedInputStream bis = new BufferedInputStream(fis, buffer.length)) {
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
                IOUtil.closeQuietly(zis, bos);

            }
        }
    }
    /**
     *
     * @param resourcesPath 被压缩文件路径
     * @param targetPath 保存路径
     * @throws Exception
     */
    public static String compressedFile(String resourcesPath, String targetPath) throws Exception {
        //源文件
        File resourcesFile = new File(resourcesPath);
        //目的文件夹
        File targetFile = new File(targetPath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        String targetName ="" ;
        if(resourcesFile.getName().indexOf(".")!=-1){
            //如果是文件则取文件名字
            targetName = resourcesFile.getName().substring(0,resourcesFile.getName().indexOf("."))+".zip";
        }else{
            //目的压缩文件名
            targetName = resourcesFile.getName() + ".zip";
        }
        FileOutputStream outputStream = new FileOutputStream(targetPath + "\\" + targetName);

        CheckedOutputStream cos = new CheckedOutputStream(outputStream, new CRC32());

        ZipOutputStream out = new ZipOutputStream(cos);

        createCompressedFile(out, resourcesFile, "");

        out.close();

        return targetPath + "\\" + targetName;
    }

    public static void createCompressedFile(ZipOutputStream out, File file, String dir) throws Exception {
        //如果当前的是文件夹，则进行进一步处理
        if (file.isDirectory()) {
            //得到文件列表信息

            File[] files = file.listFiles();
            //将文件夹添加到下一级打包目录
            out.putNextEntry(new ZipEntry(dir + "/"));
            dir = dir.length() == 0 ? "" : dir + "/";
            //循环将文件夹中的文件打包
            for (int i = 0; i < files.length; i++) {
                //递归处理
                createCompressedFile(out, files[i], dir + files[i].getName());
            }

        } else {
            //当前的是文件，打包处理
            //文件输入流
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(dir);
            out.putNextEntry(entry);
            int j = 0;
            byte[] buffer = new byte[1024];

            while ((j = bis.read(buffer)) > 0) {
                out.write(buffer, 0, j);
            }
            //关闭输入流
            bis.close();
        }
    }


}

/**
 * 用于关闭流对象
 *
 * @author wanglei
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
