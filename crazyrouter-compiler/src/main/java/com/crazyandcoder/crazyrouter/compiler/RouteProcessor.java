package com.crazyandcoder.crazyrouter.compiler;

import com.crazyandcoder.crazyrouter.annotation.Route;
import com.crazyandcoder.crazyrouter.annotation.RouteConst;
import com.crazyandcoder.crazyrouter.annotation.RouteMeta;
import com.crazyandcoder.crazyrouter.annotation.RouteType;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;



/**
 * @ClassName: RouteProcessor
 * @Description: 注解处理器
 * @Author: crazyandcoder
 * @email: lijiwork@sina.com
 * @CreateDate: 2020/5/5 4:29 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/5/5 4:29 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

/**
 * 自动编译Processor类
 *
 * @author admin
 */
@AutoService(Processor.class)
/**
 * 指定使用的Java版本 替代 {@link AbstractProcessor#getSupportedSourceVersion()} 函数
 * @author admin
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RouteProcessor extends AbstractProcessor {

    /**
     * 文件工具
     */
    private Filer mFiler;

    /**
     * 每个module的名称
     */
    private String moduleName;
    /**
     * 元素辅助类
     */
    private Elements elementUtils;

    /**
     * 类辅助类
     */
    private Types typeUtils;

    /**
     * 获取内部关键的一些对象引用。包括文件辅助类Filer,类结构元素类Elements等。
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        Map<String, String> options = processingEnvironment.getOptions();
        moduleName = options.get("moduleName");
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
    }

    /**
     * 当发现我们自定义的注解被使用之后，process方法就会被执行，相当于普通类的main方法
     * 采集使用注册注解的信息，生成Java文件
     *
     * @param annotations 注解处理器所要处理的注解集合
     * @param roundEnv    能够查询出被指定注解 注解的元素
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!annotations.isEmpty()) {
            //获取使用了指定 注解的类,指定Route
            //一个module中，有几处使用了Route注解，set的size就是几
            Set<? extends Element> routeCommElemtents = roundEnv.getElementsAnnotatedWith(Route.class);
            generatedRouteClass(RouteConst.GENERATION_PACKAGE_NAME, routeCommElemtents);
            return true;
        }
        return false;
    }

    /**
     * @param packageName
     * @param routeElements
     */
    private void generatedRouteClass(String packageName, Set<? extends Element> routeElements) {
        if (routeElements.isEmpty()) {
            return;
        }

        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouteMeta.class)
        );

        ParameterSpec parameterSpec = ParameterSpec.builder(parameterizedTypeName, "routeComm").build();

        MethodSpec.Builder loadPath = MethodSpec.methodBuilder("loadInto")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parameterSpec);



        TypeMirror typeActivity = elementUtils.getTypeElement(RouteType.ACTIVITY.getClassName()).asType();
        TypeMirror typeFragment = elementUtils.getTypeElement(RouteType.FRAGMENT.getClassName()).asType();
        TypeMirror typeFragmentV4 = elementUtils.getTypeElement(RouteType.FRAGMENT_V4.getClassName()).asType();
        TypeMirror typeProvider = elementUtils.getTypeElement(RouteType.PROVIDER.getClassName()).asType();

        ClassName routeTypeClassName = ClassName.get(RouteType.class);
        RouteType routeType;
        for (Element element : routeElements) {
            //拿到检测到的注解，因为拿到它之后，就能取注解内的参数值
            Route routeComm = element.getAnnotation(Route.class);
            //拿到当前类
            TypeMirror tm = element.asType();

            //这里要进行判断，因为要知道当前使用这个注解的是Activity还是Fragment
            if (typeUtils.isSubtype(tm, typeActivity)) {
                //如果这次是Activity
                routeType = RouteType.ACTIVITY;
            } else if (typeUtils.isSubtype(tm, typeFragment) || typeUtils.isSubtype(tm, typeFragmentV4)) {
                //fragment有多个版本，这里要兼容,如果这次是Fragment
                routeType = RouteType.FRAGMENT;
            } else if (typeUtils.isSubtype(tm, typeProvider)) {
                routeType = RouteType.PROVIDER;
            } else {
                routeType = RouteType.UNKNOWN;
            }

            loadPath.addStatement("routeComm.put($S,RouteMeta.getInstance().destination($T.class).routeType($T." + routeType + ").path($S))",
                    //如果使用注解的地方写的是@routeComm("test"),那这个routeComm.value的值就是字符串 test
                    routeComm.path(),
                    //使用这个注解的类
                    element,
                    routeTypeClassName,
                    routeComm.path());
        }


        //现在，构建类头
        TypeElement routePath = elementUtils.getTypeElement(RouteConst.SUPER_ROUTER_INTERFACE);
        try {
            TypeSpec typeSpec = TypeSpec.classBuilder(moduleName + "_RouteComm")
                    .addSuperinterface(ClassName.get(routePath))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(loadPath.build())
                    .build();
            JavaFile.builder(packageName, typeSpec).build().writeTo(mFiler);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 解析哪些注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Route.class.getCanonicalName());
        return types;

    }

    /**
     * 支持的参数
     *
     * @return
     */
    @Override
    public Set<String> getSupportedOptions() {
        Set<String> options = new LinkedHashSet<>();
        options.add("moduleName");
        return options;

    }
}
