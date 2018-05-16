package com.laipengxu.AndroidHotFixDemo.utils;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/6/29.
 */
public class FileUtils {
    @Nullable
    public static File creatFile(String path) {
        File file = new File(path);
        // 判断父目录是否存在
        if (!file.getParentFile().exists()) {
            // 创建父目录
            file.getParentFile().mkdirs();
        }

        // 判断文件是否存在
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }

        return file;
    }
}
