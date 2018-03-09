package indi.zhuyst.ioc.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * XML元素，在<beans></beans>下使用
 * Bean的内容
 * 设置的属性{@link Property}
 */
public class Bean {

    /**
     * Bean的唯一标识
     */
    private String id;

    /**
     * Bean的Class所在位置
     */
    private String classPath;

    /**
     * Bean对象
     */
    private Object object;

    /**
     * Bean中需要设置的属性列表
     */
    private List<Property> properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void addProperty(Property property){
        if(properties == null){
            properties = new ArrayList<>();
        }
        properties.add(property);
    }
}
