package com.yixihan.util;

import cn.hutool.log.StaticLog;

import java.io.File;
import java.util.Objects;

/**
 * 输出目录清空工具
 *
 * @author yixihan
 * @date 2022/11/10 9:21
 */
public class FileUtils {

    public static void emptyDir(File dir) {
        if (!dir.isDirectory ()) {
            return;
        }
        File[] files = dir.listFiles ();
        assert files != null;
        for (File file : files) {
            deleteRecursive (file);
        }
    }

    public static void deleteRecursive(File file) {
        while (file.exists ()) {
            deleteLeafFile (file);
        }
    }

    
    public static void deleteLeafFile(File file) {
        if (file.isFile ()) {
            boolean delete = file.delete ();
            if (!delete) {
                StaticLog.info ("目录清空失败!");
            }
            return;
        }
        if (Objects.requireNonNull (file.list ()).length == 0) {
            boolean delete = file.delete ();
            if (!delete) {
                StaticLog.info ("目录清空失败!");
            }
            return;
        }
        for (File file1 : Objects.requireNonNull (file.listFiles ())) {
            deleteLeafFile (file1);
        }
    }
}
