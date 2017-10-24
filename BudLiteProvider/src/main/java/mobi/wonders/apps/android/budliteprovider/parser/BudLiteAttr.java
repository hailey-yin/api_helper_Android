package mobi.wonders.apps.android.budliteprovider.parser;

import java.util.List;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p/>
 * BudLiteAttr
 *
 * @author yuqing
 * @date 2015/12/23
 */
public class BudLiteAttr {

    private static BudLiteAttr budliteAttr;

    //数据库版本号
    private int version;
    //数据库名
    private String dbName;
    //Bean对象目录
    private String dbBeanPkName;
    //Dao目录
    private String dbDaoPKName;
    //配置表信息
    private List<BudBeanAttr> list;

    public static BudLiteAttr getInstance(){
        if (budliteAttr == null){
            synchronized (BudLiteAttr.class){
                if (budliteAttr == null){
                    budliteAttr = new BudLiteAttr();
                }
            }
        }
        return budliteAttr;
    }

    public String getDbBeanPkName() {
        return dbBeanPkName;
    }

    public void setDbBeanPkName(String dbBeanPkName) {
        this.dbBeanPkName = dbBeanPkName;
    }

    public String getDbDaoPKName() {
        return dbDaoPKName;
    }

    public void setDbDaoPKName(String dbDaoPKName) {
        this.dbDaoPKName = dbDaoPKName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<BudBeanAttr> getList() {
        return list;
    }

    public void setList(List<BudBeanAttr> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "BudLiteAttr{" +
                "dbBeanPkName='" + dbBeanPkName + '\'' +
                ", version=" + version +
                ", dbName='" + dbName + '\'' +
                ", dbDaoPKName='" + dbDaoPKName + '\'' +
                ", list=" + list +
                '}';
    }
}
