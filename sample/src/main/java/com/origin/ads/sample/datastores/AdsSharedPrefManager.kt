package com.origin.ads.sample.datastores

import android.content.Context
import com.origin.ads.sample.datastores.AdsDataStoreKeys.APP_OPEN_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.BANNER_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.CLONE_ADS_AC_NAME_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.CLONE_ADS_URL_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.HELP_NATIVE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.INTERSTITIAL_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.IS_BANNER_CLONE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.IS_FORCE_BANNER_CLONE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.IS_FORCE_NATIVE_CLONE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.IS_NATIVE_CLONE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.LANGUAGE_NATIVE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.NATIVE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SHOW_BANNER_SHIMMER_LAYOUT_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SKIP_ALL_BANNER_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SKIP_ALL_NATIVE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SKIP_APP_OPEN_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SKIP_SPLASH_OPEN_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SPLASH_OPEN_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreManager.Companion.ADS_PREFERENCES_NAME

val Context.mAdsSharedPref: AdsSharedPrefManager get() = AdsSharedPrefManager.newInstance(applicationContext)

class AdsSharedPrefManager(mContext: Context) {
    private val mSharedPref = mContext.getSharedPreferences(ADS_PREFERENCES_NAME, Context.MODE_PRIVATE)

    companion object {
        fun newInstance(context: Context) = AdsSharedPrefManager(context)
    }

    /***
     * SplashOpenAdsGroup */
    var mIsSkipSplashOpenAds: Boolean
        get() = mSharedPref!!.getBoolean(SKIP_SPLASH_OPEN_ADS_KEY, false)
        set(booleanValue) = mSharedPref!!.edit().putBoolean(SKIP_SPLASH_OPEN_ADS_KEY, booleanValue).apply()

    var mSplashOpenAds: String
        get() = mSharedPref!!.getString(SPLASH_OPEN_ADS_KEY, splashOpenAds) ?: splashOpenAds
        set(stringValue) = mSharedPref!!.edit().putString(SPLASH_OPEN_ADS_KEY, stringValue).apply()

    /***
     * AppOpenAdsGroup */
    var mIsSkipAppOpenAds: Boolean
        get() = mSharedPref!!.getBoolean(SKIP_APP_OPEN_ADS_KEY, false)
        set(booleanValue) = mSharedPref!!.edit().putBoolean(SKIP_APP_OPEN_ADS_KEY, booleanValue).apply()

    var mAppOpenAds: String
        get() = mSharedPref!!.getString(APP_OPEN_ADS_KEY, appOpenAds) ?: appOpenAds
        set(stringValue) = mSharedPref!!.edit().putString(APP_OPEN_ADS_KEY, stringValue).apply()

    /***
     * InterstitialAdsGroup */
    var mMaxInterPerSession: Int
        get() = mSharedPref!!.getInt(AdsDataStoreKeys.MAX_INTER_PER_SESSION_KEY, maxInterPerSession)
        set(intValue) = mSharedPref!!.edit().putInt(AdsDataStoreKeys.MAX_INTER_PER_SESSION_KEY, intValue).apply()
    var mGapBetweenInterAdsSkipPosition: Int
        get() = mSharedPref!!.getInt(AdsDataStoreKeys.GAP_BETWEEN_INTER_ADS_SKIP_POSITION_KEY, gapBetweenInterAdsSkipPosition)
        set(intValue) = mSharedPref!!.edit().putInt(AdsDataStoreKeys.GAP_BETWEEN_INTER_ADS_SKIP_POSITION_KEY, intValue).apply()
    var mGapBetweenInterAdsInSecond: Int
        get() = mSharedPref!!.getInt(AdsDataStoreKeys.GAP_BETWEEN_INTER_ADS_IN_SECOND_KEY, gapBetweenInterAdsInSecond)
        set(intValue) = mSharedPref!!.edit().putInt(AdsDataStoreKeys.GAP_BETWEEN_INTER_ADS_IN_SECOND_KEY, intValue).apply()
    var mInterstitialAds: String
        get() = mSharedPref!!.getString(INTERSTITIAL_ADS_KEY, interstitialAds) ?: interstitialAds
        set(stringValue) = mSharedPref!!.edit().putString(INTERSTITIAL_ADS_KEY, stringValue).apply()


