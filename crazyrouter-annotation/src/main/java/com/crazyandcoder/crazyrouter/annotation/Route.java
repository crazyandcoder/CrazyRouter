package com.crazyandcoder.crazyrouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: Route
 * @Description: java类作用描述
 * @Author: crazyandcoder
 * @email: lijiwork@sina.com
 * @CreateDate: 2020/5/5 4:24 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/5/5 4:24 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Route {

    /**
     * 路由的路径，标识一个路由节点
     *
     * @return
     */
    String path();


    /**
     * 将路由节点进行分组，可以实现按组动态加载
     *
     * @return
     */
    String group() default "";

}
