package com.lp.knightoneadmin.wechatimageselector.utils;

import android.content.Context;
import android.widget.Toast;

import com.lp.knightoneadmin.wechatimageselector.view.Application;

/**
 * @Module :
 * @Comments : MyToast
 * @Author : KnightOneAdmin
 * @CreateDate : 16/9/11
 * @ModifiedBy : KnightOneAdmin
 * @ModifiedDate: 下午3:22
 * @Modified : MyToast
 */
public class MyToast {

    private static Toast mToast = null;
    public static Context mContext = Application.getInstance();

    public MyToast() {
    }

    public static void showText(CharSequence text) {
        if (mContext != null) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            try {
                mToast.show();
            } catch (Throwable var2) {
                var2.printStackTrace();
            }

        }
    }

    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
