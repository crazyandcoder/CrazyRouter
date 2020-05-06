package com.crazyandcoder.crazyrouter.api;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

/**
 * @ClassName: CRouter
 * @Description: java类作用描述
 * @Author: crazyandcoder
 * @email: lijiwork@sina.com
 * @CreateDate: 2020/5/5 7:45 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/5/5 7:45 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CRouter {

    private static Handler mHandler;

    private volatile static CRouter mInstance;

    private static Application mContext;

    private CRouter() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 路由，在一个app中只能存在一个实例，所以，作为单例来写
     *
     * @return
     */
    public static CRouter getInstance() {
        //外层if判空，是为了防止没有意义的判定锁，略微增加程序效率。
        //因为如果没有这一层判定，每一次getInstance都会判定锁是否被占用,造成没必要的效率降低
        if (mInstance == null) {
            //对CRouter加锁，是为了让同一时间只有一个线程能够访问这个类
            synchronized (CRouter.class) {
                //能同一时间进到这里的只有一个线程，这个线程判定静态变量是不是空，是空，则创建新对象
                if (mInstance == null) {
                    mInstance = new CRouter();
                }
            }
        }
        return mInstance;
    }

    public static void init(Application application) {
        mContext = application;

    }

    /**
     * 跳转的唯一方法
     *
     * @param postcard
     * @return
     */
    Object navigation(Postcard postcard) {
        //这里拿到的postcard可能是数据不完整的，所以，调用LogisticsCenter的complete方法对属性进行完善，以便于下面的跳转或者其他操作
        LogisticsCenter.complete(postcard);
        switch (postcard.getRouteType()) {
            case ACTIVITY:
                //如果是Activity，那就跳吧
                return startActivity(postcard);
            case FRAGMENT:
                //如果是Fragment，那就切换吧
//                return switchFragment(postcard);
            case PROVIDER:
                //如果是Provider，那就执行业务逻辑
                //那就直接返回provider对象
                return postcard.getProvider();
            default:
                break;
        }
        return null;
    }

    /**
     * 释放资源
     */
    public static void release() {

    }


    /**
     * startActivity
     *
     * @param postcard
     * @return
     */
    private Object startActivity(Postcard postcard) {
        Class<?> cls = postcard.getDestination();
        if (cls == null) {
            if (cls == null) {
                throw new RuntimeException("没找到对应的activity，请检查路由寻址标识是否写错");
            }
        }
        final Intent intent = new Intent(mContext, cls);
        //如果不是初始值，也就是说，flag值被更改过，那就用更改后的值
        if (Postcard.FLAG_DEFAULT != postcard.getFlag()) {
            intent.setFlags(postcard.getFlag());
        } else {
            //如果沒有设定启动模式，即 flag值没有被更改，就用常规模式启动
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                mContext.startActivity(intent);
            }
        });

        return null;
    }


    /**
     * 在UI主线程中执行相关操作
     *
     * @param runnable
     */
    private void runInMainThread(Runnable runnable) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }
}
