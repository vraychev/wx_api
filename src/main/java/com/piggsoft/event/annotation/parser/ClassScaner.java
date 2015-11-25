package com.piggsoft.event.annotation.parser;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author piggsoft@163.com
 * Class 扫描
 */
public class ClassScaner implements ResourceLoaderAware {

    /**
     * 默认的pattern
     */
    static final String DEFAULT_RESOURCE_PATTERN = "/**/*.class";

    /**
     * pattern解析器，借助spring的解析
     */
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * 过滤条件，符合即为需要
     */
    private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();

    /**
     * 过滤条件，符合即为要丢弃的
     */
    private final List<TypeFilter> excludeFilters = new LinkedList<TypeFilter>();

    /**
     * 元信息读取
     */
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
            this.resourcePatternResolver);  
  
    public ClassScaner() {  
  
    }

    /**
     * 设定 ResourceLoader
     * @param resourceLoader {@link ResourceLoader}
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils
                .getResourcePatternResolver(resourceLoader);  
        this.metadataReaderFactory = new CachingMetadataReaderFactory(  
                resourceLoader);  
    }  


    public final ResourceLoader getResourceLoader() {  
        return this.resourcePatternResolver;  
    }

    /**
     * 添加包含的过滤器
     * @param includeFilter 包含的过滤器
     */
    public void addIncludeFilter(TypeFilter includeFilter) {  
        this.includeFilters.add(includeFilter);  
    }

    /**
     * 添加排除的过滤器
     * @param excludeFilter 排除的过滤器
     */
    public void addExcludeFilter(TypeFilter excludeFilter) {  
        this.excludeFilters.add(0, excludeFilter);  
    }

    /**
     * 重置所有过滤器
     * @param useDefaultFilters 是否用默认的过滤器，默认的过滤器暂未实现
     */
    public void resetFilters(boolean useDefaultFilters) {  
        this.includeFilters.clear();  
        this.excludeFilters.clear();  
    }

    /**
     * 扫描指定的package，找出所有指定注解的class
     * @param basePackage 指定的package
     * @param annotations 指定的注解
     * @return 寻找到的结果
     */
    public static Set<Class> scan(String basePackage,
            Class<? extends Annotation>... annotations) {
        ClassScaner cs = new ClassScaner();  
        for (Class anno : annotations) {
            cs.addIncludeFilter(new AnnotationTypeFilter(anno));
        }
        return cs.doScan(basePackage);  
    }
    /**
     * 扫描指定的package，找出所有指定注解的class
     * @param basePackages 指定的package数组
     * @param annotations 指定的注解
     * @return 寻找到的结果
     */
    public static Set<Class> scan(String[] basePackages,  
            Class<? extends Annotation>... annotations) {  
        ClassScaner cs = new ClassScaner();  
        for (Class anno : annotations) {
            cs.addIncludeFilter(new AnnotationTypeFilter(anno));
        }
        Set<Class> classes = new HashSet<Class>();
        for (String s : basePackages) {
            classes.addAll(cs.doScan(s));
        }
        return classes;  
    }

    /**
     * 扫描指定的package，找出所有指定注解的class
     * <br/>内部调用{@link this#matches(MetadataReader)} 来判断
     * @param basePackage 指定的package
     * @return 寻找的结果
     */
    public Set<Class> doScan(String basePackage) {  
        Set<Class> classes = new HashSet<Class>();  
        try {  
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX  
                    + ClassUtils
                            .convertClassNameToResourcePath(SystemPropertyUtils
                                    .resolvePlaceholders(basePackage))  
                    + DEFAULT_RESOURCE_PATTERN;
            Resource[] resources = this.resourcePatternResolver
                    .getResources(packageSearchPath);  
  
            for (int i = 0; i < resources.length; i++) {  
                Resource resource = resources[i];  
                if (resource.isReadable()) {  
                    MetadataReader metadataReader = this.metadataReaderFactory
                            .getMetadataReader(resource);  
                    if ((includeFilters.size() == 0 && excludeFilters.size() == 0)  
                            || matches(metadataReader)) {  
                        try {  
                            classes.add(Class.forName(metadataReader  
                                    .getClassMetadata().getClassName()));  
                        } catch (ClassNotFoundException e) {  
                            e.printStackTrace();  
                        }  
  
                    }  
                }  
            }  
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException(
                    "I/O failure during classpath scanning", ex);  
        }  
        return classes;  
    }

    /**
     * 调用spring的方法来判断是否含有该注解，和排除的注解
     * @param metadataReader 元信息Reader
     * @return 符合条件true，not false
     * @throws IOException 读取时抛出
     */
    protected boolean matches(MetadataReader metadataReader) throws IOException {  
        for (TypeFilter tf : this.excludeFilters) {  
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return false;  
            }  
        }  
        for (TypeFilter tf : this.includeFilters) {  
            if (tf.match(metadataReader, this.metadataReaderFactory)) {  
                return true;  
            }  
        }  
        return false;  
    }  
}
