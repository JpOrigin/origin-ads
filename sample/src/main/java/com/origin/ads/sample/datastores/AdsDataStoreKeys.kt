package com.origin.ads.sample.datastores

import androidx.datastore.preferences.core.booleanPreferencesKey

object AdsDataStoreKeys {

    val SKIP_SPLASH_OPEN_ADS_KEY = "isSkipSplashOpenAds"
    val SKIP_SPLASH_OPEN_ADS = booleanPreferencesKey("isSkipSplashOpenAds")

    val SPLASH_OPEN_ADS_KEY = "SplashOpenAds"
    val SPLASH_OPEN_ADS = booleanPreferencesKey("SplashOpenAds")

    val SKIP_APP_OPEN_ADS_KEY = "isSkipAppOpenAds"
    val SKIP_APP_OPEN_ADS = booleanPreferencesKey("isSkipAppOpenAds")

    val APP_OPEN_ADS_KEY = "AppOpenAds"
    val APP_OPEN_ADS = booleanPreferencesKey("AppOpenAds")

    val SKIP_ALL_NATIVE_ADS_KEY = "isSkipAllNativeAds"
    val SKIP_ALL_NATIVE_ADS = booleanPreferencesKey("isSkipAllNativeAds")

    val GAP_BETWEEN_INTER_ADS_IN_SECOND_KEY = "gapBetweenInterAdsInSecond"
    val GAP_BETWEEN_INTER_ADS_IN_SECOND = booleanPreferencesKey("gapBetweenInterAdsInSecond")

    val GAP_BETWEEN_INTER_ADS_SKIP_POSITION_KEY = "gapBetweenInterAdsSkipPosition"
    val GAP_BETWEEN_INTER_ADS_SKIP_POSITION = booleanPreferencesKey("gapBetweenInterAdsSkipPosition")

    val MAX_INTER_PER_SESSION_KEY = "maxInterPerSession"
    val MAX_INTER_PER_SESSION = booleanPreferencesKey("maxInterPerSession")

    val INTERSTITIAL_ADS_KEY = "InterstitialAds"
    val INTERSTITIAL_ADS = booleanPreferencesKey("InterstitialAds")

    val SKIP_ALL_BANNER_ADS_KEY = "isSkipAllBannerAds"
    val SKIP_ALL_BANNER_ADS = booleanPreferencesKey("isSkipAllBannerAds")

    val BANNER_ADS_KEY = "BannerAds"
    val BANNER_ADS = booleanPreferencesKey("BannerAds")

    val CLONE_ADS_URL_KEY = "cloneAdsUrl"
    val CLONE_ADS_URL = booleanPreferencesKey("cloneAdsUrl")

    val CLONE_ADS_AC_NAME_KEY = "cloneAdsAcName"
    val CLONE_ADS_AC_NAME = booleanPreferencesKey("cloneAdsAcName")

    val IS_NATIVE_CLONE_ADS_KEY = "isNativeCloneAds"
    val IS_NATIVE_CLONE_ADS = booleanPreferencesKey("isNativeCloneAds")

    val IS_FORCE_NATIVE_CLONE_ADS_KEY = "isForceNativeCloneAds"
    val IS_FORCE_NATIVE_CLONE_ADS = booleanPreferencesKey("isForceNativeCloneAds")

    val SHOW_NATIVE_SHIMMER_LAYOUT_KEY = "showNativeShimmerLayout"
    val SHOW_NATIVE_SHIMMER_LAYOUT = booleanPreferencesKey("showNativeShimmerLayout")

    val IS_BANNER_CLONE_ADS_KEY = "isBannerCloneAds"
    val IS_BANNER_CLONE_ADS = booleanPreferencesKey("isBannerCloneAds")

    val IS_FORCE_BANNER_CLONE_ADS_KEY = "isForceBannerCloneAds"
    val IS_FORCE_BANNER_CLONE_ADS = booleanPreferencesKey("isForceBannerCloneAds")

    val SHOW_BANNER_SHIMMER_LAYOUT_KEY = "showBannerShimmerLayout"
    val SHOW_BANNER_SHIMMER_LAYOUT = booleanPreferencesKey("showBannerShimmerLayout")

    val LANGUAGE_NATIVE_ADS_KEY = "LanguageNativeAds"
    val LANGUAGE_NATIVE_ADS = booleanPreferencesKey("LanguageNativeAds")

    val HELP_NATIVE_ADS_KEY = "HelpNativeAds"
    val HELP_NATIVE_ADS = booleanPreferencesKey("HelpNativeAds")

    val NATIVE_ADS_KEY = "NativeAds"
    val NATIVE_ADS = booleanPreferencesKey("NativeAds")

}