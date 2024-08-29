package com.origin.ads

import android.content.Context
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun Context.initializeMobileAds() {
    val backgroundScope = CoroutineScope(Dispatchers.IO)
    backgroundScope.launch {
        MobileAds.initialize(this@initializeMobileAds) {}
    }
}
