package mobi.wonders.apps.android.budliteprovider.parser;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p>
 * BeanAttr
 *
 * @author yuqing
 * @date 2015/12/25
 */
public class BeanAttr {

    //字段名
    private String attrName;
    //字段类型
    private String attrType;

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    @Override
    public String toString() {
        return "BeanAttr{" +
                "attrName='" + attrName + '\'' +
                ", attrType='" + attrType + '\'' +
                '}';
    }
}
