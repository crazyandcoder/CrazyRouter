package com.crazyandcoder.crazyrouter.annotation;

import java.util.Map;

/**
 * @ClassName: IRouteGroup
 * @Description: 路由的作用就是：注册Activity 或者 Fragment 或者 Provider
 * @Author: crazyandcoder
 * @email: lijiwork@sina.com
 * @CreateDate: 2020/5/5 4:28 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/5/5 4:28 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface IRouteGroup {

    /**
     * 使用javapoet工具，根据分组名生成一个继承IRouteGroup接口的Java类，叫做分组信息类，用来保存每个分组的路由信息
     *
     * @param routes
     */
    void loadInto(Map<String, RouteMeta> routes);
}
