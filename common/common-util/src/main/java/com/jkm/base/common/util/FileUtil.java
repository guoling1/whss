package com.jkm.base.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * Created by yulong.zhang on 2016/11/30.
 */
@Slf4j
@UtilityClass
public class FileUtil {

    /**
     * 读取文件的二进制
     *
     * @param file
     * @return
     */
    public static byte[] readBinary(final File file) {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream();
            final byte[] bs = new byte[1024];
            int readLen;
            while ((readLen = fis.read(bs)) != -1) {
                baos.write(bs, 0, readLen);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (final Exception e) {
            log.error("read file to byte error", e);
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (final IOException e) {
                    log.error("close FileInputStream error", e);
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    log.error("close ByteArrayOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 将二级制流保存到file
     *
     * @param data
     * @param file
     */
    public static void saveToFile(final byte[] data, final File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
        } catch (final Exception e) {
            log.error("save byte to file error", e);
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (final IOException e) {
                    log.error("close FileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将输入流保存到file
     *
     * @param in
     * @param file
     */
    public static void saveToFile(final InputStream in, final File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            final byte[] bs = new byte[2048];
            int hasRead;
            while ((hasRead = in.read(bs)) != -1) {
                fos.write(bs, 0, hasRead);
            }
            fos.flush();
        } catch (final Exception e) {
            log.error("save InputStream to file error", e);
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
