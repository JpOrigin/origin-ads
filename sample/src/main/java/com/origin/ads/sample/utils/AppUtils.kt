package com.origin.ads.sample.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.widget.Toast
import androidx.core.hardware.display.DisplayManagerCompat
import com.origin.ads.appopenads.GoogleAppOpenAdManager


fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

//fun Context.toast(id: Int, length: Int = Toast.LENGTH_SHORT) {
//    toast(getString(id), length)
//}
fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    try {
        if (isOnMainThread()) {
            doToast(this, msg, length)
        } else {
            Handler(Looper.getMainLooper()).post {
                doToast(this, msg, length)
            }
        }
    } catch (_: Exception) {
    }
}

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()

private fun doToast(context: Context, message: String, length: Int) {
    if (context is Activity) {
        if (!context.isFinishing && !context.isDestroyed) {
            Toast.makeText(context, message, length).show()
        }
    } else {
        Toast.makeText(context, message, length).show()
    }
}

fun Context.showRateUsPlayStore() {
    val str = "android.intent.action.VIEW"
    try {
        this.startActivity(Intent(str, Uri.parse("market://details?id=${this.packageName}")))
    } catch (unused: Exception) {
        this.startActivity(Intent(str, Uri.parse("http://play.google.com/store/apps/details?id=${this.packageName}")))
    }
    // Skip_App_OpenAds_Once
    GoogleAppOpenAdManager.skipAppOpenAdsOnce()
}

var mHeight: Int? = null
fun Activity.getScreenHeight(): Int {
    if (mHeight == null || mHeight == 0) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val defaultDisplay = DisplayManagerCompat.getInstance(this).getDisplay(Display.DEFAULT_DISPLAY)
            val displayContext = createDisplayContext(defaultDisplay!!)
            mHeight = displayContext.resources.displayMetrics.heightPixels
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            this.windowManager.defaultDisplay.getMetrics(displayMetrics)
            mHeight = displayMetrics.heightPixels
        }
    }
    return mHeight ?: 0
}

val Int.dp: Int get() = (toFloat() * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun View.beVisible() {
    this.visibility = View.VISIBLE
}

fun View.beGone() {
    this.visibility = View.GONE
}
