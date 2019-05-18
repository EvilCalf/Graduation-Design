package cn.dhbin.qqrobot;

import android.app.Application;

import com.tencent.bugly.Bugly;

/**
 * Created by DHB on 2017/2/10.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "7130d0ffae", false);
    }
}
