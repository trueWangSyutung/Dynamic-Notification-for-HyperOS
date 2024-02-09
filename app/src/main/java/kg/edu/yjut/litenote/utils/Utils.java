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
}
