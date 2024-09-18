package com.origin.ads.sample.clone

import android.app.Activity
import com.origin.ads.sample.datastores.mAdsSharedPref
import com.origin.ads.sample.utils.isNetworkAvailable
import com.origin.ads.utils.logE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

fun Activity.loadCloneAdsData() {
    if (this.isNetworkAvailable()) {
        Executors.newSingleThreadExecutor().execute(CloneAdsRunnable(this))
    }
}

var mCloneAdsCurrentCount = -1
fun getCloneAdsNextCounter(): Int {
    mCloneAdsCurrentCount = if (mCloneAdsDataList.isNotEmpty() && mCloneAdsCurrentCount < mCloneAdsDataList.size - 1) {
        mCloneAdsCurrentCount + 1
    } else {
        0
    }
    return mCloneAdsCurrentCount
}

val mCloneAdsDataList: ArrayList<CloneAdsData.CloneAdsDataItem> = ArrayList()

private class CloneAdsRunnable(val mActivity: Activity) : Runnable {
    override fun run() {
        val mRetrofit = CloneAdsClient.getRetrofit(mActivity.mAdsSharedPref.mCloneAdsUrl)
        mRetrofit?.apply {
            logE("RConfig::CloneApps:request")
            this.create(CloneAdsInterface::class.java).getCloneAdsData(mActivity.mAdsSharedPref.mCloneAdsAcName).enqueue(object : Callback<CloneAdsData?> {
                override fun onResponse(mCall: Call<CloneAdsData?>, mResponse: Response<CloneAdsData?>) {
                    if (mResponse.code() == 200) {
                        val mCloneAdsData = mResponse.body()
                        if (mCloneAdsData != null) {
                            var itemList = mCloneAdsData.data
                            if (!itemList.isNullOrEmpty()) {
                                itemList = itemList.shuffled()
                                mCloneAdsDataList.clear()
                                mCloneAdsDataList.addAll(itemList)
                                logE("RConfig::CloneApps:Response:: ${mCloneAdsDataList.size}")
                            }
                        }
                    }
                }

                override fun onFailure(mCall: Call<CloneAdsData?>, t: Throwable) {
                    mCall.cancel()
                    logE("RConfig::CloneApps:Failure:: ${t.message}")
                }
            })
        }
    }
}
