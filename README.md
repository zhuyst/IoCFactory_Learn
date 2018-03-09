# IoCFactory_Learn

花了点时间摸了一个`IoC容器`出来，没实现组件扫描和变量的注解注入，就单纯实现了`使用xml进行的配置`。

## applicationContext.xml

配置了一下，`将UserDao注入到UserService当中`。

```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans>
    <bean id="userDao" class="indi.zhuyst.dao.UserDao"/>
    <bean id="userService" class="indi.zhuyst.service.UserService">
        <property name="userDao" ref="userDao"/>
    </bean>
</beans>
```

## ApplicationContext

首先定义好XML的位置以及一个`存放Bean的Map`

```java
    private static final String XML_PATH = "/applicationContext.xml";

    private Map<String,Bean> beans = new HashMap<>();

    public ApplicationContext(){
        this.init();
    }
```

之后顺序就很明确了，`读取XML -> 初始化所有Bean -> 设置Bean中的属性`

```java
/**
     * 读取XML到{@link #beans}中
     * @see #XML_PATH
     */
    private void readXml(){
        InputStream inputStream = this.getClass().getResourceAsStream(XML_PATH);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            ApplicationContextHandler handler = new ApplicationContextHandler();

            parser.parse(inputStream,handler);
            initBean(handler.getBeans());

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
```

一个简单的Ioc容器就完成了！

`完整源码`可以查看[IoCFactory_Learn](https://github.com/zhuyst/IoCFactory_Learn)
