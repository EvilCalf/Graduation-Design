package cn.EvilCalf.ECBot.xposed.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XSharedPreferences;



public class XPreferenceUtil {

    private static XSharedPreferences preferences;

    private static XSharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = new XSharedPreferences("cn.EvilCalf.ECBot");
            preferences.makeWorldReadable();
        } else {
            preferences.reload();
        }
        return preferences;
    }

    public static boolean getMasterSwitch() {
        return getPreferences().getBoolean("master_switch", true);
    }

    public static String[] getWhiteList() {
        String s = getPreferences().getString("white_list", "");
        return s.split(",");
    }

    public static String getApiKey() {
        return getPreferences().getString("robot_api_key", "");
    }

    public static String getSecret() {
        return getPreferences().getString("robot_secret", "");
    }

    public static ArrayList<Map<String, String>> getKeyReply() {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        String s = getPreferences().getString("key_reply", "");
        String[] strings = s.trim().split(" ");
        for (String s1 : strings) {
            String[] strings1 = s1.trim().split(":");
            if (strings1.length == 2) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("content", strings1[0]);
                hashMap.put("reply_content", strings1[1]);
                arrayList.add(hashMap);
            }
        }
        return arrayList;
    }

    public static boolean getNoReplyTroop() {
        return getPreferences().getBoolean("no_reply_troop", false);
    }
}
