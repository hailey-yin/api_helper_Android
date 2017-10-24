package mobi.wonders.apps.android.budliteprovider.parser;

import java.util.List;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p>
 * BudBeanAttr
 *
 * @author yuqing
 * @date 2015/12/24
 */
public class BudBeanAttr {

    //bean对象名和表名
    private String beanName;
    //bean对象名和表字段
//    private String beanAttr;

    private List<BeanAttr> beanAttrs;

    //配置表关系
    private BudRelation relation;

    public List<BeanAttr> getBeanAttrs() {
        return beanAttrs;
    }

    public void setBeanAttrs(List<BeanAttr> beanAttrs) {
        this.beanAttrs = beanAttrs;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public BudRelation getRelation() {
        return relation;
    }

    public void setRelation(BudRelation relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "BudBeanAttr{" +
                "beanAttrs=" + beanAttrs +
                ", beanName='" + beanName + '\'' +
                ", relation=" + relation +
                '}';
    }

}
