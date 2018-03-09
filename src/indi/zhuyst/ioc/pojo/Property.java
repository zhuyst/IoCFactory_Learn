package indi.zhuyst.ioc.pojo;

/**
 * XML元素，在<bean></bean>下使用
 * Bean需要设置的属性
 */
public class Property {

    /**
     * 需要设置的属性名
     */
    private String name;

    /**
     * 需要设置的BeanId
     * @see Bean#id
     */
    private String ref;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
