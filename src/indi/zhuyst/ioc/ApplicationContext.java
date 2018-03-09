package indi.zhuyst.ioc;

import indi.zhuyst.ioc.pojo.Bean;
import indi.zhuyst.ioc.pojo.Property;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {

    private static final String XML_PATH = "/applicationContext.xml";

    private Map<String,Bean> beans = new HashMap<>();

    public ApplicationContext(){
        this.init();
    }

    public Object getBean(String beanName){
        return beans.get(beanName).getObject();
    }

    private void init(){
        readXml();
        setBeanProperties();
    }

    private void readXml(){
        InputStream inputStream = this.getClass().getResourceAsStream(XML_PATH);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        ApplicationContextHandler handler;

        try {
            SAXParser parser = factory.newSAXParser();
            handler = new ApplicationContextHandler();
            parser.parse(inputStream,handler);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("XML解析错误");

        }

        try {
            List<Bean> list = handler.getBeans();
            for(Bean bean : list){
                Object object = Class.forName(bean.getClassPath()).newInstance();
                bean.setObject(object);
                beans.put(bean.getId(),bean);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("没有找到类：" + e.getMessage());
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("类初始化失败");
        }
    }

    private void setBeanProperties(){
        for(Map.Entry<String,Bean> entry : beans.entrySet()){
            Bean bean = entry.getValue();

            List<Property> properties = bean.getProperties();
            if(properties != null){

                for(Property property : properties){
                    String ref = property.getRef();
                    if(beans.containsKey(ref)){
                        Object object = beans.get(ref).getObject();
                        setter(bean.getObject(),property.getName(),object);
                    }

                    else {
                        throw new RuntimeException("无法找到" + property.getRef());
                    }
                }

            }
        }
    }

    private static void setter(Object object,String name,Object value){
        String methodName = "set" + getClassName(name);
        try {
            Method setter = object.getClass().getMethod(methodName,value.getClass());
            setter.invoke(object,value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("调用Setter失败");
        }
    }

    private static String getClassName(String name){
        return name.substring(0,1).toUpperCase() + name.substring(1);
    }
}
