package indi.zhuyst.ioc.pojo;

import java.util.ArrayList;
import java.util.List;

public class Bean {

    private String id;

    private String classPath;

    private Object object;

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
