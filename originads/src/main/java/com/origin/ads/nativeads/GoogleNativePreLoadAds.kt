package com.origin.ads.nativeads

import android.app.Activity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.origin.ads.appopenads.GoogleAppOpenAdManager
import com.origin.ads.utils.logE

class GoogleNativePreLoadAds(val testInt: Int) { // testInt is used only for log not in used
    private var mGoogleNativePreAds: NativeAd? = null
    private var googleNativePreAdsLoader: AdLoader? = null
    fun load(activity: Activity, adUnitId: String) {
        if (mGoogleNativePreAds != null) {
            return
        }
        logE("glNativePreLoadAds:$testInt:loadAd:load")
        if (googleNativePreAdsLoader == null || !googleNativePreAdsLoader?.isLoading!!) {
            googleNativePreAdsLoader = AdLoader.Builder(activity, adUnitId).forNativeAd { nativeAd: NativeAd ->
                logE("glNativePreLoadAds:$testInt:loadAd:adLoaded")
                mGoogleNativePreAds?.destroy()
                mGoogleNativePreAds = nativeAd
                mNativeAdsCallback?.onNativeAdLoaded(nativeAd)
            }.withAdListener(object : AdListener() {
                override fun onAdImpression() {
                    super.onAdImpression()
                    logE("glNativePreLoadAds:$testInt:loadAd:adShowed")
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    GoogleAppOpenAdManager.skipAppOpenAdsOnce()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    logE("glNativePreLoadAds:$testInt:loadAd:adFailedToLoad: ${adError.message}")
                    mNativeAdsCallback?.onNativeAdFailedToLoad()
                }
            }).withNativeAdOptions(NativeAdOptions.Builder().build()).build()
            googleNativePreAdsLoader?.loadAd(AdRequest.Builder().build())
            logE("glNativePreLoadAds:$testInt:loadAd:Request")
        }
    }

    private var mNativeAdsCallback: NativeAdsCallback? = null
    fun show(activity: Activity, adUnitId: String, adsCallback: NativeAdsCallback) {
        mNativeAdsCallback = null
        if (mGoogleNativePreAds != null) {
            mGoogleNativePreAds?.let {
                logE("glNativePreLoadAds:$testInt:alreadyLoaded")
                adsCallback.onNativeAdLoaded(it)
            }
        } else {
            if (adUnitId.isNotEmpty() && !adUnitId.startsWith(" ") && adUnitId != "none") {
                mNativeAdsCallback = adsCallback
                load(activity, adUnitId)
            } else {
                adsCallback.onNativeAdFailedToLoad()
            }
        }
    }

    fun destroyPreLoadedAds() {
        mGoogleNativePreAds?.destroy()
    }
}