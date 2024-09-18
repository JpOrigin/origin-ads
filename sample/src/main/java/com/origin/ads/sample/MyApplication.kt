package com.origin.ads.sample

import android.app.Application
import com.origin.ads.initializeMobileAds
import com.origin.ads.utils.initLogX

class MyApplication : Application() {

    init {
        mMyApplicationInstance = this
    }
    companion object{
        lateinit var mMyApplicationInstance: MyApplication
        @Synchronized
        fun getInstance(): MyApplication {
            var app: MyApplication
            synchronized(MyApplication::class.java) {
                app = mMyApplicationInstance
            }
            return app
        }
    }

    override fun onCreate() {
        super.onCreate()
        mMyApplicationInstance = this
        this@MyApplication.initializeMobileAds()
        initLogX(BuildConfig.DEBUG)
    }


}
