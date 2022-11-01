package com.fhs.common.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author user
 * @since 2019-05-18 11:18:12
 */
public class ThumbnailatorUtils {

    /**
     * 指定大小进行缩放
     *
     * @throws IOException
     */
    public static void zoom(String soucerPath, String targetPath, int width, int height)
            throws IOException {
        /*
         * size(width,height) 若图片横比200小，高比300小，不变 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
         * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
         */
        Thumbnails.of(soucerPath).size(width, height).toFile(targetPath);
    }


    /**
     * 输出到OutputStream
     *
     * @throws IOException
     */
    public static byte[] zoom2Bytes(String soucerPath, int width, int height)
            throws IOException {
        /**
         * toOutputStream(流对象)
         */
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(soucerPath).outputQuality(1).size(width, height).toOutputStream(os);
        byte[] result = os.toByteArray();
        os.close();
        return result;
    }


    /**
     * 输出到OutputStream
     *
     * @throws IOException
     */
    public static byte[] zoom2Bytes(InputStream soucerPath, int width, int height)
            throws IOException {
        /**
         * toOutputStream(流对象)
         */
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(soucerPath).size(width, height).keepAspectRatio(false).toOutputStream(os);
        byte[] result = os.toByteArray();
        os.close();
        return result;
    }


}
