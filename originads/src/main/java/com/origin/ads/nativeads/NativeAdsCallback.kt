package com.origin.ads.nativeads

import com.google.android.gms.ads.nativead.NativeAd

interface NativeAdsCallback {
    fun onNativeAdLoaded(nativeAd: NativeAd)
    fun onNativeAdFailedToLoad()
}