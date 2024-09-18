package com.origin.ads.sample.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.OnHierarchyChangeListener
import android.widget.ImageView
import coil.load
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.origin.ads.appopenads.GoogleAppOpenAdManager
import com.origin.ads.sample.R
import com.origin.ads.sample.clone.getCloneAdsNextCounter
import com.origin.ads.sample.clone.mCloneAdsDataList
import com.origin.ads.sample.databinding.AdCloneBannerBinding
import com.origin.ads.sample.databinding.AdCloneNativeSmallBinding
import com.origin.ads.sample.databinding.AdCloneNativeXlBinding
import com.origin.ads.sample.databinding.AdCloneNativeXxlBinding
import com.origin.ads.sample.databinding.AdUnifiedNativeSmallBinding
import com.origin.ads.sample.databinding.AdUnifiedNativeXlBinding
import com.origin.ads.sample.databinding.AdUnifiedNativeXxlBinding
import com.origin.ads.sample.datastores.mAdsSharedPref

fun Context.getInterAdsUnitId(): String {
    val adUnitId = if (this.mAdsSharedPref.mIsForceShowOfflineAppOpenAds) {
        this.mAdsSharedPref.mOfflineInterstitialAds
    } else {
        this.mAdsSharedPref.mInterstitialAds
    }
    return adUnitId
}

fun populateNativeXLAdView(nativeAd: NativeAd, unifiedAdBinding: AdUnifiedNativeXlBinding) {
    val nativeAdView = unifiedAdBinding.root

    // Set ad assets.
    nativeAdView.iconView = unifiedAdBinding.adAppIcon
    nativeAdView.headlineView = unifiedAdBinding.adHeadline
    nativeAdView.bodyView = unifiedAdBinding.adBody
    nativeAdView.callToActionView = unifiedAdBinding.adCallToAction

    // The headline and media content are guaranteed to be in every UnifiedNativeAd.
    unifiedAdBinding.adHeadline.text = nativeAd.headline

    // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
    // check before trying to display them.
    if (nativeAd.body == null) {
        unifiedAdBinding.adBody.visibility = View.INVISIBLE
    } else {
        unifiedAdBinding.adBody.beVisible()
        unifiedAdBinding.adBody.text = nativeAd.body
    }

    if (nativeAd.callToAction == null) {
        unifiedAdBinding.adCallToAction.visibility = View.INVISIBLE
    } else {
        unifiedAdBinding.adCallToAction.beVisible()
        unifiedAdBinding.adCallToAction.text = nativeAd.callToAction
    }

    if (nativeAd.icon == null) {
        unifiedAdBinding.adAppIcon.beGone()
    } else {
        unifiedAdBinding.adAppIcon.setImageDrawable(nativeAd.icon?.drawable)
        unifiedAdBinding.adAppIcon.beVisible()
    }
    // This method tells the Google Mobile Ads SDK that you have finished populating your
    // native ad view with this native ad.
    nativeAdView.setNativeAd(nativeAd)
}

fun Activity.populateCloneNativeXLAdView(adCloneNativeXlBinding: AdCloneNativeXlBinding) {
    mCloneAdsDataList[getCloneAdsNextCounter()].let { dataItem ->
        adCloneNativeXlBinding.adHeadline.text = dataItem.appName
        adCloneNativeXlBinding.adBody.text = dataItem.appDescription
        adCloneNativeXlBinding.adCallToAction.text = getString(R.string.install)
        adCloneNativeXlBinding.adAppIcon.loadImage(dataItem.appIcon)

        adCloneNativeXlBinding.adAppIcon.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeXLAdView(adCloneNativeXlBinding)
            }
        }
        adCloneNativeXlBinding.adHeadline.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeXLAdView(adCloneNativeXlBinding)
            }
        }
        adCloneNativeXlBinding.adBody.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeXLAdView(adCloneNativeXlBinding)
            }
        }
        adCloneNativeXlBinding.adCallToAction.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeXLAdView(adCloneNativeXlBinding)
            }
        }
    }
}


