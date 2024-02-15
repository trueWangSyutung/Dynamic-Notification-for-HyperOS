package kg.edu.yjut.litenote.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.Date;

import kg.edu.yjut.litenote.bean.Log;
import kg.edu.yjut.litenote.bean.LogsInfo;

public class LogManager {
    SharedPreferences sharedPreferencesHelper;

    public LogManager(Context context, String packageName) {
        sharedPreferencesHelper = context.getSharedPreferences("log_" + packageName, Context.MODE_PRIVATE);
    }

    public LogsInfo getPackageLog() {
        // 获取当天的日志
        Date date = new Date();
        String today = date.getYear() + "-" + date.getMonth() + "-" + date.getDate();
        return new Gson().fromJson(sharedPreferencesHelper.getString("log_" + today, ""), LogsInfo.class);
    }

    public void addLog(Log log) {
        // 获取当天的日志
        Date date = new Date();
        String today = date.getYear() + "-" + date.getMonth() + "-" + date.getDate();
        LogsInfo logsInfo = new Gson().fromJson(sharedPreferencesHelper.getString("log_" + today, ""), LogsInfo.class);
        logsInfo.getLogs().add(log);

        // 获取 sharedPreference 的编辑器
        SharedPreferences.Editor editor = sharedPreferencesHelper.edit();
        // 提交
        editor.putString("log_" + today, new Gson().toJson(logsInfo));

        editor.apply();
    }

    public void clearSevenDaysLogs() {
        // 获取当天的日志
        Date date = new Date();
        String today = date.getYear() + "-" + date.getMonth() + "-" + date.getDate();
        LogsInfo logsInfo = new Gson().fromJson(sharedPreferencesHelper.getString("log_" + today, ""), LogsInfo.class);
        logsInfo.getLogs().clear();

        // 获取 sharedPreference 的编辑器
        SharedPreferences.Editor editor = sharedPreferencesHelper.edit();
        // 提交
        editor.putString("log_" + today, new Gson().toJson(logsInfo));

        editor.apply();
    }

}
