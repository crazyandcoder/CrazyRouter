package com.crazyandcoder.crazyrouter.api;

import android.util.Log;

import com.crazyandcoder.crazyrouter.annotation.RouteMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: WareHouse
 * @Description: 货舱，存放 所有map
 * @Author: crazyandcoder
 * @email: lijiwork@sina.com
 * @CreateDate: 2020/5/5 7:53 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/5/5 7:53 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class WareHouse {

    /**
     * 用routes来存，不去具体区分Activity，还是Fragment,或者是Provider
     */
    public static Map<String, RouteMeta> routeMap = new HashMap<>();

    /**
     * 专门用来存放Provider对象，防止重复创建
     */
    public static Map<Class, IProvider> providerMap = new HashMap<>();

    public static void traversalCommMap() {
        for (String key : routeMap.keySet()) {
            Log.d("traversalMap", key + "   --------   " + routeMap.get(key).getDestination() + "-" + routeMap.get(key).getRouteType());
        }
    }


    /**
     * 释放内存
     */
    public static void clear() {
        routeMap.clear();
        providerMap.clear();
    }

}
