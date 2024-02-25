package kg.edu.yjut.litenote.utils;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import kg.edu.yjut.litenote.widget.NearExpressWidget;

public class Utils {
    @SuppressLint("NewApi")
    public static void startAppWidgetPage(Context context){

        AppWidgetManager appWidgetManager =

                AppWidgetManager.getInstance(context);

        ComponentName myProvider = new ComponentName(context,

                NearExpressWidget.class);
        if (appWidgetManager.isRequestPinAppWidgetSupported()) {

            Bundle extras = new Bundle();

            extras.putString("addType","appWidgetDetail");

// packageName 为应用真实包名
            extras.putString("widgetName",

                    "kg.edu.yjut.litenote/.widget.NearExpressWidget");

            Bundle dataBundle = new Bundle();

            dataBundle.putString("dataKey1","data1xxx");

            dataBundle.putString("dataKey2","data2xxx");

            dataBundle.putString("dataKey3","data3xxx");

            dataBundle.putString("dataKey4","data4xxx");

            dataBundle.putString("dataKey5","data5xxx");

            extras.putBundle("widgetExtraData", dataBundle);

            appWidgetManager.requestPinAppWidget(myProvider, extras, null);

        }

    }


    public static Boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    // 获取应用版本号
    public static String getVersionName(Context context) {
        String versionName = "1.0.0";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    // 获取应用版本代码
public static int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    // 获取应用编译时间
    public static String getBuildTime(Context context) {
        String buildTime = "2019-01-01 00:00:00";
        try {
            buildTime = context.getString(context.getResources().getIdentifier("build_time", "string", context.getPackageName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buildTime;
    }

    // 获取应用开发者
    public static String getDeveloper(Context context) {
        String developer = "Syutung";
        return developer;
    }


}
