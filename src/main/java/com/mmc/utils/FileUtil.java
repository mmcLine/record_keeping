package com.mmc.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 文件工具
 */
public class FileUtil {

    /**
     * 计算文件md5值
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 获取文件md5值
     * @param inputStream
     * @return
     */
    public static String getFileMD5(InputStream inputStream) {
        MessageDigest digest = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            while ((len = inputStream.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 计算流的md5，并写入到文件
     * @param inputStream
     * @param file
     * @return
     * @throws IOException
     */
    public static String copyInputStreamToFileAndGetMd5Hex(InputStream inputStream, File file) throws IOException {

        MessageDigest digest = DigestUtils.getMd5Digest();

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[2048];
            int len = inputStream.read(buffer);
            while ((len=inputStream.read(buffer))>-1) {
                // 计算MD5,顺便写到文件
                digest.update(buffer, 0, len);
                outputStream.write(buffer, 0, len);
            }
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        return Hex.encodeHexString(digest.digest());
    }

}
