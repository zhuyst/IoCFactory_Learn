package indi.zhuyst.ioc;

import indi.zhuyst.ioc.pojo.Bean;
import indi.zhuyst.ioc.pojo.Property;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class ApplicationContextHandler extends DefaultHandler{

    private static final String ELEMENT_BEAN = "bean";

    private static final String ELEMENT_PROPERTY = "property";

    private static final String ATTRIBUTE_ID = "id";

    private static final String ATTRIBUTE_CLASS = "class";

    private static final String ATTRIBUTE_NAME = "name";

    private static final String ATTRIBUTE_REF = "ref";

    private List<Bean> beans = new ArrayList<>();

    /**
     * 由于SAX是用流的方式读取，所以需要设置为成员变量
     */
    private Bean bean;

    /**
     * 从XML中读取Bean列表
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (ELEMENT_BEAN.equals(qName)) {
            bean = new Bean();

            String id = attributes.getValue(ATTRIBUTE_ID);
            bean.setId(id);

            String classPath = attributes.getValue(ATTRIBUTE_CLASS);
            bean.setClassPath(classPath);

            beans.add(bean);
        }
        else if(ELEMENT_PROPERTY.equals(qName)){
            Property property = new Property();

            String name = attributes.getValue(ATTRIBUTE_NAME);
            property.setName(name);

            String ref = attributes.getValue(ATTRIBUTE_REF);
            property.setRef(ref);

            bean.addProperty(property);
        }
    }

    public List<Bean> getBeans() {
        return beans;
    }
}
