package com.srthink.iotboxaar.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 */
public class MD5Utils {

    public static MD5Utils INSTANCE = new MD5Utils();

    /**
     * 获取string的MD5加密
     *
     * @param string
     * @return
     */
    public static String getMd5(String string) {

        String resultString = null;
        try {
            resultString = new String(string);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));

        } catch (Exception ex) {
            LogUtil.logInfo(ex.toString());
        }
        return resultString;
    }

    public static String byteArrayToHexString(byte[] b) {

        StringBuffer resultSb = new StringBuffer();

        for (int i = 0; i < b.length; i++) {

            resultSb.append(byteToHexString(b[i]));

        }

        return resultSb.toString();

    }

    private static String byteToHexString(byte b) {

        int n = b;

        if (n < 0)

            n = 256 + n;

        int d1 = n / 16;

        int d2 = n % 16;

        return hexDigits[d1] + hexDigits[d2];

    }

    private final static String[] hexDigits = {

            "0", "1", "2", "3", "4", "5", "6", "7",

            "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * md5加密
     *
     * @param content
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String md5(String content) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] buf = content.getBytes("UTF-8");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(buf);
        byte[] tmp = md5.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : tmp) {
            sb.append(Integer.toHexString(b & 0xff));
        }
        return sb.toString();
    }

    /**
     * 获取文件的md5--》file
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static String getFileMd5(InputStream inputStream) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[inputStream.available()];
        int count;
        while ((count = inputStream.read(buffer)) > 0) {
            if (count < buffer.length) {
                md5.update(buffer, 0, count);
            } else {
                md5.update(buffer);
            }
        }
        BigInteger bi = new BigInteger(1, md5.digest());
        return bi.toString(16);
    }

    /**
     * 获取文件的MD5-->byte
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String getBytesMd5(byte[] bytes) throws Exception {
        String result = "";
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);
        byte b[] = md.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }

        result = buf.toString();
        return result;
    }

    public static void main(String[] args) {
        String admin = getMd5("admin");
        String md5 = getMd5("2");
        LogUtil.logInfo(admin);
        LogUtil.logInfo(md5);
    }

    public static MD5Utils getInstance() {
        return INSTANCE;
    }

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    /**
     * 文件类取MD5
     *
     * @param file
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String calcMD5(File file) {
        try (InputStream stream = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {
            return calcMD5(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 输入流取MD5
     *
     * @param stream
     * @return
     */
    public static String calcMD5(InputStream stream) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[8192];
            int len;
            while ((len = stream.read(buf)) > 0) {
                digest.update(buf, 0, len);
            }
            return toHexString(digest.digest());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String toHexString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    /**
     * 获取文件的MD5值
     *
     * @param file 文件路径
     * @return md5
     */
    public static String getFileMd5(File file) {
        MessageDigest messageDigest;
        FileInputStream fis = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            if (file == null) {
                return "";
            }
            if (!file.exists()) {
                return "";
            }
            int len = 0;
            fis = new FileInputStream(file);
            //普通流读取方式
            byte[] buffer = new byte[1024 * 1024 * 10];
            while ((len = fis.read(buffer)) > 0) {
                //该对象通过使用 update（）方法处理数据
                messageDigest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            String md5 = bigInt.toString(16);
            while (md5.length() < 32) {
                md5 = "0" + md5;
            }
            return md5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getMd5ByFile(File file) {
        if (!file.exists())
            return "";

        String value = null;
        try {
            FileInputStream in = new FileInputStream(file);

            try {
                MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());

                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(byteBuffer);

                BigInteger bi = new BigInteger(1, md5.digest());

                value = bi.toString(16);

                // 防止文件的md5值以0开头
                if (value.length() == 31) {
                    value = "0" + value;
                }

            } catch (Exception e) {
                // throw this exception
                e.printStackTrace();
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // throw this exception
        }

        return value;
    }
}
