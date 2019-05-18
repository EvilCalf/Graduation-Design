package cn.dhbin.qqrobot.robot;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import cn.dhbin.qqrobot.bean.TulingRequest;
import cn.dhbin.qqrobot.bean.TulingResponse;

/**
 * Created by DHB on 2017/2/10.
 */

public class Robot {
    private RobotType mRobotType;
    private StringCallback mStringCallback;

    public enum RobotType {
        TULING
    }

    public Robot() {

    }
    public Robot(RobotType type) {
        mRobotType = type;
    }

    public void init(String key, String info, String userid) {
        if (mRobotType.equals(RobotType.TULING)) {
            tulingRobot(key, info, userid);
        }
    }

    private void tulingRobot(String key, String info, String useid) {
        RequestCall requestCall = OkHttpUtils
                .postString()
                .url("http://www.tuling123.com/openapi/api")
                .content(new Gson().toJson(new TulingRequest(key, info, useid)))
                .build();
        if (mStringCallback != null) {
            requestCall.execute(mStringCallback);
        }
    }

    public static String tulingReply(String json) {
        TulingResponse response = new Gson().fromJson(json, TulingResponse.class);
        if (response == null) return "";
        if (response.getCode().equals("100000")) {
            return response.getText();
        }
        if (response.getCode().equals("200000 ")) {
            return response.getText() + "\n" + response.getUrl();
        }
        return "";
    }

    public void setCallback(StringCallback callback) {
        mStringCallback = callback;
    }

    public void setRobotType(RobotType robotType) {
        mRobotType = robotType;
    }
}
