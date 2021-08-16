package com.srthink.iotboxaar.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.storage.StorageManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.srthink.iotboxaar.models.StorageInfo;
import com.srthink.iotboxaar.models.TotalStorageInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * app相关工具类
 *
 * @author liwanlian
 * @date 2020/12/31 11:37
 */
public class AppUtils {
    private static final String TAG = "AppUtils";

    /**
     * 判断权限集合
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean lacksPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission, context)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     *
     * @param permission
     * @param context
     * @return
     */
    public static boolean lacksPermission(String permission, Context context) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    /**
     * 获取app的具体版本名（eg:1.0.1)
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = "";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 比较两版本哪个是最新
     *
     * @param netVersion   网络检测到的版本
     * @param localVersion 系统当前的版本
     * @return
     */
    public static boolean isBiggerVersion(String netVersion, String localVersion) {
        int net = version2Int(netVersion);
        int local = version2Int(localVersion);
        return local < net;
    }

    private static int version2Int(String version) {
        String str = version.replace(".", "");
        return Integer.parseInt(str);
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return "Android " + Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取当前手机系统的语言
     *
     * @return
     */
    public static String getSysCurLan() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取手机的语言列表
     *
     * @return
     */
    public static Locale[] getSysLanList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取手机系统的厂商
     *
     * @return
     */
    public static String getPhoneBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机的IMEI（手机的唯一标识码  15位   各个手机的标识码是不同的）   国际移动身份识别码  International Mobile Equipment Identity
     * android 6.0以上需要动态申明权限
     *
     * @param ctx
     * @return
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

    /**
     * 获取手机的IMSI
     *
     * @param ctx
     * @return
     */
    public static String getIMSI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getSubscriberId();
        }
        return null;
    }

//    private void setPhoneStateManifest() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // toast("需要动态获取权限");
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
//        } else {
//            // toast("不需要动态获取权限");
//            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//            String IMEI = tm.getDeviceId();
//            Log.i(TAG, "IMEI:" + IMEI);
//        }
//    }

    //获取ip地址
    public static String getIpAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                return ipAddress;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有限网络
                return getLocalIp();
            }
        }
        return null;
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    // 获取有限网IP
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "0.0.0.0";
    }

    //获取设备的mac地址
    public static String getMacAddressFromIp(Context context) {
        String mac_s = "";
        StringBuilder buf = new StringBuilder();
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getIpAddress(context)));
            mac = ne.getHardwareAddress();
            for (byte b : mac) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            mac_s = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac_s;
    }

    public static String getEquipCpuMsg(Context context) {
        return Build.CPU_ABI + "，" + getMaxCpuFreq() + "GHz*" + getNumberOfCPUCores();
    }

    /**
     * CPU核数
     */
    public static int getNumberOfCPUCores() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            return 1;
        }
        int cores;
        try {
            cores = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (SecurityException e) {
            cores = 9;
        } catch (NullPointerException e) {
            cores = 9;
        }
        return cores;
    }

    private static final FileFilter CPU_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getName();
            if (path.startsWith("cpu")) {
                for (int i = 3; i < path.length(); i++) {
                    if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    };

    //CPU最大运行频率
    public static double getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        double a = Integer.parseInt(result.trim());
        double b = a / 1000000;
        BigDecimal c = new BigDecimal(b);
        double d = c.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }

    public static int getDisplaysize(Context context) {
        DisplayManager displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        int size = displayManager.getDisplays().length;

        return size;
    }

    /**
     *   * 获取android当前可用运行内存大小
     *   * @param context
     *   *
     */
    public static String getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
// mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }


    /**
     *   * 获取android总运行内存大小
     *   * @param context
     *   *
     */
    public static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            // 获得系统总内存，单位是KB
            int i = Integer.valueOf(arrayOfString[1]).intValue();
            //int值乘以1024转换为long类型
            initial_memory = new Long((long) i * 1024);
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    public static String getTotalRam(Context context) {//GB
        String path = "/proc/meminfo";
        String firstLine = null;
        int totalRam = 0;
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader, 8192);
            firstLine = br.readLine().split("\\s+")[1];
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (firstLine != null) {
            totalRam = (int) Math.ceil((new Float(Float.valueOf(firstLine) / (1024 * 1024)).doubleValue()));
        }

        return totalRam + "GB";//返回1GB/2GB/3GB/4GB
    }

    public static TotalStorageInfo queryStroageInfo(Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        TotalStorageInfo totalStorageInfo = new TotalStorageInfo();
        StorageInfo storageInfo = new StorageInfo();
        List<StorageInfo> storageInfos = new ArrayList<>();
        try {
            Method getVolumes = StorageManager.class.getDeclaredMethod("getVolumes");//6.0
            List<Object> getVolumeInfo = (List<Object>) getVolumes.invoke(storageManager);
            long total = 0L, used = 0L;
            int version = Build.VERSION.SDK_INT;
            long unit = 0;
            for (Object obj : getVolumeInfo) {

                Field getType = obj.getClass().getField("type");
                int type = getType.getInt(obj);

                Log.d(TAG, "type: " + type);
                if (type == 1) {//TYPE_PRIVATE

                    long totalSize = 0L;

                    //获取内置内存总大小
                    if (version >= Build.VERSION_CODES.O) {//8.0也可以不做这个判断
                        unit = 1000;
                        Method getFsUuid = obj.getClass().getDeclaredMethod("getFsUuid");
                        String fsUuid = (String) getFsUuid.invoke(obj);
                        totalSize = getTotalSize(context, fsUuid);//8.0 以后使用
                    } else if (version >= Build.VERSION_CODES.N_MR1) {//7.1.1
                        //5.0 6.0 7.0没有
                        Method getPrimaryStorageSize = StorageManager.class.getMethod("getPrimaryStorageSize");
                        totalSize = (long) getPrimaryStorageSize.invoke(storageManager);
                    }
                    long systemSize = 0L;
                    Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable");
                    boolean readable = (boolean) isMountedReadable.invoke(obj);
                    if (readable) {
                        Method file = obj.getClass().getDeclaredMethod("getPath");
                        File f = (File) file.invoke(obj);

                        if (totalSize == 0) {
                            totalSize = f.getTotalSpace();
                        }
//                        String _msg = "剩余总内存：" + getUnit(f.getTotalSpace(), unit) + "\n可用内存：" + getUnit(f.getFreeSpace(), unit) + "\n已用内存：" + getUnit(f.getTotalSpace() - f.getFreeSpace(), unit);
//                        Log.d(TAG, _msg);

                        systemSize = totalSize - f.getTotalSpace();
                        used += totalSize - f.getFreeSpace();
                        total += totalSize;
                        LogUtil.logInfo(TAG + "总内存" + getUnit(total) + " 可用内存" + getUnit(f.getFreeSpace()));
                        storageInfo.totalStorage = getUnit(total);
                        storageInfo.usedStorage = getUnit(total - f.getFreeSpace());
                        totalStorageInfo.storageInfo = storageInfo;//内置内存的使用情况
                    }
//                    Log.d(TAG, "totalSize = " + getUnit(totalSize, unit) + " ,used(with system) = " + getUnit(used, unit) + " ,free = " + getUnit(totalSize - used, unit));

                } else if (type == 0) {//TYPE_PUBLIC
                    //外置存储
                    Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable");
                    boolean readable = (boolean) isMountedReadable.invoke(obj);
                    if (readable) {
                        long total1 = 0L, used1 = 0L;
                        Method file = obj.getClass().getDeclaredMethod("getPath");
                        File f = (File) file.invoke(obj);
//                        used += f.getTotalSpace() - f.getFreeSpace();
//                        total += f.getTotalSpace();
                        used1 += f.getTotalSpace() - f.getFreeSpace();
                        total1 += f.getTotalSpace();
                        StorageInfo storageInfo1 = new StorageInfo();
                        storageInfo1.usedStorage = getUnit(used1);
                        storageInfo1.totalStorage = getUnit(total1);
                        storageInfos.add(storageInfo1);
                    }
                } else if (type == 2) {//TYPE_EMULATED

                }
            }
//            Log.d(TAG, "总内存 total = " + getUnit(total, 1000) + " ,已用 used(with system) = " + getUnit(used, 1000) + "\n可用 available = " + getUnit(total - used, 1000));
            LogUtil.logInfo(TAG + "总内存" + total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        totalStorageInfo.storageInfos = storageInfos;
        return totalStorageInfo;
    }

    /**
     * API 26 android O
     * 获取总共容量大小，包括系统大小
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getTotalSize(Context context, String fsUuid) {
        try {
            UUID id;
            if (fsUuid == null) {
                id = StorageManager.UUID_DEFAULT;
            } else {
                id = UUID.fromString(fsUuid);
            }
            StorageStatsManager stats = context.getSystemService(StorageStatsManager.class);
            return stats.getTotalBytes(id);
        } catch (NoSuchFieldError | NoClassDefFoundError | NullPointerException | IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static String formatConversion(double result) {
        if (result == 0) {
            return "0.00";
        } else {
            DecimalFormat df = new DecimalFormat("#.00");
            df.format(result);//保留两位小数
            if (result < 1) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(new DecimalFormat("#.00").format(result));
                return sb.toString();
            } else {
                return new DecimalFormat("#.00").format(result);
            }

        }
    }

    public static String formatConversionThree(float result) {
        if (result == 0) {
            return "0.000";
        } else {
            DecimalFormat df = new DecimalFormat("#.000");
            df.format(result);//保留三位小数
            if (result < 1) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(new DecimalFormat("#.000").format(result));
                return sb.toString();
            } else {
                return new DecimalFormat("#.000").format(result);
            }

        }
    }

    private static String getUnit(long num) {
        return String.valueOf(formatConversion((double) (num / 1000.0 / 1000.0 / 1000.0)));
    }

    /**
     * @param context 上下文
     * @param content 内容
     */
    public static void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context context, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }


    public static void silentInstallApkByReflect(Context context, String apkPath) {
        PackageManager packageManager = context.getPackageManager();
        Class<?> pmClz = packageManager.getClass();
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Class<?> aClass = Class.forName("android.app.PackageInstallObserver");
                Constructor<?> constructor = aClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object installObserver = constructor.newInstance();
                Method method =
                        pmClz.getDeclaredMethod("installPackage", Uri.class, aClass, int.class, String.class);
                method.setAccessible(true);
                method.invoke(packageManager, Uri.fromFile(new File(apkPath)), installObserver, 2, null);
            } else {
                Method method = pmClz.getDeclaredMethod("installPackage", Uri.class,
                        Class.forName("android.content.pm.IPackageInstallObserver"), int.class, String.class);
                method.setAccessible(true);
                method.invoke(packageManager, Uri.fromFile(new File(apkPath)), null, 2, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
