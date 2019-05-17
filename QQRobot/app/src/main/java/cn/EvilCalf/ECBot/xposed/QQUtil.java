package cn.EvilCalf.ECBot.xposed;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import cn.EvilCalf.ECBot.xposed.utils.XposedUtil;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import static cn.EvilCalf.ECBot.Common.BaseActivity;
import static cn.EvilCalf.ECBot.Common.FrameHelperActivity;
import static cn.EvilCalf.ECBot.Common.QLog;
import static cn.EvilCalf.ECBot.Common.QQAppInterface;
import static cn.EvilCalf.ECBot.Common.QQSettingMe;
import static cn.EvilCalf.ECBot.Common.Setting_Layout_Id;
import static cn.EvilCalf.ECBot.Common.Setting_TextView_Id;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.setStaticIntField;



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
                                    ComponentName cn = new ComponentName("cn.EvilCalf.ECBot", "cn.EvilCalf.ECBot.MainActivity");
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
