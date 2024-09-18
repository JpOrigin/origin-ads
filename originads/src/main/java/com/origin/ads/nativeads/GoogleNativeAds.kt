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

class GoogleNativeAds {
    private var mGoogleNativeAds: NativeAd? = null
    private var googleNativeAsLoader: AdLoader? = null
    private fun load(activity: Activity, adUnitId: String) {
        if (mGoogleNativeAds != null) {
            return
        }
        if (googleNativeAsLoader == null || !googleNativeAsLoader?.isLoading!!) {
            googleNativeAsLoader = AdLoader.Builder(activity, adUnitId).forNativeAd { nativeAd: NativeAd ->
                if (activity.isDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                    nativeAd.destroy()
                    return@forNativeAd
                }
                mGoogleNativeAds?.destroy()
                mGoogleNativeAds = nativeAd
                mNativeAdsCallback?.onNativeAdLoaded(nativeAd)
                logE("glNativeAds::loadAd:adLoaded")
            }.withAdListener(object : AdListener() {
                override fun onAdClicked() {
                    super.onAdClicked()
                    GoogleAppOpenAdManager.skipAppOpenAdsOnce()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    logE("glNativeAds::loadAd:adFailedToLoad:: ${adError.message}")
                    mNativeAdsCallback?.onNativeAdFailedToLoad()
                }
            }).withNativeAdOptions(NativeAdOptions.Builder().build()).build()
            googleNativeAsLoader?.loadAd(AdRequest.Builder().build())
            logE("glNativeAds::loadAd:Request")
        }
    }

    private var mNativeAdsCallback: NativeAdsCallback? = null
    fun show(activity: Activity, adUnitId: String, adsCallback: NativeAdsCallback) {
        mNativeAdsCallback = null
        if (mGoogleNativeAds != null) {
            mGoogleNativeAds?.let {
                logE("glNativeAds::show:callShowAds")
                adsCallback.onNativeAdLoaded(it)
            }
        } else {
            mNativeAdsCallback = adsCallback
            load(activity, adUnitId)
        }
    }
}