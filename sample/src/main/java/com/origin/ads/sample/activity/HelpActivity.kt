package com.origin.ads.sample.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.nativead.NativeAd
import com.origin.ads.nativeads.GoogleNativePreLoadAds
import com.origin.ads.nativeads.NativeAdsCallback
import com.origin.ads.sample.activity.SelectLanguageActivity.Companion.mGoogleNativePreLoadAds2
import com.origin.ads.sample.clone.mCloneAdsDataList
import com.origin.ads.sample.databinding.ActivityHelpBinding
import com.origin.ads.sample.databinding.AdCloneNativeXlBinding
import com.origin.ads.sample.databinding.AdUnifiedNativeXlBinding
import com.origin.ads.sample.databinding.ShimmerNativeXlBinding
import com.origin.ads.sample.datastores.mAdsSharedPref
import com.origin.ads.sample.utils.beGone
import com.origin.ads.sample.utils.beVisible
import com.origin.ads.sample.utils.dp
import com.origin.ads.sample.utils.populateCloneNativeXLAdView
import com.origin.ads.sample.utils.populateNativeXLAdView

class HelpActivity : AppCompatActivity() {
    private lateinit var mActivityHelpBinding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT), navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
        mActivityHelpBinding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(mActivityHelpBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mActivityHelpBinding.tvSelect.setOnClickListener {
            this@HelpActivity.mAdsSharedPref.mAppLanguage = "en"
            startMyNextActivity()
        }
        showGoogleNativeXlAd()
        mActivityHelpBinding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startMyNextActivity()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mGoogleNativePreLoadAds2?.destroyPreLoadedAds()
    }

    private fun startMyNextActivity() {
        Intent(this@HelpActivity, MainActivity::class.java).apply {
            this@HelpActivity.startActivity(this)
            this@HelpActivity.finish()
        }
    }

    /***
     * check and show NativeAds
     * */
    private fun showGoogleNativeXlAd() {
        if (this@HelpActivity.mAdsSharedPref.mIsSkipAllNativeAds) {
            mActivityHelpBinding.includeGoogleNative.rlMainGoogleNative.beGone()
            return
        }
        mActivityHelpBinding.includeGoogleNative.rlMainGoogleNative.beVisible()
        // Used_For_Shimmer_Layout
        if (this@HelpActivity.mAdsSharedPref.mShowNativeShimmerLayout) {
            mActivityHelpBinding.includeGoogleNative.flSpaceLayout.beGone()
            mActivityHelpBinding.includeGoogleNative.flShimmerGoogleNative.beVisible()
            ShimmerNativeXlBinding.inflate(layoutInflater).apply {
                mActivityHelpBinding.includeGoogleNative.flShimmerGoogleNative.removeAllViews()
                mActivityHelpBinding.includeGoogleNative.flShimmerGoogleNative.addView(this.root)
            }
        } else {
            mActivityHelpBinding.includeGoogleNative.flSpaceLayout.beVisible()
            mActivityHelpBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
            val params: FrameLayout.LayoutParams = mActivityHelpBinding.includeGoogleNative.spMain.layoutParams as FrameLayout.LayoutParams
            params.height = 135.dp
            params.width = FrameLayout.LayoutParams.MATCH_PARENT
            mActivityHelpBinding.includeGoogleNative.spMain.setLayoutParams(params)
        }
        // Used_For_Load_Native_Ads
        if (!this@HelpActivity.mAdsSharedPref.mIsForceNativeCloneAds) {
            if (mGoogleNativePreLoadAds2 == null) {
                mGoogleNativePreLoadAds2 = GoogleNativePreLoadAds(2)
            }
            mGoogleNativePreLoadAds2?.show(this@HelpActivity, this@HelpActivity.mAdsSharedPref.mHelpNativeAds, object : NativeAdsCallback {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    mActivityHelpBinding.includeGoogleNative.flSpaceLayout.beGone()
                    mActivityHelpBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
                    AdUnifiedNativeXlBinding.inflate(layoutInflater).apply {
                        populateNativeXLAdView(nativeAd, this)
                        mActivityHelpBinding.includeGoogleNative.flGoogleNative.removeAllViews()
                        mActivityHelpBinding.includeGoogleNative.flGoogleNative.addView(this.root)
                    }
                }

                override fun onNativeAdFailedToLoad() {
                    showCloneNativeXlAd()
                }
            })
        } else {
            showCloneNativeXlAd()
        }
    }

    private fun showCloneNativeXlAd() {
        if (this@HelpActivity.mAdsSharedPref.mIsNativeCloneAds && mCloneAdsDataList.isNotEmpty()) {
            mActivityHelpBinding.includeGoogleNative.flSpaceLayout.beGone()
            mActivityHelpBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
            AdCloneNativeXlBinding.inflate(layoutInflater).apply {
                populateCloneNativeXLAdView(this)
                mActivityHelpBinding.includeGoogleNative.flGoogleNative.removeAllViews()
                mActivityHelpBinding.includeGoogleNative.flGoogleNative.addView(this.root)
            }
        }
    }
}