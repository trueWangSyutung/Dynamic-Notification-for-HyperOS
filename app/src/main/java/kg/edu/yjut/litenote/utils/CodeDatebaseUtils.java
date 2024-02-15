package kg.edu.yjut.litenote.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import kg.edu.yjut.litenote.bean.Code;
import kg.edu.yjut.litenote.bean.LogBeam;
import kg.edu.yjut.litenote.helper.SQLHelper;

public class CodeDatebaseUtils {
    //  ContentValues values = new ContentValues();
    //        values.put("name", "zhangsan");
    //
    //        // b. 调用update方法修改数据库：将id=1 修改成 name = zhangsan
    //        sqliteDatabase.update("user", values, "id=?", new String[] { "1" });
    //

    public static SQLiteDatabase openOrCreateDatabase(Context context) {
        SQLiteOpenHelper dbHelper = new SQLHelper(context, "code.db");
        return dbHelper.getWritableDatabase();
    }

    public static void insertData(SQLiteDatabase db, ContentValues values) {
        // 查询该 code 是否已经存在，且 status = 0
        Cursor cursor = db.query("code", null, "code=? and status=?", new String[]{values.getAsString("code"), "0"}, null, null, null);
        if (cursor.getCount() > 0) {
            // 如果存在，则更新时间
            db.update("code", values, "code=? and status=?", new String[]{values.getAsString("code"), "0"});
            return;
        }
        Log.d("CodeDatebaseUtils", "insertData: " + values);

        db.insert("code", null, values);
    }

    public static void updateStatus(SQLiteDatabase db, int id, int status) {
        ContentValues values = new ContentValues();
        values.put("status", status);
        db.update("code", values, "id=?", new String[] {String.valueOf(id)});

    }
    public static void updateData(SQLiteDatabase db, ContentValues values, int id) {
        db.update("code", values, "id=?", new String[] {String.valueOf(id)});
    }

    public static void deleteData(SQLiteDatabase db, int id) {
        db.delete("code", "id=?", new String[] {String.valueOf(id)});
    }

    @SuppressLint("Range")
    public static ArrayList<Code> getNearDate(SQLiteDatabase db) {
        // 查询最近的6条数据，按时间倒序, 且 status = 0
        Cursor cursor = db.query("code", null, "status=?", new String[]{"0"}, null, null, "insert_time desc", "6");
        // 将数据转为 ArrayList
        ArrayList<Code> codes = new ArrayList<>();
        while (cursor.moveToNext()) {
            Code code = new Code(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("code")),
                    cursor.getString(cursor.getColumnIndex("yz")),
                    cursor.getString(cursor.getColumnIndex("kd")),
                    cursor.getString(cursor.getColumnIndex("insert_time")),
                    cursor.getInt(cursor.getColumnIndex("status"))
            );
            codes.add(code);
        }
        return codes;
    }
    @SuppressLint("Range")
    public static ArrayList<Code> getNearDateNum(SQLiteDatabase db,int num) {
        // 查询最近的6条数据，按时间倒序, 且 status = 0
        Cursor cursor = db.query("code", null, "status=?", new String[]{"0"}, null, null, "insert_time desc", String.valueOf(num));
        // 将数据转为 ArrayList
        ArrayList<Code> codes = new ArrayList<>();
        while (cursor.moveToNext()) {
            Code code = new Code(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("code")),
                    cursor.getString(cursor.getColumnIndex("yz")),
                    cursor.getString(cursor.getColumnIndex("kd")),
                    cursor.getString(cursor.getColumnIndex("insert_time")),
                    cursor.getInt(cursor.getColumnIndex("status"))
            );

            codes.add(code);
        }
        return codes;
    }
    @SuppressLint("Range")
    public static ArrayList<Code> getAllCodes(SQLiteDatabase db, int status,int page, int pageSize) {
        // 查询所有数据，分页,
        String sql = "select * from code where status = " + status + " order by insert_time desc limit " + pageSize + " offset " + (page-1)*pageSize;
        Cursor cursor = db.rawQuery(sql, null);
        // 将数据转为 ArrayList
        ArrayList<Code> codes = new ArrayList<>();
        while (cursor.moveToNext()) {
            Code code = new Code(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("code")),
                    cursor.getString(cursor.getColumnIndex("yz")),
                    cursor.getString(cursor.getColumnIndex("kd")),
                    cursor.getString(cursor.getColumnIndex("insert_time")),
                    cursor.getInt(cursor.getColumnIndex("status"))
            );

            codes.add(code);
        }
        return codes;
    }


    public static long insertLog(SQLiteDatabase db,
                                String title,
                                String content,
                                String packageName,
                                String channelName
    ) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("package_name", packageName);
        values.put("channel_name", channelName);

        // 检查一分钟以内的日志是否重复
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        String now = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(date.getTime() +  60 * 1000));
        @SuppressLint("SimpleDateFormat")
        String oneMinuteAgo = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(date.getTime() - 60 * 1000));
        String sql = "select * from logs where insert_time >=  \"" + oneMinuteAgo + "\" and  insert_time <=   \"" + now + "\" and package_name = \"" + packageName + "\" and channel_name = \"" + channelName + "\"" +
                " and title = \"" + title + "\" and content = \"" + content + "\" ";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            return -1;
        }
        // 插入日志
        return db.insert("logs", null, values);

    }


    public static ArrayList<LogBeam> getLogsByPackageNameAndTime(
            SQLiteDatabase db,
            String packageName,
            int page
    ) {
        // 获取当前 年月日
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        String now = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date(date.getTime() +  24 * 60 * 60 * 1000));
       // 获取当前时间的前7天
        @SuppressLint("SimpleDateFormat")
        String sevenDaysAgo = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date(date.getTime() - 7 * 24 * 60 * 60 * 1000));

        String sql = "select * from logs where " +
                "package_name=\"" + packageName + "\" and insert_time >=  \"" + sevenDaysAgo + "\" and  insert_time <=   \"" + now + "\" order by insert_time desc limit 10 offset " + (page-1)*10;


        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);

        ArrayList<LogBeam> logs = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") LogBeam log = new LogBeam(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("package_name")),
                    cursor.getString(cursor.getColumnIndex("channel_name")),
                    cursor.getString(cursor.getColumnIndex("insert_time"))
            );
            logs.add(log);
        }
        return logs;

    }

    public static void deleteOutOfSevenDaysLogs(SQLiteDatabase db) {
        // 删除7天前的日志
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        String sevenDaysAgo = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date(date.getTime() - 7 * 24 * 60 * 60 * 1000));
        String sql = "delete from logs where insert_time < \"" + sevenDaysAgo + "\"";
        db.execSQL(sql);
    }


    public  static void deleteLogsByPackageName(SQLiteDatabase db, String packageName) {
        String sql = "delete from logs where package_name = \"" + packageName + "\"";
        db.execSQL(sql);
    }




    public static void closeDatabase(SQLiteDatabase db) {
        db.close();
    }


}
