package com.origin.ads.demo;

import static com.origin.ads.GoogleAdsKt.initializeMobileAds;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeMobileAds(MyApplication.this);
    }
}
