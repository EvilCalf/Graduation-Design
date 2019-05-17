package cn.EvilCalf.ECBot.bean;



public class TulingRequest {
    private String key;
    private String info;
    private String userid;

    public TulingRequest(String key, String info, String userid) {
        this.key = key;
        this.info = info;
        this.userid = userid;
    }
    public String getKey() {
        return key;
    }

    public String getInfo() {
        return info;
    }

    public String getUserid() {
        return userid;
    }
}
