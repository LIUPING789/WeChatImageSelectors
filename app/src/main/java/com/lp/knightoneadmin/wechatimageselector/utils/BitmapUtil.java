package com.lp.knightoneadmin.wechatimageselector.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

/**
 * @Module :
 * @Comments : BitmapUtil
 * @Author : KnightOneAdmin
 * @CreateDate : 16/9/11
 * @ModifiedBy : KnightOneAdmin
 * @ModifiedDate: 下午3:35
 * @Modified : BitmapUtil
 */
public class BitmapUtil {
    /**
     * 通过magLoading加载图片
     *
     * @param Path
     * @param imageView
     */
    public static void setImageViewByImagLoading(Context mContext, String Path, ImageView imageView) {
        Log.e("BitmapUtil-->Path:", Path);
        String imagePath;
        if (Path.trim().toString().startsWith("https://") || Path.trim().toString().startsWith("http://") || Path.trim().toString().startsWith("file://")) {
            imagePath = Path.trim().toString();
        } else {
            imagePath = "file://" + Path.trim().toString();
        }
        PicassoUtils.setAvatarImg(mContext, imagePath, imageView);
        Log.e("BitmapUtil-->imagePath:", imagePath);
    }
}
