package cn.dhbin.qqrobot.xposed;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import cn.dhbin.qqrobot.Common;
import cn.dhbin.qqrobot.xposed.utils.XLogUtil;
import cn.dhbin.qqrobot.xposed.utils.XposedUtil;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import static cn.dhbin.qqrobot.Common.BaseActivity;
import static cn.dhbin.qqrobot.Common.FrameHelperActivity;
import static cn.dhbin.qqrobot.Common.QLog;
import static cn.dhbin.qqrobot.Common.QQAppInterface;
import static cn.dhbin.qqrobot.Common.QQSettingMe;
import static cn.dhbin.qqrobot.Common.Setting_Layout_Id;
import static cn.dhbin.qqrobot.Common.Setting_TextView_Id;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.setStaticIntField;

/**
 * Created by DHB on 2017/2/9.
 */

public class QQUtil extends BaseHook{

    private Context mContext;
    private QQUtil(ClassLoader classLoader) {
        super(classLoader);
    }

    QQUtil(ClassLoader classLoader, Context context) {
        super(classLoader);
        mContext = context;
    }

    public void injectQqSettingLayout() {
        XposedHelpers.findAndHookConstructor(QQSettingMe, classLoader,
                BaseActivity,
                QQAppInterface,
                FrameHelperActivity, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Field viewsField = XposedUtil.getField(param.thisObject, "a", View[].class);
                        if (viewsField != null) {
                            View[] views = (View []) viewsField.get(param.thisObject);
                            final LinearLayout linearLayout = (LinearLayout) views[0].getParent();
                            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                            LinearLayout injectLayout = (LinearLayout) layoutInflater.inflate(Setting_Layout_Id, null);
                            ((TextView)injectLayout.findViewById(Setting_TextView_Id)).setText("QQ机器人");
                            injectLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                    ComponentName cn = new ComponentName("cn.dhbin.qqrobot", "cn.dhbin.qqrobot.MainActivity");
                                    intent.setComponent(cn);
                                    linearLayout.getContext().startActivity(intent);
                                }
                            });
                            linearLayout.addView(injectLayout);
                        }
                    }
                });
    }


    public static void setLog(final ClassLoader classLoader) {
        findAndHookMethod(QLog, classLoader, "d",
                String.class,
                int.class,
                String.class,
                Throwable.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        setStaticIntField(XposedHelpers.findClass(QLog, classLoader), "UIN_REPORTLOG_LEVEL", 1000);
                    }
                });
    }
}
