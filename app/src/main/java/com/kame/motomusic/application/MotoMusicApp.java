package com.kame.motomusic.application;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;
import com.kame.motomusic.R;
import com.tencent.bugly.Bugly;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import com.kame.motomusic.BuildConfig;
import com.kame.motomusic.http.HttpInterceptor;
import okhttp3.OkHttpClient;

/**
 * 自定义Application
 * Created by wcy on 2015/11/27.
 */
public class MotoMusicApp extends Application {

    @Override
    public void onCreate() {
        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用半角“,”分隔。
        // 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
        SpeechUtility.createUtility(MotoMusicApp.this, "appid=" + getString(R.string.app_id));

        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        // Setting.setShowLog(false);

        super.onCreate();

        AppCache.init(this);
        ForegroundObserver.init(this);
        initOkHttpUtils();
        initBugly();
    }

    private void initOkHttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    private void initBugly() {
        if (!BuildConfig.DEBUG) {
            Bugly.init(this, BuildConfig.BUGLY_APP_ID, false);
        }
    }
}
