package com.tsingsoft.executor.config;

import com.tsingsoft.executor.utils.ClassLoaderUtil;
import com.tsingsoft.executor.utils.PackageScanner;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.List;

/**
 * 启动时注册bean核心类
 * 此类用于动态加载jar中的类进行自动注册进系统。
 * @author bask
 * @version 1.0
 * @date 2022/7/5
 * <p>
 */
@Slf4j
public class PluginImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    /**
     * 存储Jar文件基础路径
     */
    private String basePath;

    /**
     * 包名称集，多个名称则通过","逗号进行区分。
     */
    private String jarNames;
    /**
     * 包前缀，如：com.tsingsoft
     */
    private String packagePrefix;

    @SneakyThrows
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        try {
            if(jarNames==null){
                log.warn("加载包名称为空，如果需要加载则需要配置，请知晓！");
                return;
            }

            String[] jarNameses = jarNames.split(",");
            String[] packagePrefixes = packagePrefix.split(",");
            for (int i = 0; i <jarNameses.length ; i++) {
                String jarName = jarNameses[i];
                String path = "file:/"+basePath+ "/"+jarName;
                ClassLoader classLoader = ClassLoaderUtil.getClassLoader(path);
                URL url = new URL("jar:"+path+"!/");
                PackageScanner scanner = new PackageScanner();

                for (int j = 0; j < packagePrefixes.length; j++) {
                    packagePrefix = packagePrefixes[j];
                    List<String> pluginClasses = scanner.getClassesNamesByJar(packagePrefix,url);
                    pluginClasses.stream().filter(x->x.startsWith(packagePrefix)).forEach(cls->{
                        log.info("pluginClass:{}",cls);

                        if(cls.startsWith(packagePrefix) && cls!=null){
                            Class<?> clazz = null;
                            try {
                                clazz = classLoader.loadClass(cls);
                                //注册
                                registerBean(clazz, registry);
                                log.info("register bean [{}],Class [{}] success.", clazz.getName(), clazz);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            log.warn("存在空值》》》》》》》》》");
                        }
                    });
                }


            }

            log.info("加载对应jar包成功！");
        }catch (Exception e){
            e.printStackTrace();
            log.warn("指定插件目录没有加载对应合法jar包");
        }

    }

    /**
     * 註冊BEAN
     * @param c
     * @param registry
     */
    private void registerBean(Class<?> c, BeanDefinitionRegistry registry) {
        String className = c.getName();
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(c);
        BeanDefinition beanDefinition = builder.getBeanDefinition();

        if (isSpringBeanClass(c)) {
            registry.registerBeanDefinition(className, beanDefinition);
        }


    }

    /**
     * 方法描述 判断class对象是否带有spring的注解
     * 存放實現
     *
     * @param cla jar中的每一个class
     * @return true 是spring bean   false 不是spring bean
     * @method isSpringBeanClass
     */
    public boolean isSpringBeanClass(Class<?> cla) {

        if (cla == null) {
            return false;
        }
        //是否是接口
        if (cla.isInterface()) {
            return false;
        }
        //是否是抽象类
        if (Modifier.isAbstract(cla.getModifiers())) {
            return false;
        }
        try {
            if (cla.getAnnotation(Component.class) != null) {
                return true;
            }
        }catch (Exception e){
            log.error("出现异常：{}",e.getMessage());
        }

        try {
            if (cla.getAnnotation(Repository.class) != null) {
                return true;
            }
        }catch (Exception e){
            log.error("出现异常：{}",e.getMessage());
        }

        try {
            if (cla.getAnnotation(Service.class) != null) {
                return true;
            }
        }catch (Exception e){
            log.error("出现异常：{}",e.getMessage());
        }
        return false;
    }

    /**
     * 因加载顺序原因，则获取配置不用通过@Value来获取。
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.basePath = environment.getProperty("basePath");
        this.packagePrefix = environment.getProperty("packagePrefix");
        this.jarNames = environment.getProperty("jarNames");
    }

}
