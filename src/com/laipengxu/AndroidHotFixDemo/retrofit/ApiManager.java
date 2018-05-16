package com.laipengxu.AndroidHotFixDemo.retrofit;


import com.laipengxu.AndroidHotFixDemo.retrofit.api.VersionUpgradeService;
import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/6/6.
 * Api管理
 */
public class ApiManager {
    private static ApiManager sApiManager;

    private ApiManager() {
    }

    public static ApiManager getInstance() {
        if (sApiManager == null) {
            synchronized (ApiManager.class) {
                if (sApiManager == null) {
                    sApiManager = new ApiManager();
                }
            }
        }
        return sApiManager;
    }


    public Observable<byte[]> downloadVersion(String downloadUrl) {
        return RetrofitManager
                .createApi(VersionUpgradeService.class, RetrofitManager.getDownloadClient())
                .downloadVersion(downloadUrl);
    }

}
