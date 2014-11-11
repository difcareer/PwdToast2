package com.andr0day.xposed;

import android.app.Application;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;
import com.andr0day.Logger;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class PwdToast implements IXposedHookLoadPackage {

    public static Application firstApplication;

    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("cmb.pb")) {
            return;
        }
        Logger.log("start hook cmb");

        if (lpparam.isFirstApplication) {
            String appClassName = lpparam.appInfo.className;
            Logger.log(appClassName);
            findAndHookMethod(appClassName, lpparam.classLoader, "onCreate", new XC_MethodHook() {
                @Override
                public void afterHookedMethod(MethodHookParam param) {
                    firstApplication = (Application) param.thisObject;
                }
            });
            Logger.log("application cached");
        }

        findAndHookMethod("cmb.pb.ui.cmbwidget.CmbEditText$m", lpparam.classLoader, "handleMessage", Message.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Message message = (Message) param.args[0];
                String text = message.getData().getString("KeyString");
                Logger.log("firstApplication exist : " + (firstApplication != null) + " text : " + text);
                Toast.makeText(firstApplication, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
