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

    /**
     * 从工厂{@link #beans}中获取Bean
     * @param beanName Bean名称{@link Bean#id}
     * @return Bean
     */
    public Object getBean(String beanName){
        Bean bean = beans.get(beanName);
        if(bean == null){
            throw new RuntimeException("没有找到" + beanName);
        }
        return bean.getObject();
    }

    /**
     * 初始化工厂
     */
    private void init(){
        List<Bean> list = readXml();
        initBean(list);
        setBeanProperties();
    }

    /**
     * 读取XML到{@link #beans}中
     * @see #XML_PATH
     */
    private List<Bean> readXml(){
        InputStream inputStream = this.getClass().getResourceAsStream(XML_PATH);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            ApplicationContextHandler handler = new ApplicationContextHandler();

            parser.parse(inputStream,handler);
            return handler.getBeans();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("XML解析错误");

        }
    }

    /**
     * 通过反射，初始化所有的Bean
     * @param beanList Bean列表
     */
    private void initBean(List<Bean> beanList){
        try {
            for(Bean bean : beanList){
                Object object = Class.forName(bean.getClassPath()).newInstance();
                bean.setObject(object);
                beans.put(bean.getId(),bean);

                System.out.println("初始化 —— " + bean.getClassPath());
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("没有找到类：" + e.getMessage());
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("类初始化失败");
        }
    }

    /**
     * 核心：通过setter设置Bean中需要的类
     */
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

                        System.out.println("向 " + bean.getClassPath() + " 设置 " + property.getName());
                    }

                    else {
                        throw new RuntimeException("无法找到" + property.getRef());
                    }
                }

            }
        }
    }

    /**
     * 通过反射设置属性
     * @param object 要设置的类
     * @param name 要设置的属性名
     * @param value 要设置的属性值
     */
    private static void setter(Object object,String name,Object value){
        final String setterPrefix = "set";
        String methodName = setterPrefix + getClassName(name);

        try {
            Method setter = object.getClass().getMethod(methodName,value.getClass());
            setter.invoke(object,value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("调用Setter失败");
        }
    }

    /**
     * 将属性名转换为类名（首字母大写）
     * @param name 属性名
     * @return 类名
     */
    private static String getClassName(String name){
        return name.substring(0,1).toUpperCase() + name.substring(1);
    }
}
