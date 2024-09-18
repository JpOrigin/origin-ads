package com.origin.ads.interstitialads

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.origin.ads.appopenads.GoogleAppOpenAdManager
import com.origin.ads.isNetworkAvailable
import com.origin.ads.utils.logE
import java.util.Date

class GoogleInterstitialAds {

    companion object {
        var mInterstitialAd: InterstitialAd? = null
        //

        var mCurrentInterCountSession: Int = 0
        var maxInterPerSession: Int = 3

        /***
         * gapBetweenInterAdsSkipPosition is used to skip interstitialAds after showing particular view/clicks by position.
         * */
        var mCurrentInterPosition: Int = 0
        var gapBetweenInterAdsSkipPosition: Int = 2

        /***
         * gapBetweenInterAdsInSecond is used to skip interstitialAds after showing particular view/clicks by time(in seconds).
         * */
        var lastInterAdsCloseTime: Long = 0L
        var gapBetweenInterAdsInSecond: Int = 10
    }

    private var adIsLoading: Boolean = false
    fun load(activity: Activity, adUnitId: String) {
        if (!activity.isNetworkAvailable()) {
            return
        }
        if (adIsLoading || mInterstitialAd != null || mCurrentInterCountSession >= maxInterPerSession) {
            return
        }
        adIsLoading = true
        val adRequest = AdRequest.Builder().build()
        logE("glInterAds::load:request_new_ads")
        InterstitialAd.load(activity, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                logE("glInterAds::load:adFailedToLoad:: ${adError.message}")
                mInterstitialAd = null
                adIsLoading = false
            }

            override fun onAdLoaded(ad: InterstitialAd) {
                logE("glInterAds::load:adLoaded")
                mInterstitialAd = ad
                adIsLoading = false
            }
        })
    }

    private fun wasLastShowTimeGreaterThanNSecondsAgo(): Boolean {
        val dateDifference: Long = Date().time - lastInterAdsCloseTime
        return dateDifference > (gapBetweenInterAdsInSecond * 1000) // 1 second = 1000 millisecond
    }

    private fun isShownCheck(): Boolean {
        if (mCurrentInterCountSession < maxInterPerSession && mCurrentInterPosition < 0 && wasLastShowTimeGreaterThanNSecondsAgo()) {
            mCurrentInterPosition = gapBetweenInterAdsSkipPosition
            return true
        } else {
            return false
        }
    }

    fun show(activity: Activity, adUnitId: String, adsCallback: () -> Unit) {
        if (mCurrentInterPosition >= 0) {
            mCurrentInterPosition -= 1
        }
        if (mInterstitialAd != null && isShownCheck()) {
            mInterstitialAd?.apply {
                this.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        logE("glInterAds::show:ShowedAds")
                        GoogleAppOpenAdManager.pauseAppOpenAds()
                        mCurrentInterCountSession += 1
                    }

                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        logE("glInterAds::show:DismissedAds")

                        mInterstitialAd = null
                        GoogleAppOpenAdManager.resumeAppOpenAds()
                        adsCallback.invoke()
                        load(activity, adUnitId)
                        lastInterAdsCloseTime = Date().time
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        super.onAdFailedToShowFullScreenContent(adError)
                        logE("glInterAds::show:FailedToShow:: ${adError.message}")
                        mInterstitialAd = null
                        adsCallback.invoke()
                    }
                }
                this.show(activity)
                logE("glInterAds::show:callShowAds")
            }
        } else {
            load(activity, adUnitId)
            adsCallback.invoke()
        }
    }
}