    /***
     * AllNativeAdsGroup */
    var mIsSkipAllNativeAds: Boolean
        get() = mSharedPref!!.getBoolean(SKIP_ALL_NATIVE_ADS_KEY, false)
        set(booleanValue) = mSharedPref!!.edit().putBoolean(SKIP_ALL_NATIVE_ADS_KEY, booleanValue).apply()
    var mIsNativeCloneAds: Boolean
        get() = mSharedPref!!.getBoolean(IS_NATIVE_CLONE_ADS_KEY, false)
        set(booleanValue) = mSharedPref!!.edit().putBoolean(IS_NATIVE_CLONE_ADS_KEY, booleanValue).apply()
    var mIsForceNativeCloneAds: Boolean
        get() = mSharedPref!!.getBoolean(IS_FORCE_NATIVE_CLONE_ADS_KEY, false)
        set(booleanValue) = mSharedPref!!.edit().putBoolean(IS_FORCE_NATIVE_CLONE_ADS_KEY, booleanValue).apply()
    var mShowNativeShimmerLayout: Boolean
        get() = mSharedPref!!.getBoolean(AdsDataStoreKeys.SHOW_NATIVE_SHIMMER_LAYOUT_KEY, false)
        set(booleanValue) = mSharedPref!!.edit().putBoolean(AdsDataStoreKeys.SHOW_NATIVE_SHIMMER_LAYOUT_KEY, booleanValue).apply()

    /***
     * NativeAdsGroup */
    var mLanguageNativeAds: String
        get() = mSharedPref!!.getString(LANGUAGE_NATIVE_ADS_KEY, languageNativeAds) ?: languageNativeAds
        set(stringValue) = mSharedPref!!.edit().putString(LANGUAGE_NATIVE_ADS_KEY, stringValue).apply()
    var mHelpNativeAds: String
        get() = mSharedPref!!.getString(HELP_NATIVE_ADS_KEY, helpNativeAds) ?: helpNativeAds
        set(stringValue) = mSharedPref!!.edit().putString(HELP_NATIVE_ADS_KEY, stringValue).apply()
    var mNativeAds: String
        get() = mSharedPref!!.getString(NATIVE_ADS_KEY, nativeAds) ?: nativeAds
        set(stringValue) = mSharedPref!!.edit().putString(NATIVE_ADS_KEY, stringValue).apply()


    /***
     * AllBannerAdsGroup */
    var mIsSkipAllBannerAds: Boolean
        get() = mSharedPref!!.getBoolean(SKIP_ALL_BANNER_ADS_KEY, false)
        set(booleanValue) = mSharedPref!!.edit().putBoolean(SKIP_ALL_BANNER_ADS_KEY, booleanValue).apply()
    var mIsBannerCloneAds: Boolean
        get() = mSharedPref!!.getBoolean(IS_BANNER_CLONE_ADS_KEY, false)
        set(booleanValue) = mSharedPref!!.edit().putBoolean(IS_BANNER_CLONE_ADS_KEY, booleanValue).apply()
    var mIsForceBannerCloneAds: Boolean
        get() = mSharedPref!!.getBoolean(IS_FORCE_BANNER_CLONE_ADS_KEY, false)
        set(booleanValue) = mSharedPref!!.edit().putBoolean(IS_FORCE_BANNER_CLONE_ADS_KEY, booleanValue).apply()
    var mShowBannerShimmerLayout: Boolean
        get() = mSharedPref!!.getBoolean(SHOW_BANNER_SHIMMER_LAYOUT_KEY, false)
        set(booleanValue) = mSharedPref!!.edit().putBoolean(SHOW_BANNER_SHIMMER_LAYOUT_KEY, booleanValue).apply()

    /***
     * BannerAdsGroup */
    var mBannerAds: String
        get() = mSharedPref!!.getString(BANNER_ADS_KEY, bannerAds) ?: bannerAds
        set(stringValue) = mSharedPref!!.edit().putString(BANNER_ADS_KEY, stringValue).apply()


    /***
     * CloneAdsGroup */
    var mCloneAdsUrl: String
        get() = mSharedPref!!.getString(CLONE_ADS_URL_KEY, "") ?: ""
        set(stringValue) = mSharedPref!!.edit().putString(CLONE_ADS_URL_KEY, stringValue).apply()
    var mCloneAdsAcName: String
        get() = mSharedPref!!.getString(CLONE_ADS_AC_NAME_KEY, "") ?: ""
        set(stringValue) = mSharedPref!!.edit().putString(CLONE_ADS_AC_NAME_KEY, stringValue).apply()


    //
    var splashIntervalInSeconds: Int
        get() = mSharedPref!!.getInt("splashInterval", 10)
        set(intValue) = mSharedPref!!.edit().putInt("splashInterval", intValue).apply()

    var mIsForceSkipConfig: Boolean
        get() = mSharedPref!!.getBoolean("forceSkipConfig", false)
        set(booleanValue) = mSharedPref!!.edit().putBoolean("forceSkipConfig", booleanValue).apply()

    var mAppLanguage: String
        get() = mSharedPref!!.getString("appLanguage", "") ?: ""
        set(stringValue) = mSharedPref!!.edit().putString("appLanguage", stringValue).apply()

    var mIsShowedHelpScreen: Boolean
        get() = mSharedPref!!.getBoolean("isShowedHelpScreen", false)
        set(booleanValue) = mSharedPref!!.edit().putBoolean("isShowedHelpScreen", booleanValue).apply()
}