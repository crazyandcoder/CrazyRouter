package com.crazyandcoder.crazyrouter.annotation;

/**
 * @ClassName: RouteMeta
 * @Description: 封装路由目的地信息基类
 * @Author: crazyandcoder
 * @email: lijiwork@sina.com
 * @CreateDate: 2020/5/5 4:18 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/5/5 4:18 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class RouteMeta {

    /**
     * 区分路由类型
     */
    protected RouteType routeType;


    /**
     * 之前map里面存的都是.class， 现在用一个字段来保存目的地class
     */
    protected Class destination;

    /**
     * 路由标识符，唯一
     */
    protected String path;

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public void setDestination(Class destination) {
        this.destination = destination;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public String getPath() {
        return path;
    }

    /**
     * 先做一个Builder模式
     */
    public RouteMeta() {
    }

    public RouteMeta routeType(RouteType routeType) {
        this.routeType = routeType;
        return this;
    }

    public RouteMeta destination(Class destination) {
        this.destination = destination;
        return this;
    }

    public RouteMeta path(String path) {
        this.path = path;
        return this;
    }

    public static RouteMeta getInstance() {
        return new RouteMeta();
    }
}
