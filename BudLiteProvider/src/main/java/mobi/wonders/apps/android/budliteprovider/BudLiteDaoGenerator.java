package mobi.wonders.apps.android.budliteprovider;

import java.io.File;
import java.util.List;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Schema;
import mobi.wonders.apps.android.budliteprovider.parser.BeanAttr;
import mobi.wonders.apps.android.budliteprovider.parser.BudBeanAttr;
import mobi.wonders.apps.android.budliteprovider.parser.BudLiteAttr;
import mobi.wonders.apps.android.budliteprovider.parser.BudLiteParser;

public class BudLiteDaoGenerator {

    public static String BEANPK;
    public static String DAOPK;
    public static int DBVERSION;
    public static String DBNAME;

    public static String JAVAGENPK = "BudLiteProvider/src/main/java-gen";
//    public static String ANDROIDGENPK = "app/src/main/java-gen";

    private static BudLiteDaoGenerator mInstance;

    public BudLiteDaoGenerator() {
    }

    public static BudLiteDaoGenerator getInstance() {
        if(mInstance == null) {
            mInstance = new BudLiteDaoGenerator();
        }
        return mInstance;
    }

    public Schema getSchema() {
        // 读取配置
        parserBudlite();

        Schema schema = new Schema(DBVERSION, BEANPK);
        schema.setDefaultJavaPackageDao(DAOPK);

//        schema.enableActiveEntitiesByDefault();
        schema.enableKeepSectionsByDefault();

//        ConfigSchema.getInstance().initialise(schema);

        return schema;

    }

    public void executeGenerator(Schema schema) {
        try {
            createDir(JAVAGENPK);
            new DaoGenerator().generateAll(schema, JAVAGENPK);
//            delJavaFile(new File(JAVAGENPK));

//            createDir(ANDROIDGENPK);
//            new DaoGenerator().generateAll(schema, ANDROIDGENPK);
//            delAndroidFile(new File(ANDROIDGENPK));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parserBudlite() {
        BudLiteParser.parseBudLiteConfiguration();
        BudLiteAttr mAttr = BudLiteAttr.getInstance();
        DBNAME = mAttr.getDbName();
        DBVERSION = mAttr.getVersion();
        BEANPK = mAttr.getDbBeanPkName();
        DAOPK = mAttr.getDbDaoPKName();

        System.out.println("数据库名称：" + DBNAME
                + "\n数据库版本：" + DBVERSION
                + "\nbean目录：" + BEANPK
                + "\ndao目录：" + DAOPK);

        List<BudBeanAttr> mAttrs = mAttr.getList();
        int count = 0;
        System.out.println("数据表信息：\n");
        for (BudBeanAttr b : mAttrs) {
            count ++ ;
            System.out.println("=====================================\n"
                    + "表" + count + "名称：" + b.getBeanName());
            for (BeanAttr attr : b.getBeanAttrs()) {
                System.out.println("[字段：" + attr.getAttrName() + ",类型：" + attr.getAttrType() + "]");
            }
            if(b.getRelation() != null && b.getRelation().getRelationType().equalsIgnoreCase("ToOne")) {
                System.out.println("多表关系:" + b.getBeanName() + "表和" + b.getRelation().getRelationTo() + "表是一对一关联");
            }else if(b.getRelation() != null && b.getRelation().getRelationType().equalsIgnoreCase("ToMany")) {
                System.out.println("多表关系:" + b.getBeanName() + "表和" + b.getRelation().getRelationTo() + "表是一对多关联");
            }
        }
    }

    private void createDir(String path) {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
            System.out.println("新建目录：" + path);
        } else {
            System.out.println("目录已存在" + path);
        }
        delJavaFile(new File(JAVAGENPK));
    }

    private void delAndroidFile(File file) {
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f:files){
                delAndroidFile(f);
            }
        }else if( file.getName().equalsIgnoreCase("DaoMaster.java") || file.getName().equalsIgnoreCase("DaoSession.java")){
            file.delete();
        }
    }

    private void delJavaFile(File file) {
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f:files){
                delJavaFile(f);
            }
        }else if( !file.getName().equalsIgnoreCase("DaoMaster.java") && !file.getName().equalsIgnoreCase("DaoSession.java")){
            file.delete();
        }
    }

}
