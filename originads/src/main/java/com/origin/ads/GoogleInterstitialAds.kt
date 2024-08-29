package com.origin.ads

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


var mInterstitialAd: InterstitialAd? = null

fun Activity.showGoogleInterstitialAd() {
    if (mInterstitialAd != null) {
        showInterstitialWithCallback {}
    } else {
        loadGoogleInterstitialAd()
    }
}

fun Activity.showGoogleInterstitialAd(callNextActivity: () -> Unit) {
    if (mInterstitialAd != null) {
        showInterstitialWithCallback(callNextActivity)
    } else {
        loadGoogleInterstitialAd()
    }
}

var isInterstitialRequestAlreadyCalled = false
fun Activity.loadGoogleInterstitialAd() {
    if (!isInterstitialRequestAlreadyCalled) {
        val adRequest = AdRequest.Builder().build()
        isInterstitialRequestAlreadyCalled = true
        InterstitialAd.load(this, googleInterstitialId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                isInterstitialRequestAlreadyCalled = false
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                isInterstitialRequestAlreadyCalled = false
                mInterstitialAd = interstitialAd
            }
        })
    }
}

fun Activity.showInterstitialWithCallback(callNextActivity: () -> Unit) {
    mInterstitialAd?.apply {
        fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                mInterstitialAd = null
                callNextActivity.invoke()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                super.onAdFailedToShowFullScreenContent(adError)
                mInterstitialAd = null
                callNextActivity.invoke()
            }
        }
        show(this@showInterstitialWithCallback)
    }
}
