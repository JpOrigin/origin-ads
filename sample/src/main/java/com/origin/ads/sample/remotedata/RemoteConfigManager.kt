package com.origin.ads.sample.remotedata

import android.content.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.origin.ads.sample.datastores.AdsDataStoreKeys.APP_OPEN_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.BANNER_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.CLONE_ADS_AC_NAME_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.CLONE_ADS_URL_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.FORCE_SHOW_OFFLINE_ALL_BANNER_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.FORCE_SHOW_OFFLINE_ALL_NATIVE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.FORCE_SHOW_OFFLINE_APP_OPEN_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.FORCE_SHOW_OFFLINE_SPLASH_OPEN_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.GAP_BETWEEN_INTER_ADS_IN_SECOND_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.GAP_BETWEEN_INTER_ADS_SKIP_POSITION_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.INTERSTITIAL_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.IS_BANNER_CLONE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.IS_FORCE_BANNER_CLONE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.IS_FORCE_NATIVE_CLONE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.IS_NATIVE_CLONE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.MAX_INTER_PER_SESSION_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.NATIVE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SHOW_BANNER_SHIMMER_LAYOUT_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SHOW_NATIVE_SHIMMER_LAYOUT_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SKIP_ALL_BANNER_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SKIP_ALL_NATIVE_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SKIP_APP_OPEN_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SKIP_SPLASH_OPEN_ADS_KEY
import com.origin.ads.sample.datastores.AdsDataStoreKeys.SPLASH_OPEN_ADS_KEY
import com.origin.ads.sample.datastores.mAdsSharedPref
import com.origin.ads.sample.utils.isNetworkAvailable
import com.origin.ads.sample.utils.toast
import com.origin.ads.utils.logE
import java.util.concurrent.Executors


var isConfigDataFetched: Boolean = false
fun Context.loadConfigData(notifyRemoteConfigData: () -> Unit) {
    if (this.isNetworkAvailable()) {
        Executors.newSingleThreadExecutor().execute(ConfigRunnable(this, notifyRemoteConfigData))
    }
}

