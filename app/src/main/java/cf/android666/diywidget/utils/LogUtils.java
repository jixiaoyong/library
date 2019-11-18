package cf.android666.diywidget.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Formatter;


/**
 * author:
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-09-07
 * description: 日志打印类
 */
public class LogUtils {

    private static final ThreadLocal thread_local_formatter = new ThreadLocal() {
        protected ReusableFormatter initialValue() {
            return new ReusableFormatter();
        }
    };
    // 容许在logcat中打印日志的类型，默认是true，设置为false则不打印
    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWtf = true;
    /**
     * 单个log文件最大大小 1MB (单位kb)
     */
    public static long SINGLE_LOG_MAX_SIZE = 1024L;
    /**
     * 日志文件最大占用内存 100Mb (单位kb)
     */
    public static long MAX_LOG_FILE_SIZE = 100 * SINGLE_LOG_MAX_SIZE;
    /**
     * 日志最大保存时间 30天（单位 秒）
     * 60*60*24*30L s
     */
    public static long MAX_SAVED_TIME = 2_592_000;
    /**
     * 自定义的logger
     */
    public static CustomLogger customLogger;
    /**
     * 自定义TAG
     */
    private static String CUSTOMIZE_TAG = "";

    /**
     * 自定义日志打印的tag
     *
     * @param caller 堆栈元素构建对象
     * @return 打印日志的tag
     */
    private static String generateTag(StackTraceElement caller) {

        if (!"".equals(getCustomizeTag())) {
            return getCustomizeTag();
        }

        String packageName = LogUtils.class.getPackage().getName();
        if (LogCollector.getContext() != null) {
            packageName = LogCollector.getContext().getPackageName();
        }

        String tag = "%s.%s(Line:%d)"; // 占位符
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(),
                caller.getLineNumber()); // 替换
        tag = TextUtils.isEmpty(packageName) ? tag : packageName + "/" + tag;
        return tag;
    }

    public static String getCustomizeTag() {
        return CUSTOMIZE_TAG;
    }

    public static void setCustomizeTag(String customizeTag) {
        LogUtils.CUSTOMIZE_TAG = customizeTag;
    }

    public static void d(String content) {
        if (!allowD) {
            return;
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content);
        } else {
            Log.d(tag, content);
        }

        if (LogCollector.isSaveTagLog()) {
            point(tag, content);
        }
    }

    public static void d(String tag, String msg) {
        if (!allowD) {
            return;
        }

        if (customLogger != null) {
            customLogger.d(tag, msg);
        } else {
            Log.d(tag, msg);
        }

        if (LogCollector.isSaveTagLog()) {
            point(tag, msg);
        }
    }

    public static void d(String content, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content, tr);
        } else {
            Log.d(tag, content, tr);
        }

        if (LogCollector.isSaveTagLog()) {
            point(tag, content);
        }
    }

    public static void e(String content) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content);
        } else {
            Log.e(tag, content);
        }
        if (LogCollector.isSaveTagLog()) {
            point(tag, content);
        }
    }

    public static void e(String tag, String msg) {
        if (!allowE)
            return;

        if (customLogger != null) {
            customLogger.e(tag, msg);
        } else {
            Log.e(tag, msg);
        }
        if (LogCollector.isSaveTagLog()) {
            point(tag, msg);
        }
    }

    public static void e(String content, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content, tr);
        } else {
            Log.e(tag, content, tr);
        }
        if (LogCollector.isSaveTagLog()) {
            point(tag, tr.getMessage());
        }
    }

    public static void i(String content) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content);
        } else {
            Log.i(tag, content);
        }

        if (LogCollector.isSaveTagLog()) {
            point(tag, content);
        }
    }

    public static void i(String tag, String msg) {
        if (!allowI)
            return;

        if (customLogger != null) {
            customLogger.i(tag, msg);
        } else {
            Log.i(tag, msg);
        }

        if (LogCollector.isSaveTagLog()) {
            point(tag, msg);
        }
    }

    public static void i(String content, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content, tr);
        } else {
            Log.i(tag, content, tr);
        }

        if (LogCollector.isSaveTagLog()) {
            point(tag, content);
        }
    }

    public static void v(String content) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content);
        } else {
            Log.v(tag, content);
        }
        if (LogCollector.isSaveTagLog()) {
            point(tag, content);
        }
    }

    public static void v(String tag, String msg) {
        if (!allowV)
            return;

        if (customLogger != null) {
            customLogger.v(tag, msg);
        } else {
            Log.v(tag, msg);
        }
        if (LogCollector.isSaveTagLog()) {
            point(tag, msg);
        }
    }

    public static void v(String content, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content, tr);
        } else {
            Log.v(tag, content, tr);
        }
        if (LogCollector.isSaveTagLog()) {
            point(tag, content);
        }
    }

    public static void w(String content) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content);
        } else {
            Log.w(tag, content);
        }
        if (LogCollector.isSaveTagLog()) {
            point(tag, content);
        }
    }

    public static void w(String tag, String msg) {
        if (!allowW)
            return;

        if (customLogger != null) {
            customLogger.w(tag, msg);
        } else {
            Log.w(tag, msg);
        }
        if (LogCollector.isSaveTagLog()) {
            point(tag, msg);
        }
    }

    public static void w(String content, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content, tr);
        } else {
            Log.w(tag, content, tr);
        }
        if (LogCollector.isSaveTagLog()) {
            point(tag, content);
        }
    }

    public static void w(Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, tr);
        } else {
            Log.w(tag, tr);
        }
    }

    public static void wtf(String content) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content);
        } else {
            Log.wtf(tag, content);
        }

        if (LogCollector.isSaveTagLog()) {
            point(tag, content);
        }
    }

    public static void wtf(String tag, String msg) {
        if (!allowWtf)
            return;

        if (customLogger != null) {
            customLogger.wtf(tag, msg);
        } else {
            Log.wtf(tag, msg);
        }

        if (LogCollector.isSaveTagLog()) {
            point(tag, msg);
        }
    }

    public static void wtf(String content, Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content, tr);
        } else {
            Log.wtf(tag, content, tr);
        }

        if (LogCollector.isSaveTagLog()) {
            point(tag, content);
        }
    }

    public static void wtf(Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, tr);
        } else {
            Log.wtf(tag, tr);
        }
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    /**
     * 保存tag日志到本地
     *
     * @param tag tag标签
     * @param msg 消息实体
     */
    public static void point(String tag, String msg) {
        String path = LogCollector.getLogTagPath();
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        checkCountAndTimeout(dir);

        String log = getCurrentLogTime() + "\t" + tag + "\t" + msg + "\n";
        String tagLogName = LogCollector.TAG_LOG_FILE_NAME;
        if (TextUtils.isEmpty(tagLogName)) {
            tagLogName = LogCollector.getTagLogFileName();
        }

        if (checkLogSize(dir.getPath() + "/" + LogCollector.TAG_LOG_FILE_NAME)) {
            tagLogName = LogCollector.getTagLogFileName();
        }
        File file = new File(dir, tagLogName);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(log);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查文件是否过期或者占用内存过大
     * <p>
     * 如果占用内存过大则删除最旧的文件
     * 如果文件保存>30天则删除
     *
     * @param dir 日志保存路径
     */
    private static void checkCountAndTimeout(File dir) {
        File[] logFiles = dir.listFiles();
        Arrays.sort(logFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Long.compare(o1.lastModified(), o2.lastModified());
            }
        });
        //单位kb
        long totalSize = 0L;
        for (File logFile : logFiles) {
            //删除过期文件
            if ((logFile.lastModified() - System.currentTimeMillis()) / 1_000 > MAX_SAVED_TIME) {
                logFile.delete();
            } else {
                totalSize += logFile.length() / 1024;
            }
        }
        //内存不足，删除最旧的文件
        if (totalSize > MAX_LOG_FILE_SIZE) {
            logFiles[logFiles.length - 1].delete();
            checkCountAndTimeout(dir);
        }

    }

    private static boolean checkLogSize(String path) {
        File logFile = new File(path);
        if (logFile.exists() && logFile.isFile()) {
            return logFile.length() > SINGLE_LOG_MAX_SIZE;
        }
        return false;
    }

    private static String getCurrentLogTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        return simpleDateFormat.format(date);
    }

    public static String format(String msg, Object... args) {
        ReusableFormatter formatter = (ReusableFormatter) thread_local_formatter.get();
        return formatter.format(msg, args);
    }

    /**
     * 一键设置是否打印日志
     *
     * @param isAllow
     */
    public static void setAllowLog(boolean isAllow) {
        allowD = isAllow;
        allowE = isAllow;
        allowI = isAllow;
        allowV = isAllow;
        allowW = isAllow;
        allowWtf = isAllow;
    }

    /**
     * 根据配置文件决定是否打印日志
     *
     * @param path
     * @return
     */
    public static boolean checkPrintLog(String path) {

        if (TextUtils.isEmpty(path)) {
            return false;
        }

        File file = new File(path);
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            return false;
        }

        if (file.length() <= 0) {
            return true;
        }

        InputStreamReader read = null;//考虑到编码格式
        try {
            read = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(read);
            String readLine = "";
            while ((readLine = bufferedReader.readLine()) != null) {
                if (readLine.contains("allowD=")) {
                    if (readLine.length() > "allowD=".length()) {
                        allowD = Boolean.parseBoolean(readLine.split("=")[1]);
                    }
                } else if (readLine.contains("allowE=")) {
                    if (readLine.length() > "allowE=".length()) {
                        allowE = Boolean.parseBoolean(readLine.split("=")[1]);
                    }
                } else if (readLine.contains("allowI=")) {
                    if (readLine.length() > "allowI=".length()) {
                        allowI = Boolean.parseBoolean(readLine.split("=")[1]);
                    }
                } else if (readLine.contains("allowV=")) {
                    if (readLine.length() > "allowV=".length()) {
                        allowV = Boolean.parseBoolean(readLine.split("=")[1]);
                    }
                } else if (readLine.contains("allowW=")) {
                    if (readLine.length() > "allowW=".length()) {
                        allowW = Boolean.parseBoolean(readLine.split("=")[1]);
                    }
                } else if (readLine.contains("allowWtf=")) {
                    if (readLine.length() > "allowWtf=".length()) {
                        allowWtf = Boolean.parseBoolean(readLine.split("=")[1]);
                    }
                }
            }
            read.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public interface CustomLogger {
        void d(String tag, String content);

        void d(String tag, String content, Throwable tr);

        void e(String tag, String content);

        void e(String tag, String content, Throwable tr);

        void i(String tag, String content);

        void i(String tag, String content, Throwable tr);

        void v(String tag, String content);

        void v(String tag, String content, Throwable tr);

        void w(String tag, String content);

        void w(String tag, String content, Throwable tr);

        void w(String tag, Throwable tr);

        void wtf(String tag, String content);

        void wtf(String tag, String content, Throwable tr);

        void wtf(String tag, Throwable tr);
    }

    /**
     * A little trick to reuse a formatter in the same thread
     */
    private static class ReusableFormatter {

        private Formatter formatter;
        private StringBuilder builder;

        public ReusableFormatter() {
            builder = new StringBuilder();
            formatter = new Formatter(builder);
        }

        public String format(String msg, Object... args) {
            formatter.format(msg, args);
            String s = builder.toString();
            builder.setLength(0);
            return s;
        }
    }
}