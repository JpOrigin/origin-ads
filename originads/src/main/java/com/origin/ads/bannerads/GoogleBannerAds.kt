package com.origin.ads.bannerads

import android.app.Activity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.origin.ads.appopenads.GoogleAppOpenAdManager
import com.origin.ads.utils.logE

class GoogleBannerAds {
    private var mGoogleBannerAds: AdView? = null
    fun show(activity: Activity, adUnitId: String, adSize: AdSize, bannerAdsCallback: BannerAdsCallback) {
        if (adUnitId.isNotEmpty() && !adUnitId.startsWith(" ") && adUnitId != "none") {
            AdView(activity).apply {
                this.adUnitId = adUnitId
                this.setAdSize(adSize)
                mGoogleBannerAds = this
                val build = AdRequest.Builder().build()
                this.loadAd(build)
                logE("glBannerAds::load:request_new_ads")
                this.adListener = object : AdListener() {
                    override fun onAdClicked() {
                        super.onAdClicked()
                        GoogleAppOpenAdManager.skipAppOpenAdsOnce()
                        logE("glBannerAds::load:adClicked")
                    }

                    override fun onAdFailedToLoad(ad: LoadAdError) {
                        super.onAdFailedToLoad(ad)
                        bannerAdsCallback.onBannerAdFailedToLoad()
                        logE("glBannerAds::load:adFailedToLoad:: ${ad.message}")
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        logE("glBannerAds::load:adLoaded")
                        bannerAdsCallback.onBannerAdLoaded(this@apply)
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        logE("glBannerAds::load:adShowed")
                    }
                }
            }
        } else {
            bannerAdsCallback.onBannerAdFailedToLoad()
        }
    }
    fun pauseLoadedAds() {
        mGoogleBannerAds?.pause()
    }

    fun resumeLoadedAds() {
        mGoogleBannerAds?.resume()
    }

    fun destroyLoadedAds() {
        mGoogleBannerAds?.destroy()
    }
}