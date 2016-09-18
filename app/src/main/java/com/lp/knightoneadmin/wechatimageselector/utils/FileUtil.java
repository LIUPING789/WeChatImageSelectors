package com.lp.knightoneadmin.wechatimageselector.utils;

import android.os.Environment;

import java.io.File;

/**
 * @Module :
 * @Comments : FileUtil
 * @Author : KnightOneAdmin
 * @CreateDate : 16/9/11
 * @ModifiedBy : KnightOneAdmin
 * @ModifiedDate: 下午3:58
 * @Modified : FileUtil
 */
public class FileUtil {
    private static final String FILE_ROOT = Environment.getExternalStorageDirectory() + "/RCB";//根目录
    public static File getFile(String filename) {
        File file = new File(FILE_ROOT, filename);
        file.getParentFile().mkdirs();
        return file;
    }
}
