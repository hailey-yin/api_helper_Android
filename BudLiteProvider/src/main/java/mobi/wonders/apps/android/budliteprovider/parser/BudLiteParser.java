package mobi.wonders.apps.android.budliteprovider.parser;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import mobi.wonders.apps.android.budliteprovider.exceptions.ParseConfigurationFileException;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p>
 * BudLiteParser
 *
 * @author yuqing
 * @date 2015/12/23
 */
public class BudLiteParser {
    /**
     * dbname 节点.
     */
    static final String NODE_DB_NAME = "dbname";

    /**
     * version 节点.
     */
    static final String NODE_VERSION = "version";

    /**
     * Bean目录 节点.
     */
    static final String NODE_BEANPK = "dbBeanPkName";

    /**
     * DAO目录 节点.
     */
    static final String NODE_DAOPK = "dbDaoPKName";

    /**
     * 属性值.
     */
    static final String ATTR_VALUE = "value";

    /**
     *  bean对象名字 节点
     */
    static final String BEAN_NAME = "beanName";

    /**
     *  Bean对象字段名字节点
     */
    static final String ATTR_BEAN = "beanAttr";

    /**
     *  Bean对象字段名字属性
     */
    static final String ATTR_NAME = "attrName";

    /**
     *  Bean对象字段类型属性
     */
    static final String ATTR_TYPE = "attrType";

    /**
     *  表关系节点
     */
    static final String RELATION = "relation";

    /**
     *  表关系类型
     */
    static final String RELATIONTYPE = "relationType";

    /**
     *  要建立关系的表
     */
    static final String RELATIONTO = "relationTo";

    /**
     * 存储解析过litepal.xml的值
     */
    private static BudLiteParser parser;

    /**
     *  配置文件名
     */
    private static final String CONFIGURATION_FILE_NAME = "budlite.xml";

    private static final String ASSETS_PATH = "app/src/main/assets/budlite.xml";

    public static void parseBudLiteConfiguration() {
        if (parser == null) {
            parser = new BudLiteParser();
        }
        parser.useSAXParser();
    }

    public static void parseBudLiteConfiguration(Context context) {
        if (parser == null) {
            parser = new BudLiteParser();
        }
        parser.useSAXParser(context);
    }

    /**
     *  SAX解析xml for java
     */
    void useSAXParser() {
        BudLiteContentHandler handler = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            handler = new BudLiteContentHandler();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(getConfigInputStreamForJava()));
            return;
        } catch (Resources.NotFoundException e) {
            throw new ParseConfigurationFileException(
                    ParseConfigurationFileException.CAN_NOT_FIND_BUDLITE_FILE);
        } catch (SAXException e) {
            throw new ParseConfigurationFileException(
                    ParseConfigurationFileException.FILE_FORMAT_IS_NOT_CORRECT);
        } catch (ParserConfigurationException e) {
            throw new ParseConfigurationFileException(
                    ParseConfigurationFileException.PARSE_CONFIG_FAILED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  SAX解析xml for android
     */
    void useSAXParser(Context context) {
        BudLiteContentHandler handler = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            handler = new BudLiteContentHandler();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(getConfigInputStreamForAndroid(context)));
            return;
        } catch (Resources.NotFoundException e) {
            throw new ParseConfigurationFileException(
                    ParseConfigurationFileException.CAN_NOT_FIND_BUDLITE_FILE);
        } catch (SAXException e) {
            throw new ParseConfigurationFileException(
                    ParseConfigurationFileException.FILE_FORMAT_IS_NOT_CORRECT);
        } catch (ParserConfigurationException e) {
            throw new ParseConfigurationFileException(
                    ParseConfigurationFileException.PARSE_CONFIG_FAILED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  pull解析xml
     */
    void usePullParse(Context context) {
        try {
            BudLiteAttr budLiteAttr = BudLiteAttr.getInstance();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(getConfigInputStreamForAndroid(context), "UTF-8");
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        if (NODE_DB_NAME.equals(nodeName)) {
                            String dbName = xmlPullParser.getAttributeValue("", ATTR_VALUE);
                            budLiteAttr.setDbName(dbName);
                        } else if (NODE_VERSION.equals(nodeName)) {
                            String version = xmlPullParser.getAttributeValue("", ATTR_VALUE);
                            budLiteAttr.setVersion(Integer.parseInt(version));
                        } else if (NODE_BEANPK.equals(nodeName)) {
                            String className = xmlPullParser.getAttributeValue("", ATTR_VALUE);
                            budLiteAttr.setDbBeanPkName(className);
                        } else if (NODE_DAOPK.equals(nodeName)) {
                            String cases = xmlPullParser.getAttributeValue("", ATTR_VALUE);
                            budLiteAttr.setDbDaoPKName(cases);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            throw new ParseConfigurationFileException(
                    ParseConfigurationFileException.FILE_FORMAT_IS_NOT_CORRECT);
        } catch (IOException e) {
            throw new ParseConfigurationFileException(ParseConfigurationFileException.IO_EXCEPTION);
        }
    }

    private InputStream getConfigInputStreamForAndroid(Context context) throws IOException {
//        AssetManager assetManager = BudLiteApplication.getContext().getAssets();
        AssetManager assetManager = context.getAssets();
        String[] fileNames = assetManager.list("");
        if (fileNames != null && fileNames.length > 0) {
            for (String fileName : fileNames) {
                if (CONFIGURATION_FILE_NAME.equalsIgnoreCase(fileName)) {
                    return assetManager.open(fileName, AssetManager.ACCESS_BUFFER);
                }
            }
        }
        throw new ParseConfigurationFileException(
                ParseConfigurationFileException.CAN_NOT_FIND_BUDLITE_FILE);
    }

    private InputStream getConfigInputStreamForJava() throws FileNotFoundException {
        File file = new File(ASSETS_PATH);
        InputStream inputStream = new FileInputStream(file);
        return inputStream;
    }

}