private class ConfigRunnable(val mContext: Context, val notifyRemoteConfigData: () -> Unit) : Runnable {
    override fun run() {
        if (!isConfigDataFetched && !mContext.mAdsSharedPref.mIsForceSkipConfig) {
            Firebase.remoteConfig.apply {
//            var mFetchIntervalInSeconds :Long
                // if debug
                val mFetchIntervalInSeconds = 5L
//            else
//            mFetchIntervalInSeconds = 3600L
                val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = mFetchIntervalInSeconds }
                this.setConfigSettingsAsync(configSettings)
                logE("RConfig::request")
                this.fetchAndActivate().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        logE("RConfig:addOnCompleteListener:updated: $updated")
                        // SplashOpenAdsGroup
                        mContext.mAdsSharedPref.mIsForceShowOfflineSplashOpenAds = this.getBoolean(FORCE_SHOW_OFFLINE_SPLASH_OPEN_ADS_KEY)
                        mContext.mAdsSharedPref.mIsSkipSplashOpenAds = this.getBoolean(SKIP_SPLASH_OPEN_ADS_KEY)
                        mContext.mAdsSharedPref.mSplashOpenAds = this.getString(SPLASH_OPEN_ADS_KEY)
                        // SplashOpenAdsGroup

                        // AppOpenAdsGroup
                        mContext.mAdsSharedPref.mIsForceShowOfflineAppOpenAds = this.getBoolean(FORCE_SHOW_OFFLINE_APP_OPEN_ADS_KEY)
                        mContext.mAdsSharedPref.mIsSkipAppOpenAds = this.getBoolean(SKIP_APP_OPEN_ADS_KEY)
                        mContext.mAdsSharedPref.mAppOpenAds = this.getString(APP_OPEN_ADS_KEY)
                        // AppOpenAdsGroup

                        // InterstitialAdsGroup
                        mContext.mAdsSharedPref.mMaxInterPerSession = this.getLong(MAX_INTER_PER_SESSION_KEY).toInt()
                        mContext.mAdsSharedPref.mGapBetweenInterAdsSkipPosition = this.getLong(GAP_BETWEEN_INTER_ADS_SKIP_POSITION_KEY).toInt()
                        mContext.mAdsSharedPref.mGapBetweenInterAdsInSecond = this.getLong(GAP_BETWEEN_INTER_ADS_IN_SECOND_KEY).toInt()
                        mContext.mAdsSharedPref.mInterstitialAds = this.getString(INTERSTITIAL_ADS_KEY)
                        // InterstitialAdsGroup

                        /***
                         * AllNativeAdsGroup
                         * */
                        mContext.mAdsSharedPref.mIsForceShowOfflineAllNativeAds = this.getBoolean(FORCE_SHOW_OFFLINE_ALL_NATIVE_ADS_KEY)
                        mContext.mAdsSharedPref.mIsSkipAllNativeAds = this.getBoolean(SKIP_ALL_NATIVE_ADS_KEY)
                        mContext.mAdsSharedPref.mShowNativeShimmerLayout = this.getBoolean(SHOW_NATIVE_SHIMMER_LAYOUT_KEY)
                        mContext.mAdsSharedPref.mIsNativeCloneAds = this.getBoolean(IS_NATIVE_CLONE_ADS_KEY)
                        mContext.mAdsSharedPref.mIsForceNativeCloneAds = this.getBoolean(IS_FORCE_NATIVE_CLONE_ADS_KEY)
                        mContext.mAdsSharedPref.mNativeAds = this.getString(NATIVE_ADS_KEY)

                        /***
                         * AllBannerAdsGroup
                         * */
                        mContext.mAdsSharedPref.mIsForceShowOfflineAllBannerAds = this.getBoolean(FORCE_SHOW_OFFLINE_ALL_BANNER_ADS_KEY)
                        mContext.mAdsSharedPref.mIsSkipAllBannerAds = this.getBoolean(SKIP_ALL_BANNER_ADS_KEY)
                        mContext.mAdsSharedPref.mShowBannerShimmerLayout = this.getBoolean(SHOW_BANNER_SHIMMER_LAYOUT_KEY)
                        mContext.mAdsSharedPref.mIsBannerCloneAds = this.getBoolean(IS_BANNER_CLONE_ADS_KEY)
                        mContext.mAdsSharedPref.mIsForceBannerCloneAds = this.getBoolean(IS_FORCE_BANNER_CLONE_ADS_KEY)
                        mContext.mAdsSharedPref.mBannerAds = this.getString(BANNER_ADS_KEY)

                        /***
                         * CloneAdsGroup
                         * */
                        mContext.mAdsSharedPref.mCloneAdsUrl = this.getString(CLONE_ADS_URL_KEY)
                        mContext.mAdsSharedPref.mCloneAdsAcName = this.getString(CLONE_ADS_AC_NAME_KEY)


                        // End of task
                        isConfigDataFetched = true
                        notifyRemoteConfigData.invoke()

                        logE("RConfig::mIsForceShowOfflineSplashOpenAds:::   ${mContext.mAdsSharedPref.mIsForceShowOfflineSplashOpenAds}")
                        logE("RConfig::mIsSkipSplashOpenAds:::   ${mContext.mAdsSharedPref.mIsSkipSplashOpenAds}")
                        logE("RConfig::mSplashOpenAds:::   ${mContext.mAdsSharedPref.mSplashOpenAds}")
                        logE("RConfig::mIsForceShowOfflineAppOpenAds:::   ${mContext.mAdsSharedPref.mIsForceShowOfflineAppOpenAds}")
                        logE("RConfig::mIsSkipAppOpenAds:::   ${mContext.mAdsSharedPref.mIsSkipAppOpenAds}")
                        logE("RConfig::mAppOpenAds:::   ${mContext.mAdsSharedPref.mAppOpenAds}")
                        logE("RConfig::mMaxInterPerSession:::   ${mContext.mAdsSharedPref.mMaxInterPerSession}")
                        logE("RConfig::mGapBetweenInterAdsSkipPosition:::   ${mContext.mAdsSharedPref.mGapBetweenInterAdsSkipPosition}")
                        logE("RConfig::mGapBetweenInterAdsInSecond:::   ${mContext.mAdsSharedPref.mGapBetweenInterAdsInSecond}")
                        logE("RConfig::mInterstitialAds:::   ${mContext.mAdsSharedPref.mInterstitialAds}")
                        logE("RConfig::mIsForceShowOfflineAllNativeAds:::   ${mContext.mAdsSharedPref.mIsForceShowOfflineAllNativeAds}")
                        logE("RConfig::mIsSkipAllNativeAds:::   ${mContext.mAdsSharedPref.mIsSkipAllNativeAds}")
                        logE("RConfig::mShowNativeShimmerLayout:::   ${mContext.mAdsSharedPref.mShowNativeShimmerLayout}")
                        logE("RConfig::mIsNativeCloneAds:::   ${mContext.mAdsSharedPref.mIsNativeCloneAds}")
                        logE("RConfig::mIsForceNativeCloneAds:::   ${mContext.mAdsSharedPref.mIsForceNativeCloneAds}")
                        logE("RConfig::mNativeAds:::   ${mContext.mAdsSharedPref.mNativeAds}")
                        logE("RConfig::mIsForceShowOfflineAllBannerAds:::   ${mContext.mAdsSharedPref.mIsForceShowOfflineAllBannerAds}")
                        logE("RConfig::mIsSkipAllBannerAds:::   ${mContext.mAdsSharedPref.mIsSkipAllBannerAds}")
                        logE("RConfig::mShowBannerShimmerLayout:::   ${mContext.mAdsSharedPref.mShowBannerShimmerLayout}")
                        logE("RConfig::mIsBannerCloneAds:::   ${mContext.mAdsSharedPref.mIsBannerCloneAds}")
                        logE("RConfig::mIsForceBannerCloneAds:::   ${mContext.mAdsSharedPref.mIsForceBannerCloneAds}")
                        logE("RConfig::mBannerAds:::   ${mContext.mAdsSharedPref.mBannerAds}")
                        logE("RConfig::mCloneAdsUrl:::   ${mContext.mAdsSharedPref.mCloneAdsUrl}")
                        logE("RConfig::mCloneAdsAcName:::   ${mContext.mAdsSharedPref.mCloneAdsAcName}")
                    } else {
                        mContext.toast("No Data Found!")
                    }
                }
            }
        } else {
            notifyRemoteConfigData.invoke()
        }
    }
}
