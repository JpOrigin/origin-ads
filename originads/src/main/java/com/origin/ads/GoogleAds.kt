package com.origin.ads

import android.app.Application
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun Application.initializeMobileAds() {
    val backgroundScope = CoroutineScope(Dispatchers.IO)
    backgroundScope.launch {
        MobileAds.initialize(this@initializeMobileAds) {}
    }
}
