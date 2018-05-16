package com.laipengxu.AndroidHotFixDemo.application;

import android.app.Application;
import android.content.pm.PackageManager;
import com.alipay.euler.andfix.patch.PatchManager;

/**
 * Created by 赖鹏旭 on 2017/9/20.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;
    private PatchManager mPatchManager;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initHotfix();
    }

    /**
     * 初始化热修复框架
     */
    private void initHotfix() {
        String appVersion = "";
        try {
            appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mPatchManager = new PatchManager(this);
        mPatchManager.init(appVersion);//current version
        mPatchManager.loadPatch();
    }

    public PatchManager getPatchManager() {
        return mPatchManager;
    }
}
