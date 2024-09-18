package com.origin.ads.splashopenads

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.origin.ads.utils.logE
import java.util.Date

class GoogleSplashAppOpenAdManager {
    private var appOpenAdSplash: AppOpenAd? = null

    private var isSplashActivityPaused = false
    fun pauseAds() {
        isSplashActivityPaused = true
        removeDelayHandlerCallbacks()
    }
    fun resumeAds() {
        isSplashActivityPaused = false
    }

    private var loadTime: Long = 0
    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long = 4L): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAdSplash != null && wasLoadTimeLessThanNHoursAgo()
    }

    interface OnShowAdCompleteListener {
        fun onShowAd()
        fun onShowAdComplete()
    }

    var isLoadingSplashAd: Boolean = false
    fun loadAd(activity: Activity, adsUnitId: String, onShowAdCompleteListener: OnShowAdCompleteListener) {
        if (isAdAvailable()) {
            showAdIfAvailableWithDelay(activity, onShowAdCompleteListener)
            return
        }
        if (isLoadingSplashAd) {
            return
        }
        isLoadingSplashAd = true
        val request = AdRequest.Builder().build()
        logE("glSplashAppOpenAds::load:request_new_ads")
        AppOpenAd.load(activity, adsUnitId, request, object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                logE("glSplashAppOpenAds::load:adLoaded")
                appOpenAdSplash = ad
                isLoadingSplashAd = false
                loadTime = Date().time
                showAdIfAvailable(activity, onShowAdCompleteListener)
            }
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                logE("glSplashAppOpenAds::load:adFailedToLoad:: ${loadAdError.message}")
                isLoadingSplashAd = false
            }
        })
    }

    var isShowingSplashAd = false
    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        if (isShowingSplashAd || isSplashActivityPaused) {
            return
        }
        if (isAdAvailable()) {
            appOpenAdSplash?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    logE("glSplashAppOpenAds::show:DismissedAds")
                    appOpenAdSplash = null
                    isShowingSplashAd = false
                    onShowAdCompleteListener.onShowAdComplete()
                }
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    logE("glSplashAppOpenAds::show:FailedToShow:: ${adError.message}")
                    appOpenAdSplash = null
                    isShowingSplashAd = false
                    onShowAdCompleteListener.onShowAdComplete()
                }
                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    logE("glSplashAppOpenAds::show:ShowedAds")
                }
            }
            isShowingSplashAd = true
            appOpenAdSplash?.show(activity)
            logE("glSplashAppOpenAds::show:callShowAds")
            onShowAdCompleteListener.onShowAd()
        }
    }

    private var mHandlerDelay: Handler? = null
    private var mRunnableDelay: Runnable? = null
    private fun removeDelayHandlerCallbacks() {
        if (mHandlerDelay != null && mRunnableDelay != null) {
            mHandlerDelay?.removeCallbacks(mRunnableDelay!!)
        }
    }

    fun showAdIfAvailableWithDelay(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        removeDelayHandlerCallbacks()
        mHandlerDelay = Handler(Looper.getMainLooper())
        mRunnableDelay = Runnable {
            showAdIfAvailable(activity, onShowAdCompleteListener)
        }
        mHandlerDelay?.postDelayed(mRunnableDelay!!, 1800L)
    }
}