package kg.edu.yjut.litenote.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class RegexMangerHelper {
    SharedPreferences sharedPreferencesHelper;

    public RegexMangerHelper(Context context) {
        sharedPreferencesHelper = context.getSharedPreferences("regex_manager", Context.MODE_PRIVATE);
    }

    public String getCompanyRegex() {
        return sharedPreferencesHelper.getString("companyRegex", "【(.*?)】");
    }

    public String getYizhanRegex() {
        return sharedPreferencesHelper.getString("yizhanRegex", "到(.*?)驿站");
    }

    public String getQujianRegex() {
        return sharedPreferencesHelper.getString("qujianRegex", "凭(.*?)在|凭(.*?)至|凭(.*?)到|凭(.*?)取");
    }

    public void addCompanyRegex(String companyRegex) {
        // 读取 sharedPreference
        String companyRegexOld = sharedPreferencesHelper.getString("companyRegex", "【(.*?)】");
        // 获取 sharedPreference 的编辑器
        SharedPreferences.Editor editor = sharedPreferencesHelper.edit();
        // 在原有的基础上添加 |companyRegex
        companyRegex = companyRegexOld + "|" + companyRegex;
        // 提交
        editor.putString("companyRegex", companyRegex);
        editor.apply();
    }

    public void addYizhanRegex(String yizhanRegex) {
        // 读取 sharedPreference
        String yizhanRegexOld = sharedPreferencesHelper.getString("yizhanRegex", "到(.*?)驿站");
        // 获取 sharedPreference 的编辑器
        SharedPreferences.Editor editor = sharedPreferencesHelper.edit();
        // 在原有的基础上添加 |yizhanRegex
        yizhanRegex = yizhanRegexOld + "|" + yizhanRegex;
        // 提交
        editor.putString("yizhanRegex", yizhanRegex);
        editor.apply();
    }

    public void addQujianRegex(String qujianRegex) {
        // 读取 sharedPreference
        String qujianRegexOld = sharedPreferencesHelper.getString("qujianRegex", "凭(.*?)在|凭(.*?)至|凭(.*?)到|凭(.*?)取");
        // 获取 sharedPreference 的编辑器
        SharedPreferences.Editor editor = sharedPreferencesHelper.edit();
        // 在原有的基础上添加 |qujianRegex
        qujianRegex = qujianRegexOld + "|" + qujianRegex;
        // 提交
        editor.putString("qujianRegex", qujianRegex);
        editor.apply();
    }

    public void saveCompanyRegex(String companyRegex) {
        // 获取 sharedPreference 的编辑器
        SharedPreferences.Editor editor = sharedPreferencesHelper.edit();
        // 提交
        editor.putString("companyRegex", companyRegex);
        editor.apply();
    }

    public void saveYizhanRegex(String yizhanRegex) {
        // 获取 sharedPreference 的编辑器
        SharedPreferences.Editor editor = sharedPreferencesHelper.edit();
        // 提交
        editor.putString("yizhanRegex", yizhanRegex);
        editor.apply();
    }

    public void saveQujianRegex(String qujianRegex) {
        // 获取 sharedPreference 的编辑器
        SharedPreferences.Editor editor = sharedPreferencesHelper.edit();
        // 提交
        editor.putString("qujianRegex", qujianRegex);
        editor.apply();
    }

    public void removeCompanyRegex(String companyRegex) {
        // 读取 sharedPreference
        String companyRegexOld = sharedPreferencesHelper.getString("companyRegex", "【(.*?)】");
        // 获取 sharedPreference 的编辑器
        SharedPreferences.Editor editor = sharedPreferencesHelper.edit();
        // 在原有的基础上删除 |companyRegex
        companyRegex = companyRegexOld.replace("|" + companyRegex, "");
        // 提交
        editor.putString("companyRegex", companyRegex);
        editor.apply();
    }

    public void removeYizhanRegex(String yizhanRegex) {
        // 读取 sharedPreference
        String yizhanRegexOld = sharedPreferencesHelper.getString("yizhanRegex", "到(.*?)驿站");
        // 获取 sharedPreference 的编辑器
        SharedPreferences.Editor editor = sharedPreferencesHelper.edit();
        // 在原有的基础上删除 |yizhanRegex
        yizhanRegex = yizhanRegexOld.replace("|" + yizhanRegex, "");
        // 提交
        editor.putString("yizhanRegex", yizhanRegex);
        editor.apply();
    }

    public void removeQujianRegex(String qujianRegex) {
        // 读取 sharedPreference
        String qujianRegexOld = sharedPreferencesHelper.getString("qujianRegex", "凭(.*?)在|凭(.*?)至|凭(.*?)到|凭(.*?)取");
        // 获取 sharedPreference 的编辑器
        SharedPreferences.Editor editor = sharedPreferencesHelper.edit();
        // 在原有的基础上删除 |qujianRegex
        qujianRegex = qujianRegexOld.replace("|" + qujianRegex, "");
        // 提交
        editor.putString("qujianRegex", qujianRegex);
        editor.apply();
    }

    public void closeRegexManger() {
        sharedPreferencesHelper = null;

    }



    public static void initRegexManger(Context context) {
        // 获取 sharedPreference
        SharedPreferences sharedPreferencesHelper = context.getSharedPreferences("regex_manager", Context.MODE_PRIVATE);
        // 获取 sharedPreference 的编辑器
        SharedPreferences.Editor editor = sharedPreferencesHelper.edit();
        // 提取出内容中 的 【xxxx】 (公司名)
        // 到xxxx驿站 (驿站名)
        // 凭xxxx在、凭xxx至、凭xxx到、凭xxx取 （取件码）
        // 添加  companyRegex
        editor.putString("companyRegex", "【(.*?)】");
        // 添加  yizhanRegex
        editor.putString("yizhanRegex", "到(.*?)驿站");
        // 添加  qujianRegex
        editor.putString("qujianRegex", "凭(.*?)在|凭(.*?)至|凭(.*?)到|凭(.*?)取");
        // 提交
        editor.apply();
    }
}
