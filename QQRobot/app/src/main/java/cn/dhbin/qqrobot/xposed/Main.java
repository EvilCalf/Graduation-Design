package cn.dhbin.qqrobot.xposed;

import android.app.Application;
import android.content.pm.PackageInfo;

import cn.dhbin.qqrobot.Common;
import cn.dhbin.qqrobot.xposed.utils.XLogUtil;
import cn.dhbin.qqrobot.xposed.utils.XPreferenceUtil;
import cn.dhbin.qqrobot.xposed.utils.XposedUtil;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static cn.dhbin.qqrobot.Common.InjectUtils;
import static cn.dhbin.qqrobot.Common.QLog;
import static cn.dhbin.qqrobot.Common.QQ_PACKAGE_NAME;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.setStaticIntField;

/**
 * Created by DHB on 2017/2/8.
 */

public class Main implements IXposedHookLoadPackage, IXposedHookInitPackageResources {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam packageParam) throws Throwable {
        XposedUtil.markAllActivity();
        if (!packageParam.packageName.equals(QQ_PACKAGE_NAME) && !packageParam.processName.equals(QQ_PACKAGE_NAME))
            return;

        findAndHookMethod(InjectUtils, packageParam.classLoader, "injectExtraDexes",
                Application.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        final Application application = (Application) param.args[0];
                        PackageInfo packageInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
                        String QqVersion = packageInfo.versionName;
                        XLogUtil.d("QQ Version", QqVersion);
                        Common.initVersion(QqVersion);

                        MessageUtil messageUtil = new MessageUtil(application.getClassLoader(), application);
                        messageUtil.initAutoReply();

                        QQUtil.setLog(application.getClassLoader());

                        QQUtil qqUtil = new QQUtil(application.getClassLoader(), application);
                        qqUtil.injectQqSettingLayout();
                    }
                });
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {

    }
}
