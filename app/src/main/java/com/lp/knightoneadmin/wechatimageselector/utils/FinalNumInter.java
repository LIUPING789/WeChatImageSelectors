package com.lp.knightoneadmin.wechatimageselector.utils;

/**
 * @Module :
 * @Comments : FinalNumInter
 * @Author : KnightOneAdmin
 * @CreateDate : 16/9/11
 * @ModifiedBy : KnightOneAdmin
 * @ModifiedDate: 下午3:29
 * @Modified : FinalNumInter
 */
public interface FinalNumInter {
    // activity中的状态
    int STATE_INIT = -1;
    int STATE_STOPPED = 0;
    int STATE_PAUSED = 1;
    int STATE_RUNNING = 2;
    int STATE_DESTROYED = 999;
    // 手机号码长度
    int PHONE_CODE_LENGTH = 11;
    // 验证码长度
    int VERIFYCODE_LENGTH = 6;
    String IMAGE_UNSPECIFIED = "image/*";
    int PHOTO_GRAPH = 1;// 拍照
    int PHOTO_ZOOM = 2; // 相册
    int PHOTO_RESOULT = 3;// 结果
    String PHOTO_NAME = "me_header.jpg";//System.currentTimeMillis()+".jpg";
    //	String PHOTO_ADDRESS = Environment.getExternalStorageDirectory() + "/" + PHOTO_NAME;
    int PHOTO_ZOOM_OUTPUT_X = 120;// 裁剪图片的宽
    int PHOTO_ZOOM_OUTPUT_Y = 120;// 裁剪图片高
    int BITMAP_STANDRD_WIDTH = 100;
    int PHOTORESULT = -1;
    int PHOTOCODE = 100;
}
