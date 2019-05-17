package cn.EvilCalf.ECBot.xposed.utils;

import android.app.Activity;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;



public class XposedUtil {

    public static void markedAllMethod(String className, ClassLoader classLoader) {
        Class aClass = XposedHelpers.findClass(className, classLoader);
        markedAllMethod(aClass);
    }

    public static void markedAllMethod(Class aClass) {
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method :
                methods) {
            Object[] parameterTypes = method.getParameterTypes();
            Object[] parameterTypesAndCallback = new Object[parameterTypes.length + 1];
            System.arraycopy(parameterTypes, 0, parameterTypesAndCallback, 0, parameterTypes.length);
            parameterTypesAndCallback[parameterTypesAndCallback.length - 1] = new MarkedAllMethodCallback(method);
            XposedHelpers.findAndHookMethod(aClass, method.getName(), parameterTypesAndCallback);
        }
    }

    private static class MarkedAllMethodCallback extends XC_MethodHook {

        private String methodName;

        MarkedAllMethodCallback(Method method) {
            methodName = method.toGenericString();
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            XLogUtil.d(getTime(), methodName);
        }
    }

    public static String getTime() {
        SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        dateFormat.applyPattern("HH:mm:ss:SS");
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }

    public static void showAllField(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object o = field.get(object);
                if (o != null) XLogUtil.d(field.toGenericString(), o.toString());
                else XLogUtil.d(field.toGenericString(), "null");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setField(Object object, String fieldName, Object value, Type type) {
//        Field[] fields = object.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            if (field.getType() == type && field.getName().equals(fieldName)) {
//                field.setAccessible(true);
//                try {
//                    field.set(object, value);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        Field field = getField(object, fieldName, type);
        if (field == null) return;
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Field getField(Object object, String fieldName, Type type) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == type && field.getName().equals(fieldName)) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    public static void markAllActivity() {
        findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Toast.makeText((Activity) param.thisObject, param.thisObject.getClass().getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
