package cn.EvilCalf.ECBot;



public class Common {
    public static final String QQ_PACKAGE_NAME = "com.tencent.mobileqq";
    public static final String InjectUtils = "com.tencent.mobileqq.app.InjectUtils";
    public static final String MessageHandlerUtils = "com.tencent.mobileqq.app.MessageHandlerUtils";
    public static final String QQAppInterface = "com.tencent.mobileqq.app.QQAppInterface";
    public static final String MessageRecord = "com.tencent.mobileqq.data.MessageRecord";
    public static final String ChatActivityFacade = "com.tencent.mobileqq.activity.ChatActivityFacade";
    public static final String SessionInfo = "com.tencent.mobileqq.activity.aio.SessionInfo";
    public static final String SendMsgParams = "com.tencent.mobileqq.activity.ChatActivityFacade$SendMsgParams";
    public static final String QLog = "com.tencent.qphone.base.util.QLog";
    public static final String BaseApplicationImpl = "com.tencent.common.app.BaseApplicationImpl";
    public static final String QQSettingMe = "com.tencent.mobileqq.activity.QQSettingMe";
    public static final String BaseActivity = "com.tencent.mobileqq.app.BaseActivity";
    public static final String FrameHelperActivity = "com.tencent.mobileqq.app.FrameHelperActivity";

    public static int Setting_Layout_Id = 0x7f0405cb;
    public static int Setting_TextView_Id = 0x7f0a1be5;


    public static void initVersion(String versionName) {
        switch (versionName) {
            case "6.6.9":
                Setting_Layout_Id = 0x7f04059c;
                Setting_TextView_Id = 0x7f0a1af3;
                break;
            case "6.7.0":
                Setting_Layout_Id = 0x7f0405cb;
                Setting_TextView_Id = 0x7f0a1be5;
                break;
        }
    }
}
