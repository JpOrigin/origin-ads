package com.origin.ads.appopenads

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.origin.ads.utils.logE
import java.util.Date


class GoogleAppOpenAdManager : ActivityLifecycleCallbacks, LifecycleEventObserver {
    private var mActivity: Activity? = null
    private var mApplication: Application? = null
    private var mAdsUnitId: String = ""
    private var isSkipAppOpenAds: Boolean = false

    companion object {
        var mAppOpenAd: AppOpenAd? = null
        var mLastLoadAppOpenAdTime: Long = 0

        var isSkipAppOpenAdsOnce = false

        /**
         * Set a timeMillis for skip app open Ads once.
         *
         * Large intent took more time to load/show other screen. In that case, simply increase timeMillis.
         */
        private var isTimerOn = false
        private var mCountDownTimer: CountDownTimer? = null
        fun skipAppOpenAdsOnce(timeMillis: Long = 500L) {
            if (isTimerOn) {
                mCountDownTimer?.apply {
                    this.cancel()
                    mCountDownTimer = null
                }
            }
            mCountDownTimer = object : CountDownTimer(timeMillis, 10) {
                override fun onTick(millisUntilFinished: Long) {
                    isTimerOn = true
                    isSkipAppOpenAdsOnce = true
                }

                override fun onFinish() {
                    isTimerOn = false
                    isSkipAppOpenAdsOnce = true
                }
            }.start()
        }

        var isForceStopAppOpenAds = false
        fun pauseAppOpenAds() {
            isForceStopAppOpenAds = true
        }

        fun resumeAppOpenAds() {
            isForceStopAppOpenAds = false
        }
    }

    fun initialize(application: Application, adsUnitId: String, skipAppOpenAds: Boolean) {
        this.mApplication = application
        this.mAdsUnitId = adsUnitId
        this.isSkipAppOpenAds = skipAppOpenAds
        try {
            this.mApplication?.registerActivityLifecycleCallbacks(this)
        } catch (_: Exception) {
        }
        try {
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        } catch (_: Exception) {
        }
        registerScreenOnOffReceiver()
    }

    fun destroyAppOpenAds() {
        try {
            this.mApplication?.unregisterActivityLifecycleCallbacks(this)
        } catch (_: Exception) {
        }
        try {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        } catch (_: Exception) {
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        if (!isShowingAppOpenAd) {
            mActivity = activity
        }
    }

    private var isAnyActivityPaused = -1 // true = 1, false = 0
    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        mActivity = null
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            if (isAnyActivityPaused == -1) {
                return
            }
            if (isSkipAppOpenAdsOnce) {
                isSkipAppOpenAdsOnce = false
                isShowingAppOpenAd = true
                setEventOnStartCalled(false)
            } else if (isForceStopAppOpenAds) {
                isShowingAppOpenAd = true
                setEventOnStartCalled(false)
            } else {
                isShowingAppOpenAd = false
                setEventOnStartCalled(true)
            }
        } else if (event == Lifecycle.Event.ON_PAUSE) {
            isAnyActivityPaused = 1
            isShowingAppOpenAd = isForceStopAppOpenAds
        } else if (event == Lifecycle.Event.ON_RESUME) {
            if (isAnyActivityPaused == 1) {
                isAnyActivityPaused = 0
            }
            checkShowAdIfAvailable()
        }
    }


    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long = 4L): Boolean {
        val dateDifference: Long = Date().time - mLastLoadAppOpenAdTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    private fun isAdAvailable(): Boolean {
        return mAppOpenAd != null && wasLoadTimeLessThanNHoursAgo()
    }

    private var isLoadingAppOpenAd: Boolean = false
    private fun loadAd(activity: Activity) {
        if (isAdAvailable() || isLoadingAppOpenAd) {
            return
        }
        if (mAdsUnitId.isEmpty() || mAdsUnitId.startsWith(" ") || mAdsUnitId == "none") {
            return
        }
        if (isSkipAppOpenAds) {
            destroyAppOpenAds()
            return
        }
        isLoadingAppOpenAd = true
        val request = AdRequest.Builder().build()
        logE("glAppOpenAds::load:request_new_ads")
        AppOpenAd.load(activity, mAdsUnitId, request, object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                logE("glAppOpenAds::load:adLoaded")
                mAppOpenAd = ad
                isLoadingAppOpenAd = false
                mLastLoadAppOpenAdTime = Date().time
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                logE("glAppOpenAds::load:adFailedToLoad:: ${loadAdError.message}")
                isLoadingAppOpenAd = false
            }
        })
    }

    private fun checkShowAdIfAvailable() {
        Handler(Looper.getMainLooper()).postDelayed({
            showAdIfAvailable()
        }, 100L)
    }

    private var isEventOnStartCalled = false
    private fun setEventOnStartCalled(isCalled: Boolean) {
        this.isEventOnStartCalled = isCalled
    }

    var isShowingAppOpenAd = false
    fun setIsShowingAppOpenAd(isShowing: Boolean) {
        isShowingAppOpenAd = isShowing
    }

    private fun showAdIfAvailable() {
        mActivity?.apply {
            if (!isShowingAppOpenAd && isAnyActivityPaused == 0 && isEventOnStartCalled && isAdAvailable()) {
                mAppOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdImpression() {
                        super.onAdImpression()
                        logE("glAppOpenAds::show:adShowed")
                    }
                    override fun onAdDismissedFullScreenContent() {
                        logE("glAppOpenAds::show:adDismissed")
                        mAppOpenAd = null
                        isShowingAppOpenAd = false
                        loadAd(this@apply)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        logE("glAppOpenAds::show:adFailedToShow:: ${adError.message}")
                        mAppOpenAd = null
                        isShowingAppOpenAd = false
                    }
                }
                isShowingAppOpenAd = true
                mAppOpenAd?.show(this)
            } else {
                loadAd(this)
            }
            setEventOnStartCalled(false)
        }
    }

    private fun registerScreenOnOffReceiver() {
        val screenStateFilter = IntentFilter()
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON)
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF)
        this.mApplication?.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null && intent.action != null) {
                    when (intent.action) {
                        Intent.ACTION_SCREEN_ON -> skipAppOpenAdsOnce()
                        Intent.ACTION_SCREEN_OFF -> skipAppOpenAdsOnce()
                    }
                }
            }
        }, screenStateFilter)
    }


}