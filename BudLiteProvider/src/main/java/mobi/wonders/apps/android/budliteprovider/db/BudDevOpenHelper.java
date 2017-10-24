package mobi.wonders.apps.android.budliteprovider.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import dao.DaoMaster;


/**
 *  封装DaoMaster.OpenHelper来实现数据库升级
 */
public class BudDevOpenHelper extends DaoMaster.OpenHelper{

    public BudDevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                //创建新表，注意createTable()是静态方法

//                 GradeDao.createTable(db, true);
                // 加入新字段
//                 db.execSQL("ALTER TABLE 'NOTE' ADD 'result' REAL");
                // TODO 处理新数据结构

                //USER表添加DATE字段
                db.execSQL("ALTER TABLE 'USER_DB' ADD 'DATE' REAL");
                break;
        }
    }
}
