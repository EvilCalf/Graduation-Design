package cn.EvilCalf.ECBot.xposed.utils;

import de.robv.android.xposed.XposedBridge;



public class XLogUtil {
    private enum Mode {
        RELEASE, DEBUG
    }

    private static Mode mMode = Mode.RELEASE;

    public static void setMode(Mode mode) {
        mMode = mode;
    }

    private static void log(String tag, String log) {
        XposedBridge.log("[" + tag + "]" + log);
    }

    public static void e(String tag, String log) {
        log(tag, log);
    }

    public static void d(String tag, String log) {
        if (mMode.equals(Mode.DEBUG)) {
            log(tag, log);
        }
    }
}
