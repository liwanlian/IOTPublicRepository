package com.srthink.iotengravingmachinelibrary.utils;

import java.io.File;

/**
 * @author liwanlian
 * @date 2021/6/18 14:01
 */
public class CommonUtils {
    public static File getTempFile(String url, String filePath, String tempName) {
        File parentFile = new File(filePath).getParentFile();
//        String md5 = bytes2HexString(url.getBytes());
        String md5 = bytes2HexString(tempName.getBytes());
        return new File(parentFile.getAbsolutePath(), md5 + ".temp");
    }

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        int len = bytes.length;
        if (len <= 0) {
            return "";
        }
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }
}
