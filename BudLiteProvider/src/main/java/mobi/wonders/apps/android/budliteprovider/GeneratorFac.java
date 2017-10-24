package mobi.wonders.apps.android.budliteprovider;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import mobi.wonders.apps.android.budliteprovider.parser.BeanAttr;
import mobi.wonders.apps.android.budliteprovider.parser.BudBeanAttr;
import mobi.wonders.apps.android.budliteprovider.parser.BudLiteAttr;
import mobi.wonders.apps.android.budliteprovider.parser.BudRelation;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p>
 * GeneratorFac
 *
 * @author yuqing
 * @date 2015/12/23
 */
public class GeneratorFac {

    /**
     *  字段属性: String
     */
    private static final String ATTR_TYPE_STRING = "String";

    /**
     *  字段属性: int
     */
    private static final String ATTR_TYPE_INT = "int";

    /**
     *  字段属性: long
     */
    private static final String ATTR_TYPE_LONG = "long";

    /**
     *  字段属性: boolean
     */
    private static final String ATTR_TYPE_BOOLEAN = "boolean";

    /**
     *  字段属性: 主键ID
     */
    private static final String ATTR_TYPE_ID = "id";

    /**
     *  表关系:一对一
     */
    private static final String SCHEMA_RELATION_TOONE = "ToOne";

    /**
     *  表关系：一对多
     */
    private static final String SCHEMA_RELATION_TOMANY = "ToMany";

    /**
     *  表对象
     */
    private static Entity enty;

    public static void execDaoGenerator() {
        BudLiteDaoGenerator config = BudLiteDaoGenerator.getInstance();
        Schema schema = config.getSchema();
        addSchema(schema);
        config.executeGenerator(schema);
    }

    private static void addSchema(Schema schema) {
        List<BudBeanAttr> attrs = BudLiteAttr.getInstance().getList();
        List<Entity> entys = new ArrayList<>();
        for(int i = 0; attrs != null && i < attrs.size(); i++) {
            BudBeanAttr bean = attrs.get(i);
//            System.out.println("entity:"+bean.getBeanName());
            enty = schema.addEntity(bean.getBeanName());
//            enty.addStringProperty("id").primaryKey();
            enty.addIdProperty();
            List<BeanAttr> beanAttrs = bean.getBeanAttrs();
            for(int j = 0; beanAttrs != null && j < beanAttrs.size(); j++) {
                switch (beanAttrs.get(j).getAttrType()) {
                    case ATTR_TYPE_STRING:
                        enty.addStringProperty(beanAttrs.get(j).getAttrName());
                        break;
                    case ATTR_TYPE_INT:
                        enty.addIntProperty(beanAttrs.get(j).getAttrName());
                        break;
                    case ATTR_TYPE_LONG:
                        enty.addLongProperty(beanAttrs.get(j).getAttrName());
                        break;
                    case ATTR_TYPE_BOOLEAN:
                        enty.addBooleanProperty(beanAttrs.get(j).getAttrName());
                        break;
                }
//                System.out.println("add: private " + beanAttrs.get(j).getAttrType() + "  "+beanAttrs.get(j).getAttrName() + ";");
            }
            entys.add(enty);
            enty = null;
        }

        for(int i = 0; attrs != null && i < attrs.size(); i++) {
            BudBeanAttr bean = attrs.get(i);
            BudRelation relation = bean.getRelation();
            if(relation != null) {
                String className = bean.getBeanName();
                Entity targetEnty = null;
                Entity sourceEnty = null;
                for (Entity e : entys) {
                    if(e.getClassName().equalsIgnoreCase(relation.getRelationTo())){
                        targetEnty = e;
                    }
                    if (e.getClassName().equalsIgnoreCase(className)) {
                        sourceEnty = e;
                    }

                }
                if(targetEnty != null && sourceEnty != null) {
                    switch (relation.getRelationType()) {
                        case SCHEMA_RELATION_TOONE:
                            Property toOneProperty = sourceEnty.addLongProperty(relation.getRelationTo().toLowerCase() + "id").getProperty();
                            sourceEnty.addToOne(targetEnty, toOneProperty);
                            break;
                        case SCHEMA_RELATION_TOMANY:
                            Property toManyProperty = targetEnty.addLongProperty(relation.getRelationTo().toLowerCase() + "id").getProperty();
                            sourceEnty.addToMany(targetEnty, toManyProperty).setName(targetEnty.getClassName().toLowerCase() + "s");
                            break;
                    }
                }
            }
        }
    }

}
