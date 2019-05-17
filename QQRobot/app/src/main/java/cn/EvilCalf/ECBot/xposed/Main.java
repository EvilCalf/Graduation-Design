package cn.EvilCalf.ECBot.xposed;

import android.app.Application;
import android.content.pm.PackageInfo;

import cn.EvilCalf.ECBot.Common;
import cn.EvilCalf.ECBot.xposed.utils.XLogUtil;
import cn.EvilCalf.ECBot.xposed.utils.XposedUtil;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static cn.EvilCalf.ECBot.Common.InjectUtils;
import static cn.EvilCalf.ECBot.Common.QQ_PACKAGE_NAME;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;



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
