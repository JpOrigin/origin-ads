package com.origin.ads.bannerads

import com.google.android.gms.ads.AdView

interface BannerAdsCallback {
    fun onBannerAdLoaded(adView: AdView)
    fun onBannerAdFailedToLoad()
}