fun populateNativeSmallAdView(nativeAd: NativeAd, unifiedAdBinding: AdUnifiedNativeSmallBinding) {
    val nativeAdView = unifiedAdBinding.root

    // Set ad assets.
    nativeAdView.iconView = unifiedAdBinding.adAppIcon
    nativeAdView.headlineView = unifiedAdBinding.adHeadline
    nativeAdView.bodyView = unifiedAdBinding.adBody
    nativeAdView.callToActionView = unifiedAdBinding.adCallToAction

    // The headline and media content are guaranteed to be in every UnifiedNativeAd.
    unifiedAdBinding.adHeadline.text = nativeAd.headline

    // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
    // check before trying to display them.
    if (nativeAd.body == null) {
        unifiedAdBinding.adBody.visibility = View.INVISIBLE
    } else {
        unifiedAdBinding.adBody.beVisible()
        unifiedAdBinding.adBody.text = nativeAd.body
    }

    if (nativeAd.callToAction == null) {
        unifiedAdBinding.adCallToAction.visibility = View.INVISIBLE
    } else {
        unifiedAdBinding.adCallToAction.beVisible()
        unifiedAdBinding.adCallToAction.text = nativeAd.callToAction
    }

    if (nativeAd.icon == null) {
        unifiedAdBinding.adAppIcon.beGone()
    } else {
        unifiedAdBinding.adAppIcon.setImageDrawable(nativeAd.icon?.drawable)
        unifiedAdBinding.adAppIcon.beVisible()
    }
    // This method tells the Google Mobile Ads SDK that you have finished populating your
    // native ad view with this native ad.
    nativeAdView.setNativeAd(nativeAd)
}

fun Activity.populateCloneNativeSmallAdView(adCloneNativeSmallBinding: AdCloneNativeSmallBinding) {
    mCloneAdsDataList[getCloneAdsNextCounter()].let { dataItem ->
        adCloneNativeSmallBinding.adHeadline.text = dataItem.appName
        adCloneNativeSmallBinding.adBody.text = dataItem.appDescription
        adCloneNativeSmallBinding.adCallToAction.text = getString(R.string.install)
        adCloneNativeSmallBinding.adAppIcon.loadImage(dataItem.appIcon)

        adCloneNativeSmallBinding.adAppIcon.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeSmallAdView(adCloneNativeSmallBinding)
            }
        }
        adCloneNativeSmallBinding.adHeadline.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeSmallAdView(adCloneNativeSmallBinding)
            }
        }
        adCloneNativeSmallBinding.adBody.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeSmallAdView(adCloneNativeSmallBinding)
            }
        }
        adCloneNativeSmallBinding.adCallToAction.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeSmallAdView(adCloneNativeSmallBinding)
            }
        }
    }
}

fun Activity.populateNativeXXLAdView(nativeAd: NativeAd, unifiedAdBinding: AdUnifiedNativeXxlBinding) {
    val nativeAdView = unifiedAdBinding.root

    // Set ad assets.
    nativeAdView.iconView = unifiedAdBinding.adAppIcon
    nativeAdView.headlineView = unifiedAdBinding.adHeadline
    nativeAdView.bodyView = unifiedAdBinding.adBody
    nativeAdView.callToActionView = unifiedAdBinding.adCallToAction
    // Set the media view.
    val mScHeight = if (this.getScreenHeight() / 5 > 300) {
        (this.getScreenHeight() / 5)
    } else {
        300
    }
    val mediaView: MediaView = unifiedAdBinding.adMedia
    val params = mediaView.layoutParams
    params.height = mScHeight
    params.width = ViewGroup.LayoutParams.MATCH_PARENT
    mediaView.layoutParams = params
    mediaView.setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
        override fun onChildViewAdded(parent: View, child: View) {
            if (child is ImageView) {
                child.adjustViewBounds = true
            }
        }

        override fun onChildViewRemoved(parent: View, child: View) {
        }
    })
    nativeAdView.mediaView = mediaView

    // The headline and media content are guaranteed to be in every UnifiedNativeAd.
    unifiedAdBinding.adHeadline.text = nativeAd.headline
    nativeAd.mediaContent?.let { unifiedAdBinding.adMedia.mediaContent = it }

    // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
    // check before trying to display them.
    if (nativeAd.body == null) {
        unifiedAdBinding.adBody.visibility = View.INVISIBLE
    } else {
        unifiedAdBinding.adBody.beVisible()
        unifiedAdBinding.adBody.text = nativeAd.body
    }

    if (nativeAd.callToAction == null) {
        unifiedAdBinding.adCallToAction.visibility = View.INVISIBLE
    } else {
        unifiedAdBinding.adCallToAction.beVisible()
        unifiedAdBinding.adCallToAction.text = nativeAd.callToAction
    }

    if (nativeAd.icon == null) {
        unifiedAdBinding.adAppIcon.beGone()
    } else {
        unifiedAdBinding.adAppIcon.setImageDrawable(nativeAd.icon?.drawable)
        unifiedAdBinding.adAppIcon.beVisible()
    }
    // This method tells the Google Mobile Ads SDK that you have finished populating your
    // native ad view with this native ad.
    nativeAdView.setNativeAd(nativeAd)
}

