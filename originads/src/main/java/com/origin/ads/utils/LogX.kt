/*
 * 4/2/24, 6:07 PM
 */

package com.origin.ads.utils

import android.annotation.SuppressLint
import timber.log.Timber


const val mDefaultTagString = " originAds "

fun logE(mMessageString: String?) {
    Timber.tag(mDefaultTagString).e(mMessageString)
}

fun logE(tagString: String?, mMessageString: String?) {
    if (tagString.isNullOrEmpty()) {
        logE(mMessageString)
    } else {
        Timber.tag(tagString).e(mMessageString)
    }
}

fun initLogX(isDebug: Boolean = true) {
    if (isDebug) {
        Timber.plant(Timber.DebugTree())
    } else {
        Timber.plant(CrashReportingTree())
    }
}

class CrashReportingTree : Timber.Tree() {
    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == android.util.Log.VERBOSE || priority == android.util.Log.DEBUG) {
            return
        }
        val mTagString = tag ?: mDefaultTagString
        try {
            android.util.Log.e(mTagString, message)
        } catch (_: Exception) {
        }
    }
}