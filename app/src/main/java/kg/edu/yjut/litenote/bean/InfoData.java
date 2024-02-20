package kg.edu.yjut.litenote.bean;

public class InfoData {
    private int version_code;
    private String version_info;
    private String apk_url;
    private String apk_size;
    private String apk_md5;
    private String apk_type;
    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }
    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_info(String version_info) {
        this.version_info = version_info;
    }
    public String getVersion_info() {
        return version_info;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }
    public String getApk_url() {
        return apk_url;
    }

    public void setApk_size(String apk_size) {
        this.apk_size = apk_size;
    }
    public String getApk_size() {
        return apk_size;
    }

    public void setApk_md5(String apk_md5) {
        this.apk_md5 = apk_md5;
    }
    public String getApk_md5() {
        return apk_md5;
    }

    public void setApk_type(String apk_type) {
        this.apk_type = apk_type;
    }
    public String getApk_type() {
        return apk_type;
    }
}
