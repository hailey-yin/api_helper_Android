package mobi.wonders.apps.android.budliteprovider.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p>
 * BudLiteContentHandler
 *
 * @author yuqing
 * @date 2015/12/23
 */
public class BudLiteContentHandler extends DefaultHandler {

    /**
     * xml持久化对象
     */
    private BudLiteAttr budLiteAttr;

    /**
     * xml表对象集合
     */
    private List mBeanList = new ArrayList();

    /**
     *  xml表字段临时列表集合
     */
    private List mAttrList;

    /**
     * xml临时表对象
     */
    private BudBeanAttr mBeanAttr;

    /**
     *  xml表字段临时对象
     */
    private BeanAttr mAttrs;

    /**
     *  关系表
     */
    private BudRelation mRelation;

    @Override
    public void startDocument() throws SAXException {
        budLiteAttr = budLiteAttr.getInstance();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName){
            case BudLiteParser.BEAN_NAME: // beanName
                mBeanAttr = new BudBeanAttr();
                mAttrList = new ArrayList();
                for (int i = 0; attributes != null && i < attributes.getLength(); i++) { // value
                    if (BudLiteParser.ATTR_VALUE.equalsIgnoreCase(attributes.getQName(i))) {
                        mBeanAttr.setBeanName(attributes.getValue(i).trim());
                    }
                }
                break;
            case BudLiteParser.ATTR_BEAN: // beanAttr
                mAttrs = new BeanAttr();
                for (int i = 0; attributes != null && i < attributes.getLength(); i++) { // attrName
                    if (BudLiteParser.ATTR_NAME.equalsIgnoreCase(attributes.getQName(i))) {
                        mAttrs.setAttrName(attributes.getValue(i).trim());
                    }else if(BudLiteParser.ATTR_TYPE.equalsIgnoreCase(attributes.getQName(i))) {//attrType
                        mAttrs.setAttrType(attributes.getValue(i).trim());
                    }
                }
                break;
            case BudLiteParser.NODE_DB_NAME:
                for (int i = 0; attributes != null && i < attributes.getLength(); i++) {
                    if (BudLiteParser.ATTR_VALUE.equalsIgnoreCase(attributes.getQName(i))) {
                        budLiteAttr.setDbName(attributes.getValue(i).trim());
                    }
                }
                break;
            case BudLiteParser.NODE_VERSION:
                for (int i = 0; attributes != null && i < attributes.getLength(); i++) {
                    if (BudLiteParser.ATTR_VALUE.equalsIgnoreCase(attributes.getQName(i))) {
                        budLiteAttr.setVersion(Integer.parseInt(attributes.getValue(i).trim()));
                    }
                }
                break;
            case BudLiteParser.NODE_BEANPK:
                for (int i = 0; attributes != null && i < attributes.getLength(); i++) {
                    if (BudLiteParser.ATTR_VALUE.equalsIgnoreCase(attributes.getQName(i))) {
                        budLiteAttr.setDbBeanPkName(attributes.getValue(i).trim());
                    }
                }
                break;
            case BudLiteParser.NODE_DAOPK:
                for (int i = 0; attributes != null && i < attributes.getLength(); i++) {
                    if (BudLiteParser.ATTR_VALUE.equalsIgnoreCase(attributes.getQName(i))) {
                        budLiteAttr.setDbDaoPKName(attributes.getValue(i).trim());
                    }
                }
                break;
            case BudLiteParser.RELATION:
                mRelation = new BudRelation();
                for (int i = 0; attributes != null && i < attributes.getLength(); i++) { // relationType
                    if (BudLiteParser.RELATIONTYPE.equalsIgnoreCase(attributes.getQName(i))) {
                        mRelation.setRelationType(attributes.getValue(i).trim());
                    }else if(BudLiteParser.RELATIONTO.equalsIgnoreCase(attributes.getQName(i))) {//relationTo
                        mRelation.setRelationTo(attributes.getValue(i).trim());
                    }
                }
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equalsIgnoreCase(BudLiteParser.ATTR_BEAN)) {
            mAttrList.add(mAttrs);
            mAttrs = null;
        }else if (qName.equalsIgnoreCase(BudLiteParser.BEAN_NAME)) {
            mBeanAttr.setBeanAttrs(mAttrList);
            mAttrList = null;
            mBeanList.add(mBeanAttr);
            mBeanAttr = null;
        }else if(qName.equalsIgnoreCase(BudLiteParser.RELATION)) {
            mBeanAttr.setRelation(mRelation);
            mRelation = null;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        budLiteAttr.setList(mBeanList);
    }
}
