package com.srthink.iotboxaar.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author liwanlian
 * @date 2021/4/27 14:02
 */
public class FileOperation {

    private static final String TAG = "FileOperation";

    /**
     * 创建文件
     *
     * @param fileName
     * @return
     */
    public static boolean createFile(File fileName) throws Exception {
        boolean flag = false;
        try {
            if (!fileName.exists()) {
                fileName.createNewFile();
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static File getSaveFile(Context context, String folderName) {
        //新建文件夹
//        String folderName = "IOTCrashRegester";
        File sdCardDir;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            LogUtil.logInfo(TAG + "android 10以下创建文件夹");
            sdCardDir = new File(Environment.getExternalStorageDirectory() + "/" + folderName + "/");
            LogUtil.logInfo(TAG + "文件的路径是：" + sdCardDir.getPath());
            if (!sdCardDir.exists()) {
                LogUtil.logInfo("create result" + "创建结果" + sdCardDir.mkdirs());
            } else {
                LogUtil.logInfo(TAG + "文件夹已存在");
            }
        } else {
//            String path = context.getExternalFilesDir(null).getAbsolutePath() + "/IOTCrashRegester/";
//            sdCardDir = new File(path);
//            sdCardDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/IOTCrashRegester/");
            sdCardDir = new File(Environment.getExternalStorageDirectory() + "/" + folderName + "/");
            LogUtil.logInfo(TAG + "文件的路径是：" + sdCardDir.getPath());
            if (!sdCardDir.exists()) {
                LogUtil.logInfo("create result" + "创建结果" + sdCardDir.mkdirs());
            } else {
                LogUtil.logInfo(TAG + "文件夹已存在");
            }
        }

        return sdCardDir;
    }

    /**
     * 创建一个txt文档
     *
     * @return
     */
    public static File createTxt(Context context, String folderName) {
        File sdCardDir = getSaveFile(context, folderName);
        File saveFile = null;
        if (sdCardDir.exists()) {
            //新建文件
            saveFile = new File(sdCardDir, "SerialNumer.txt");
            try {
                if (!saveFile.exists()) {
                    saveFile.createNewFile();
                    LogUtil.logInfo(TAG + "文件不存在");
                } else {
                    LogUtil.logInfo(TAG + "文件已存在" + saveFile.getName() + "  " + saveFile.getPath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return saveFile;
    }

    /**
     * 往txt写入内容
     *
     * @param content
     * @return
     */
    public static boolean writeTxt(String content, Context context, String foloderName) {
//        //新建文件夹
        boolean result = false;
        LogUtil.logInfo(TAG + "数据长度为" + content.length());
        try {
            File saveFile = createTxt(context, foloderName);
            if (saveFile == null) return false;
            else {
                if (!saveFile.exists()) return false;
            }
            final FileOutputStream outStream = new FileOutputStream(saveFile);
            try {
                outStream.write(content.getBytes());
                outStream.flush();
                outStream.close();
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.logError(TAG + "onClick: --------------" + e.toString());
            }
            LogUtil.logInfo(TAG + "写入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 读TXT文件内容
     *
     * @param fileName
     * @return
     */
    public static String readTxtFile(File fileName) throws Exception {
        String result = "";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            try {
                String read = null;
                while ((read = bufferedReader.readLine()) != null) {
                    result = result + read + "\r\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }
        LogUtil.logInfo(TAG + "读取成功" + result);
        return result;
    }


    public static boolean writeTxtFile(String content, File fileName) throws Exception {
        RandomAccessFile mm = null;
        boolean flag = false;
        FileOutputStream o = null;
        try {
            o = new FileOutputStream(fileName);
            o.write(content.getBytes("GBK"));
            o.close();
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (mm != null) {
                mm.close();
            }
        }
        return flag;
    }

    /**
     * 获取文件下载路径
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getDownloadPath(Context context, String fileName) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileName;
    }

    /**
     * 文件删除
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file.exists()) {
            return file.delete();
        } else return false;
    }
}