fun Activity.populateCloneNativeXXLAdView(adCloneNativeXxlBinding: AdCloneNativeXxlBinding) {
    mCloneAdsDataList[getCloneAdsNextCounter()].let { dataItem ->
        val mScHeight = if (this.getScreenHeight() / 5 > 300) {
            (this.getScreenHeight() / 5)
        } else {
            300
        }
        val mediaView: ImageView = adCloneNativeXxlBinding.adMedia
        val params = mediaView.layoutParams
        params.height = mScHeight
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        mediaView.layoutParams = params
        mediaView.loadImage(dataItem.appBanner)

        adCloneNativeXxlBinding.adHeadline.text = dataItem.appName
        adCloneNativeXxlBinding.adBody.text = dataItem.appDescription
        adCloneNativeXxlBinding.adCallToAction.text = getString(R.string.install)
        adCloneNativeXxlBinding.adAppIcon.loadImage(dataItem.appIcon)

        adCloneNativeXxlBinding.adAppIcon.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeXXLAdView(adCloneNativeXxlBinding)
            }
        }
        adCloneNativeXxlBinding.adMedia.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeXXLAdView(adCloneNativeXxlBinding)
            }
        }
        adCloneNativeXxlBinding.adHeadline.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeXXLAdView(adCloneNativeXxlBinding)
            }
        }
        adCloneNativeXxlBinding.adBody.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeXXLAdView(adCloneNativeXxlBinding)
            }
        }
        adCloneNativeXxlBinding.adCallToAction.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneNativeXXLAdView(adCloneNativeXxlBinding)
            }
        }
    }
}

fun Activity.populateCloneBannerAdView(adCloneBannerBinding: AdCloneBannerBinding) {
    mCloneAdsDataList[getCloneAdsNextCounter()].let { dataItem ->
        adCloneBannerBinding.adHeadline.text = dataItem.appName
        adCloneBannerBinding.adBody.text = dataItem.appDescription
        adCloneBannerBinding.adCallToAction.text = getString(R.string.install)
        adCloneBannerBinding.adAppIcon.loadImage(dataItem.appIcon)
        adCloneBannerBinding.adAppIcon.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneBannerAdView(adCloneBannerBinding)
            }
        }
        adCloneBannerBinding.adHeadline.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneBannerAdView(adCloneBannerBinding)
            }
        }
        adCloneBannerBinding.adBody.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneBannerAdView(adCloneBannerBinding)
            }
        }
        adCloneBannerBinding.adCallToAction.setOnClickListener {
            this.openUrl(dataItem.appLink) {
                populateCloneBannerAdView(adCloneBannerBinding)
            }
        }
    }
}

fun Activity.getAdSize(view: View): AdSize {
    val adWidth: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = this.windowManager.currentWindowMetrics
        val bounds = windowMetrics.bounds
        var adWidthPixels: Float = view.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = bounds.width().toFloat()
        }
        (adWidthPixels / this.resources.displayMetrics.density).toInt()
    } else {
        @Suppress("DEPRECATION")
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        display.getMetrics(outMetrics)
        val density = outMetrics.density
        var adWidthPixels: Float = view.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }
        (adWidthPixels / density).toInt()
    }
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
}

private fun ImageView.loadImage(iconUrl: String?) {
    iconUrl?.let {
        this.load(it)
    }
}

private fun Activity.openUrl(appUrl: String?, callback: () -> Unit) {
    appUrl?.let {
        try {
            GoogleAppOpenAdManager.skipAppOpenAdsOnce()
            Intent(Intent.ACTION_VIEW, Uri.parse(it)).apply {
                this@openUrl.startActivity(this)
            }
            Handler(Looper.getMainLooper()).postDelayed({ callback.invoke() }, 500L)
        } catch (_: Exception) {
        }
    }
}




