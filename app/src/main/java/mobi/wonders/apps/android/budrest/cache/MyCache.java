package mobi.wonders.apps.android.budrest.cache;

/*public class MyCache {

    public static Context context;
    public static long SCEND = 1000;
    public static long MINUTE = SCEND*60;
    public static long HOUR = MINUTE*60;
    public static long DAY = HOUR*24;
    public static long outdate;
    public static Class<? extends Model> tableName;

    public MyCache(){}

    public Class<? extends Model> getTableName() {
        return tableName;
    }

    public static AlarmManager getAlarmManager(Context context){
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public static <T extends Model> void saveCacheList(Context context,Class<T> clazz, List<T> data, long outdate){

        tableName = clazz;
        new Delete().from(clazz).execute();
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0;i < data.size();i++) {
                try {
                    T model = null;
                    model = (T)Class.forName(clazz.getName()).newInstance();
                    model = data.get(i);
                    model.save();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // sTime秒后将产生广播,触发UpdateReceiver的执行,这个方法才是真正的更新数据的操作主要代码
        Intent intent = new Intent(context, UpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP , System.currentTimeMillis()+outdate, pendingIntent);
    }

    public static void saveLatestProject(Context context,int temp,List<ProjectModel>mProjects,long outdate) {
        int maxSize = 10;
        int k = 0;
        List<LatestModel> latestModels = new Select().from(LatestModel.class).orderBy("Id ASC").execute();
        LatestModel latestModel = new Select().from(LatestModel.class).where("pid = ?", mProjects.get(temp).getid()).executeSingle();
        int len = latestModels.size();
        if (len > maxSize) {
            for (int i = latestModels.size(); i > maxSize;i--) {
                new Delete().from(LatestModel.class).where("pid = ?",latestModels.get(k).getPid()).executeSingle();
                k++;
            }
        }
        if (latestModel == null) {
            if (len >= 10) {
                new Delete().from(LatestModel.class).where("pid = ?",latestModels.get(k).getPid()).executeSingle();
            }
            latestModel = new LatestModel();
            latestModel.setPid(mProjects.get(temp).getid());
            latestModel.save();
        } else {
            latestModel.delete();
            latestModel = new LatestModel();
            latestModel.setPid(mProjects.get(temp).getid());
            latestModel.save();
        }

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // sTime秒后将产生广播,触发UpdateReceiver的执行,这个方法才是真正的更新数据的操作主要代码
        Intent intent = new Intent(context, UpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + outdate, pendingIntent);
    }

    public static <T extends Model> void saveCacheObject(Context context,Class<T>clazz,T data, long outdate) {
        tableName = clazz;
        if (clazz.equals(UserModel.class)) {
            List<UserModel> users = new Select().from(UserModel.class).execute();
            for (int i = 0;i < users.size();i++) {
                if (data != users.get(i)) {
                    T model = null;
                    try {
                        model = (T)Class.forName(clazz.getName()).newInstance();
                        model = data;
                        model.save();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        } else {
            new Delete().from(clazz).execute();
            try {
                T model = (T) Class.forName(clazz.getName()).newInstance();
                model = data;
                model.save();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // sTime秒后将产生广播,触发UpdateReceiver的执行,这个方法才是真正的更新数据的操作主要代码
        Intent intent = new Intent(context, UpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + outdate, pendingIntent);

    }


    public static <T extends Model> void getCache(Class<T> clazz, CacheCallBack callBack) {
        List<T> data = new Select().from(clazz).execute();
        callBack.onData(data);
    }

}*/


