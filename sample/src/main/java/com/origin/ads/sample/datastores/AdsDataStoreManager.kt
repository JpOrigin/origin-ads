package com.origin.ads.sample.datastores
//
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
//
class AdsDataStoreManager {
    companion object {
        val ADS_PREFERENCES_NAME = "ads_preferences"
//        val mDataStoreManager = AdsDataStoreManager()
    }
//
//    var context: Context? = null
//    var mIsForceShowOfflineSplashOpenAds = false
//    val Context.adsDataStore: DataStore<Preferences> by preferencesDataStore(name = ADS_PREFERENCES_NAME)
//
//    fun initialize(context: Context, onLoad: () -> Unit) {
//        this.context = context
//        GlobalScope.launch(Dispatchers.Default) {
//            mIsForceShowOfflineSplashOpenAds = getBooleanFromAdsDataStore(AdsDataStoreKeys.FORCE_SHOW_OFFLINE_SPLASH_OPEN_ADS, false)
//            onLoad()
//        }
//    }
}
//
