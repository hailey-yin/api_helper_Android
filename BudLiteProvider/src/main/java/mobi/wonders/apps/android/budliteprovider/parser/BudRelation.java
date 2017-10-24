package mobi.wonders.apps.android.budliteprovider.parser;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p/>
 * BudRelation
 *
 * @author yuqing
 * @date 2015/12/31
 */
public class BudRelation {
    //表关系类型
    private String relationType;
    //要建立关系的表
    private String relationTo;

    public String getRelationTo() {
        return relationTo;
    }

    public void setRelationTo(String relationTo) {
        this.relationTo = relationTo;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    @Override
    public String toString() {
        return "BudRelation{" +
                "relationTo='" + relationTo + '\'' +
                ", relationType='" + relationType + '\'' +
                '}';
    }
}
