package com.srthink.iotboxaar.utils;

import java.util.Random;

/**
 * @author liwanlian
 * @date 2021/5/26 16:06
 */
public class RandomUtils {
    public static String getRandomsData() {
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            result = result + random.nextInt(10);
        }
        return result;
    }
}
