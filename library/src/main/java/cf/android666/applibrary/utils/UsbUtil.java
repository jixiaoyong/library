package cf.android666.applibrary.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cf.android666.applibrary.logger.Logger;

/**
 *
 */
public class UsbUtil {

    private static final String TAG = "tag";
    private static final String USB_PATH_6 = "";


    /**
     * Android 6.0获取外置U盘路径
     *
     * @param mContext
     * @return
     */
    public static List<String> getUsbStoragePath(Context mContext) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getUsbStoragePathApi6(mContext);
        } else {
            return getUsbStoragePathApi4(mContext);
        }
    }

    private static List<String> getUsbStoragePathApi4(Context mContext) {
        StorageManager storageManager = (StorageManager) mContext.getSystemService(Context
                .STORAGE_SERVICE);

        List<String> target = new ArrayList<>();

        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
            Method getVolumeStateMethod = StorageManager.class.getMethod("getVolumeState", String.class);
            getVolumePathsMethod.setAccessible(true);
            getVolumeStateMethod.setAccessible(true);
            Object[] params = {};
            Object invoke = getVolumePathsMethod.invoke(storageManager, params);
            ArrayList paths = new ArrayList(Arrays.asList((String[]) invoke));

            for (int i = 0; i < paths.size(); i++) {
                String state = (String) getVolumeStateMethod.invoke(storageManager, paths.get(i).toString());
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    Logger.i("**********file exists " + paths.get(i));
                    target.add((String) paths.get(i));
                }
            }

        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (target.contains(rootPath)) {
            target.remove(rootPath);
        }
        return target;
    }

    /**
     * Android 6.0获取外置U盘路径
     *
     * @param mContext
     * @return
     */
    public static List<String> getUsbStoragePathApi6(Context mContext) {
        List<String> target = new ArrayList<>();
        String path6 = getUsb6Path(mContext);

        if (path6 != null && !"".equals(path6)) {
            target.add(path6);
            return target;
        }

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> volumeInfoClazz = null;
        target = new ArrayList<>();
        try {
            volumeInfoClazz = Class.forName("android.os.storage.VolumeInfo");
            Method getType = volumeInfoClazz.getMethod("getType");
            Method getPath = volumeInfoClazz.getMethod("getPath");
            Method getVolumes = mStorageManager.getClass().getMethod("getVolumes");

            List<Object> result = (List<Object>) getVolumes.invoke(mStorageManager);
            for (int i = 0; i < result.size(); i++) {
                Object volumeInfoElement = result.get(i);
                int m_type = (int) getType.invoke(volumeInfoElement);
                File file = (File) getPath.invoke(volumeInfoElement);
                if (file != null) {
                    Log.d("pp", "type=" + m_type + "   path=" + file.getPath());
                    if (m_type == 0 && file.exists()) {
                        target.add(file.getPath());
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * 获取usb的根路径
     * 用df命令获取所有挂在的存储，过滤出usb的存储，再筛选出容量最大的为usb的根路径
     *
     * @return usb根路径
     */
    public static String getUsb6Path(Context ctx) {
        String mount = "";
        long maxFreeSize = 0;
        File usbMountDir = new File(USB_PATH_6);
        if (usbMountDir.listFiles() == null || usbMountDir.listFiles().length <= 0) {
            return mount;
        }
        try {
            //耗时操作，如果耗时太长放到子线程运行
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("df");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (containPath(line, usbMountDir.listFiles())) {
                    Log.d(TAG, "line = " + line);
                    List<String> tmp = new ArrayList<String>();
                    for (String str : line.split(" ")) {
                        if (str != null && str.length() != 0 && !" ".equals(str)) {
                            tmp.add(str);
                        }
                    }
                    String[] data = tmp.toArray(new String[0]);
                    String freeSize = data[3];
                    long size = 0;
                    if (freeSize.endsWith("G")) {
                        //GB 转换成KB
                        size = (long) (Double.parseDouble(freeSize.substring(0, freeSize.length() - 1)) * 1024 * 1024);
                    } else if (freeSize.endsWith("M")) {
                        //MB 转换成KB
                        size = (long) (Double.parseDouble(freeSize.substring(0, freeSize.length() - 1)) * 1024);
                    } else if (freeSize.endsWith("K")) {
                        //KB
                        size = (long) Double.parseDouble(freeSize.substring(0, freeSize.length() - 1));
                    }
                    Log.d(TAG, "size = " + size);
                    if (size > maxFreeSize) {
                        mount = data[0];
                        maxFreeSize = size;
                    }
                }
            }
            is.close();
            isr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("DiskPathReceiver", "mount = " + mount);
        return mount;
    }

    private static boolean containPath(String line, File[] files) {
        for (File f : files) {
            if (line.contains(f.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }


}
