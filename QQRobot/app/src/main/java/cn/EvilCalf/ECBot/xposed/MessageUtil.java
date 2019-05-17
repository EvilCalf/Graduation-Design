package cn.EvilCalf.ECBot.xposed;

import android.content.Context;

import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import cn.EvilCalf.ECBot.robot.Robot;
import cn.EvilCalf.ECBot.xposed.utils.XLogUtil;
import cn.EvilCalf.ECBot.xposed.utils.XPreferenceUtil;
import cn.EvilCalf.ECBot.xposed.utils.XposedUtil;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import okhttp3.Call;

import static cn.EvilCalf.ECBot.Common.BaseApplicationImpl;
import static cn.EvilCalf.ECBot.Common.ChatActivityFacade;
import static cn.EvilCalf.ECBot.Common.MessageHandlerUtils;
import static cn.EvilCalf.ECBot.Common.MessageRecord;
import static cn.EvilCalf.ECBot.Common.QQAppInterface;
import static cn.EvilCalf.ECBot.Common.SendMsgParams;
import static cn.EvilCalf.ECBot.Common.SessionInfo;
import static cn.EvilCalf.ECBot.xposed.utils.XPreferenceUtil.getApiKey;
import static cn.EvilCalf.ECBot.xposed.utils.XPreferenceUtil.getKeyReply;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.newInstance;



public class MessageUtil extends BaseHook {

    private Object sApplication;
    private Context mContext;

    private MessageUtil(ClassLoader classLoader) {
        super(classLoader);

    }

    MessageUtil(ClassLoader classLoader, Context context) {
        super(classLoader);
        mContext = context;
    }

    public void initAutoReply() {
        sApplication = callStaticMethod(findClass(BaseApplicationImpl, classLoader), "getApplication");
        findAndHookMethod(MessageHandlerUtils, classLoader, "a",
                QQAppInterface,
                MessageRecord,
                boolean.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XLogUtil.d("总开关", XPreferenceUtil.getMasterSwitch() + "");
                        if (!XPreferenceUtil.getMasterSwitch()) return;

                        final String frienduin = getObjectField(param.args[1], "frienduin").toString();
                        final String selfuin = getObjectField(param.args[1], "selfuin").toString();
                        final int istroop = (int) getObjectField(param.args[1], "istroop");
                        final String senderuin = getObjectField(param.args[1], "senderuin").toString();
                        final String msg = getObjectField(param.args[1], "msg").toString();
                        boolean isread = (boolean) getObjectField(param.args[1], "isread");

                        XLogUtil.d("frienduin", frienduin);
                        XLogUtil.d("selfuin", selfuin);
                        XLogUtil.d("senderuin", senderuin);

                        String[] whiteList = XPreferenceUtil.getWhiteList();

                        if (whiteList.length != 0 && !senderuin.equals(selfuin)) {
                            if (XPreferenceUtil.getNoReplyTroop() && istroop == 1) return;
                            iteratorWhiteList(frienduin, selfuin, msg, senderuin, istroop, isread, whiteList);
                        }
                    }
                });
    }

    private void iteratorWhiteList(String frienduin, String selfuin, String msg, String senderuin, int istroop, boolean isread, String[] list) {
        for (String s : list) {
            if (frienduin.equals(s) && !isread && !frienduin.equals(selfuin)) {
                XLogUtil.d("MessageUtil", "iteratorWhiteList is running...");
                ArrayList<Map<String, String>> keyReply = getKeyReply();
                if (keyReply != null && keyReply.size() != 0) {
                    for (Map<String, String> stringMap: keyReply){
                        if (stringMap.get("content").trim().equals(msg)) {
                            send(frienduin, selfuin, istroop, stringMap.get("reply_content"));
                            return;
                        }
                    }
                }
                Robot robot = new Robot(Robot.RobotType.TULING);
                robot.setCallback(new ReplyCallback(frienduin, selfuin, istroop));
                robot.init(getApiKey(), msg, senderuin);
            }
        }
    }

    private void send(String frienduin, String selfuin, int istroop, String s) {
        Object qqAppInterface = callMethod(sApplication, "getAppRuntime", selfuin);
        Object sessionInfo = newInstance(findClass(SessionInfo, classLoader));
        Object sendMsgParams = newInstance(findClass(SendMsgParams, classLoader));
        XposedUtil.setField(sessionInfo, "a", frienduin, String.class);
        XposedUtil.setField(sessionInfo, "a", istroop, int.class);
        callStaticMethod(XposedHelpers.findClass(ChatActivityFacade, classLoader), "a", qqAppInterface, mContext, sessionInfo, s, new ArrayList<>(), sendMsgParams);
    }

    private class ReplyCallback extends StringCallback {

        private String frienduin;
        private String selfuin;
        private int istroop;

        ReplyCallback(String frienduin, String selfuin, int istroop) {
            this.frienduin = frienduin;
            this.selfuin = selfuin;
            this.istroop = istroop;
        }
        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(String response, int id) {
            send(frienduin, selfuin, istroop, Robot.tulingReply(response));
        }
    }
}
