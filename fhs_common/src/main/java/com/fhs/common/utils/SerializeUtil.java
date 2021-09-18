package com.fhs.common.utils;

import java.io.*;

/**
 * 序列化反序列化工具类
 *
 * @author jackwong
 * @date 2020-05-21 13:40:16
 */
public class SerializeUtil {
    /**
     * 序列化
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @return
     */
    public static Object unSerialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 序列化
     *
     * @param data
     * @param filePath
     * @return
     */
    public static boolean serializeToFile(Object data, String filePath) throws Exception {
        //未测试  update by cyx
        File file = new File(filePath);
        try (FileOutputStream out = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(out)) {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            oos.writeObject(data);
            oos.flush();
            oos.close();
            return true;
        }
    }


    /**
     * 序列化
     *
     * @param filePath
     * @return
     */
    public static Object unSerializeFromFile(String filePath) throws Exception {
        //未测试  update by cyx
        File file = new File(filePath);
        try (FileInputStream in = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(in)) {
            if (!file.exists()) {
                return null;
            }
            Object result = ois.readObject();
            return result;
        }
    }
}
