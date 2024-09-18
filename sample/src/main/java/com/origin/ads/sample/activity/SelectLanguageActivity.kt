package com.origin.ads.sample.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.nativead.NativeAd
import com.origin.ads.nativeads.GoogleNativePreLoadAds
import com.origin.ads.nativeads.NativeAdsCallback
import com.origin.ads.sample.activity.SplashActivity.Companion.mGoogleNativePreLoadAds
import com.origin.ads.sample.clone.mCloneAdsDataList
import com.origin.ads.sample.databinding.ActivitySelectLanguageBinding
import com.origin.ads.sample.databinding.AdCloneNativeXxlBinding
import com.origin.ads.sample.databinding.AdUnifiedNativeXxlBinding
import com.origin.ads.sample.databinding.ShimmerNativeXxlBinding
import com.origin.ads.sample.datastores.mAdsSharedPref
import com.origin.ads.sample.utils.beGone
import com.origin.ads.sample.utils.beVisible
import com.origin.ads.sample.utils.dp
import com.origin.ads.sample.utils.getScreenHeight
import com.origin.ads.sample.utils.populateCloneNativeXXLAdView
import com.origin.ads.sample.utils.populateNativeXXLAdView

class SelectLanguageActivity : AppCompatActivity() {

    private lateinit var mActivitySelectLanguageBinding: ActivitySelectLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT), navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
        mActivitySelectLanguageBinding = ActivitySelectLanguageBinding.inflate(layoutInflater)
        setContentView(mActivitySelectLanguageBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mActivitySelectLanguageBinding.tvSelect.setOnClickListener {
            this@SelectLanguageActivity.mAdsSharedPref.mAppLanguage = "en"
            startMyNextActivity()
        }
        initGoogleNativeAds()
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startMyNextActivity()
            }
        })
    }


    private fun startMyNextActivity() {
        Intent(this@SelectLanguageActivity, MainActivity::class.java).apply {
            this@SelectLanguageActivity.startActivity(this)
            this@SelectLanguageActivity.finish()
        }
    }

    /***
     * check and show NativeAds
     * */
    private fun initGoogleNativeAds() {
        if (this@SelectLanguageActivity.mAdsSharedPref.mIsSkipAllNativeAds) {
            mActivitySelectLanguageBinding.includeGoogleNative.rlMainGoogleNative.beGone()
            return
        }
        mActivitySelectLanguageBinding.includeGoogleNative.rlMainGoogleNative.beVisible()

        // Used_For_Shimmer_Layout
        val mScHeight = if (this@SelectLanguageActivity.getScreenHeight() / 5 > 300) {
            (this@SelectLanguageActivity.getScreenHeight() / 5)
        } else {
            300
        }
        if (this@SelectLanguageActivity.mAdsSharedPref.mShowNativeShimmerLayout) {
            mActivitySelectLanguageBinding.includeGoogleNative.flSpaceLayout.beGone()
            mActivitySelectLanguageBinding.includeGoogleNative.flShimmerGoogleNative.beVisible()
            ShimmerNativeXxlBinding.inflate(layoutInflater).apply {
                mActivitySelectLanguageBinding.includeGoogleNative.flShimmerGoogleNative.removeAllViews()
                val params: LinearLayout.LayoutParams = this.iv2.layoutParams as LinearLayout.LayoutParams
                params.height = mScHeight
                params.width = FrameLayout.LayoutParams.MATCH_PARENT
                this.iv2.layoutParams = params
                mActivitySelectLanguageBinding.includeGoogleNative.flShimmerGoogleNative.addView(this.root)
            }
        } else {
            mActivitySelectLanguageBinding.includeGoogleNative.flSpaceLayout.beVisible()
            mActivitySelectLanguageBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
            val params: FrameLayout.LayoutParams = mActivitySelectLanguageBinding.includeGoogleNative.spMain.layoutParams as FrameLayout.LayoutParams
            params.height = mScHeight
            params.width = FrameLayout.LayoutParams.MATCH_PARENT
            params.topMargin = 74.dp
            params.bottomMargin = 58.dp
            mActivitySelectLanguageBinding.includeGoogleNative.spMain.setLayoutParams(params)
        }

        // Used_For_Get_Native_Ads_Id
        val adUnitId = if (this@SelectLanguageActivity.mAdsSharedPref.mIsForceShowOfflineAllNativeAds) {
            this@SelectLanguageActivity.mAdsSharedPref.mOfflineNativeAds
        } else {
            this@SelectLanguageActivity.mAdsSharedPref.mNativeAds
        }
        // Used_For_Load_Native_Ads
        if (!this@SelectLanguageActivity.mAdsSharedPref.mIsForceNativeCloneAds) {
            if (mGoogleNativePreLoadAds == null) {
                mGoogleNativePreLoadAds = GoogleNativePreLoadAds()
            }
            mGoogleNativePreLoadAds?.show(this@SelectLanguageActivity, adUnitId, object : NativeAdsCallback {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    mActivitySelectLanguageBinding.includeGoogleNative.flSpaceLayout.beGone()
                    mActivitySelectLanguageBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
                    AdUnifiedNativeXxlBinding.inflate(layoutInflater).apply {
                        this@SelectLanguageActivity.populateNativeXXLAdView(nativeAd, this)
                        mActivitySelectLanguageBinding.includeGoogleNative.flGoogleNative.removeAllViews()
                        mActivitySelectLanguageBinding.includeGoogleNative.flGoogleNative.addView(this.root)
                    }
                }
                override fun onNativeAdFailedToLoad() {
                    showCloneNativeXxlAd()
                }
            })
        } else {
            showCloneNativeXxlAd()
        }
    }
    private fun showCloneNativeXxlAd() {
        if (this@SelectLanguageActivity.mAdsSharedPref.mIsNativeCloneAds && mCloneAdsDataList.isNotEmpty()) {
            mActivitySelectLanguageBinding.includeGoogleNative.flSpaceLayout.beGone()
            mActivitySelectLanguageBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
            AdCloneNativeXxlBinding.inflate(layoutInflater).apply {
                populateCloneNativeXXLAdView(this)
                mActivitySelectLanguageBinding.includeGoogleNative.flGoogleNative.removeAllViews()
                mActivitySelectLanguageBinding.includeGoogleNative.flGoogleNative.addView(this.root)
            }
        }
    }

}