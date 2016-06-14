package com.sobey.common.utils.others;

/**
 * Created by MagicBean on 2015/07/23 16:16:03
 */
public class FileUtils {

    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
