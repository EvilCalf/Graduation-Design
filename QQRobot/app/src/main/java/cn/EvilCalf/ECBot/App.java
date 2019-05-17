package cn.EvilCalf.ECBot;

import android.app.Application;

import com.tencent.bugly.Bugly;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "7130d0ffae", false);
    }
}
