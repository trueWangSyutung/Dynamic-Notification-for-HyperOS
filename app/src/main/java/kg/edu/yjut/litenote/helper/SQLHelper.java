package kg.edu.yjut.litenote.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
    // 数据库版本号
    private static Integer Version = 1;

    /**
     * 构造函数
     * 在SQLiteOpenHelper的子类中，必须有该构造函数
     */
    public SQLHelper(Context context, String name) {
        // 参数说明
        // context：上下文对象
        // name：数据库名称
        // param：一个可选的游标工厂（通常是 Null）
        // version：当前数据库的版本，值必须是整数并且是递增的状态

        // 必须通过super调用父类的构造函数
        super(context, name, null, Version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据库1张表
        // 通过execSQL（）执行SQL语句（此处创建了1个名为person的表）
        String sql = "create table code(id integer primary key autoincrement," +
                "code varchar(64)," +
                "yz varchar(64), " +
                "kd  varchar(64)," +
                "insert_time datetime default (datetime('now', 'localtime'))," +
                "status integer default 0" +
                ")";
        db.execSQL(sql);


        // 注：数据库实际上是没被创建 / 打开的（因该方法还没调用）
        // 直到getWritableDatabase() / getReadableDatabase() 第一次被调用时才会进行创建 / 打开

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 参数说明：
        // db ： 数据库
        // oldVersion ： 旧版本数据库
        // newVersion ： 新版本数据库

        // 使用 SQL的ALTER语句

    }



}
