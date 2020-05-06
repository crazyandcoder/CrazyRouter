package com.crazyandcoder.crazyrouter.annotation;

/**
 * @ClassName: RouteType
 * @Description: 路由框架，支持ACTIVITY,FRAGMENT,PROVIDER 三种信息互通格式
 * @Author: crazyandcoder
 * @email: lijiwork@sina.com
 * @CreateDate: 2020/5/5 4:12 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/5/5 4:12 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

/**
 * 路由框架，支持ACTIVITY,FRAGMENT,PROVIDER 三种信息互通格式
 *
 * @author admin
 */
public enum RouteType {
    ACTIVITY("android.app.Activity"),
    FRAGMENT("android.app.Fragment"),
    FRAGMENT_V4("android.support.v4.app.Fragment"),
    PROVIDER("com.crazyandcoder.crazyrouter.api.IProvider"),
    UNKNOWN("Unknown route type");


    /**
     * 提供一个全限定类名
     */
    private String className;

    public String getClassName() {
        return className;
    }

    RouteType(String className) {
        this.className = className;
    }
}
