package com.lp.knightoneadmin.wechatimageselector.view;

/**
 * @Module :
 * @Comments : Application
 * @Author : KnightOneAdmin
 * @CreateDate : 16/9/11
 * @ModifiedBy : KnightOneAdmin
 * @ModifiedDate: 下午3:24
 * @Modified : Application
 */
public class Application extends android.app.Application {
    private static Application instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
    public synchronized  static Application getInstance(){
        if(instance==null){
            instance=new Application();
        }
        return  instance;
    }
}
