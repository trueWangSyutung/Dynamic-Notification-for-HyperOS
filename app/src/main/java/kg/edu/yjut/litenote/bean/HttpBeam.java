package kg.edu.yjut.litenote.bean;

public class HttpBeam {
    private int code;
    private String msg;
    private InfoData data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setData(InfoData data) {
        this.data = data;
    }
    public InfoData getData() {
        return data;
    }
}
