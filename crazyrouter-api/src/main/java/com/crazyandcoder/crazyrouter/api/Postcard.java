package com.crazyandcoder.crazyrouter.api;

import android.os.Bundle;

import com.crazyandcoder.crazyrouter.annotation.RouteMeta;

/**
 * @ClassName: Postcard
 * @Description: 用于封装 目的地的各种操作，比如跳转Activity，切换Fragment，或者执行 业务模块暴露出来的service
 * @Author: crazyandcoder
 * @email: lijiwork@sina.com
 * @CreateDate: 2020/5/5 8:12 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/5/5 8:12 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Postcard extends RouteMeta {

    /**
     * 为了模块间数据交互而预备的属性，先放着
     */
    private IProvider provider;

    /**
     * activity跳转的时候有可能会携带参数，这里先预留一个属性
     */
    private Bundle bundle;

    /**
     * Activity的启动模式，在java里面是用int值表示的，所以这里也留个字段
     */
    private int flag = FLAG_DEFAULT;

    public static final int FLAG_DEFAULT = -1;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int mFlag) {
        this.flag = mFlag;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public IProvider getProvider() {
        return provider;
    }

    public void setProvider(IProvider provider) {
        this.provider = provider;
    }

    public Postcard(String path) {
        this.path = path;
    }

    /**
     * 开始执行任务，是跳转Activity呢？还是切换Fragment呢？还是 执行业务模块暴露的服务呢。。。
     */
    public Object navigation() {
        return CRouter.getInstance().navigation(this);
    }
}
