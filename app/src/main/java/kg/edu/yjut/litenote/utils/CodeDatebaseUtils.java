package kg.edu.yjut.litenote.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import kg.edu.yjut.litenote.bean.Code;
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



    public static void closeDatabase(SQLiteDatabase db) {
        db.close();
    }


}
