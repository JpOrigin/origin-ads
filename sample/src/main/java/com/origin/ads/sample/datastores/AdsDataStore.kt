package com.origin.ads.sample.datastores

//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.edit
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.flow.firstOrNull
//import kotlinx.coroutines.launch

//suspend fun AdsDataStoreManager.getBooleanFromAdsDataStore(key: Preferences.Key<Boolean>, defaultValue: Boolean): Boolean {
//    val context = this.context ?: return defaultValue
//    return context.adsDataStore.data.firstOrNull()?.get(key) ?: defaultValue
//}
//
//suspend fun AdsDataStoreManager.setBooleanToAdsDataStore(key: Preferences.Key<Boolean>, value: Boolean) {
//    val context = this.context ?: return
//    context.adsDataStore.edit { usrData ->
//        usrData[key] = value
//    }
//}
//fun AdsDataStoreManager.updateBooleanToAdsDataStore(key: Preferences.Key<Boolean>,value: Boolean) {
//    GlobalScope.launch(Dispatchers.Default) {
//        setBooleanToAdsDataStore(key, value)
//    }
//}
