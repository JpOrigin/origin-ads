package com.origin.ads.sample.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.nativead.NativeAd
import com.origin.ads.nativeads.GoogleNativeAds
import com.origin.ads.nativeads.NativeAdsCallback
import com.origin.ads.sample.R
import com.origin.ads.sample.clone.mCloneAdsDataList
import com.origin.ads.sample.databinding.ActivityNativeAdsBinding
import com.origin.ads.sample.databinding.AdCloneNativeSmallBinding
import com.origin.ads.sample.databinding.AdCloneNativeXlBinding
import com.origin.ads.sample.databinding.AdCloneNativeXxlBinding
import com.origin.ads.sample.databinding.AdUnifiedNativeSmallBinding
import com.origin.ads.sample.databinding.AdUnifiedNativeXlBinding
import com.origin.ads.sample.databinding.AdUnifiedNativeXxlBinding
import com.origin.ads.sample.databinding.ShimmerNativeSmallBinding
import com.origin.ads.sample.databinding.ShimmerNativeXlBinding
import com.origin.ads.sample.databinding.ShimmerNativeXxlBinding
import com.origin.ads.sample.datastores.mAdsSharedPref
import com.origin.ads.sample.utils.beGone
import com.origin.ads.sample.utils.beVisible
import com.origin.ads.sample.utils.dp
import com.origin.ads.sample.utils.getScreenHeight
import com.origin.ads.sample.utils.populateCloneNativeSmallAdView
import com.origin.ads.sample.utils.populateCloneNativeXLAdView
import com.origin.ads.sample.utils.populateCloneNativeXXLAdView
import com.origin.ads.sample.utils.populateNativeSmallAdView
import com.origin.ads.sample.utils.populateNativeXLAdView
import com.origin.ads.sample.utils.populateNativeXXLAdView

class NativeAdsActivity : AppCompatActivity() {
    private lateinit var mActivityNativeAdsBinding: ActivityNativeAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT), navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
        mActivityNativeAdsBinding = ActivityNativeAdsBinding.inflate(layoutInflater)
        setContentView(mActivityNativeAdsBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mNativeSize = intent.getIntExtra("native_size", R.id.rbBigNative)
        when (mNativeSize) {
            R.id.rbSmallNative -> {
                showGoogleNativeXlAd()
            }
            R.id.rbSmallNativeBanner -> {
                showGoogleNativeSmallAd()
            }
            else -> {
                showGoogleNativeXxlAd()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mGoogleNativeAds?.destroyLoadedAds()
    }
    private var mGoogleNativeAds: GoogleNativeAds? = null
    private fun showGoogleNativeXlAd() {
        if (this@NativeAdsActivity.mAdsSharedPref.mIsSkipAllNativeAds) {
            mActivityNativeAdsBinding.includeGoogleNative.rlMainGoogleNative.beGone()
            return
        }
        mActivityNativeAdsBinding.includeGoogleNative.rlMainGoogleNative.beVisible()
        // Used_For_Shimmer_Layout
        if (this@NativeAdsActivity.mAdsSharedPref.mShowNativeShimmerLayout) {
            mActivityNativeAdsBinding.includeGoogleNative.flSpaceLayout.beGone()
            mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.beVisible()
            ShimmerNativeXlBinding.inflate(layoutInflater).apply {
                mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.removeAllViews()
                mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.addView(this.root)
            }
        } else {
            mActivityNativeAdsBinding.includeGoogleNative.flSpaceLayout.beVisible()
            mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
            val params: FrameLayout.LayoutParams = mActivityNativeAdsBinding.includeGoogleNative.spMain.layoutParams as FrameLayout.LayoutParams
            params.height = 135.dp
            params.width = FrameLayout.LayoutParams.MATCH_PARENT
            mActivityNativeAdsBinding.includeGoogleNative.spMain.setLayoutParams(params)
        }
        // Used_For_Load_Native_Ads
        if (!this@NativeAdsActivity.mAdsSharedPref.mIsForceNativeCloneAds) {
            if(mGoogleNativeAds == null){
                mGoogleNativeAds = GoogleNativeAds()
            }
            mGoogleNativeAds?.show(this@NativeAdsActivity, this@NativeAdsActivity.mAdsSharedPref.mNativeAds, object : NativeAdsCallback {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    mActivityNativeAdsBinding.includeGoogleNative.flSpaceLayout.beGone()
                    mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
                    AdUnifiedNativeXlBinding.inflate(layoutInflater).apply {
                        populateNativeXLAdView(nativeAd, this)
                        mActivityNativeAdsBinding.includeGoogleNative.flGoogleNative.removeAllViews()
                        mActivityNativeAdsBinding.includeGoogleNative.flGoogleNative.addView(this.root)
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
        if (this@NativeAdsActivity.mAdsSharedPref.mIsNativeCloneAds && mCloneAdsDataList.isNotEmpty()) {
            mActivityNativeAdsBinding.includeGoogleNative.flSpaceLayout.beGone()
            mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
            AdCloneNativeXlBinding.inflate(layoutInflater).apply {
                populateCloneNativeXLAdView(this)
                mActivityNativeAdsBinding.includeGoogleNative.flGoogleNative.removeAllViews()
                mActivityNativeAdsBinding.includeGoogleNative.flGoogleNative.addView(this.root)
            }
        }
    }

    private fun showGoogleNativeSmallAd() {
        if (this@NativeAdsActivity.mAdsSharedPref.mIsSkipAllNativeAds) {
            mActivityNativeAdsBinding.includeGoogleNative.rlMainGoogleNative.beGone()
            return
        }
        mActivityNativeAdsBinding.includeGoogleNative.rlMainGoogleNative.beVisible()
        // Used_For_Shimmer_Layout
        if (this@NativeAdsActivity.mAdsSharedPref.mShowNativeShimmerLayout) {
            mActivityNativeAdsBinding.includeGoogleNative.flSpaceLayout.beGone()
            mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.beVisible()
            ShimmerNativeSmallBinding.inflate(layoutInflater).apply {
                mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.removeAllViews()
                mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.addView(this.root)
            }
        } else {
            mActivityNativeAdsBinding.includeGoogleNative.flSpaceLayout.beVisible()
            mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
            val params: FrameLayout.LayoutParams = mActivityNativeAdsBinding.includeGoogleNative.spMain.layoutParams as FrameLayout.LayoutParams
            params.height = 86.dp
            params.width = FrameLayout.LayoutParams.MATCH_PARENT
            mActivityNativeAdsBinding.includeGoogleNative.spMain.setLayoutParams(params)
        }
        // Used_For_Load_Native_Ads
        if (!this@NativeAdsActivity.mAdsSharedPref.mIsForceNativeCloneAds) {
            if(mGoogleNativeAds == null){
                mGoogleNativeAds = GoogleNativeAds()
            }
            mGoogleNativeAds?.show(this@NativeAdsActivity, this@NativeAdsActivity.mAdsSharedPref.mNativeAds, object : NativeAdsCallback {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    mActivityNativeAdsBinding.includeGoogleNative.flSpaceLayout.beGone()
                    mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
                    AdUnifiedNativeSmallBinding.inflate(layoutInflater).apply {
                        populateNativeSmallAdView(nativeAd, this)
                        mActivityNativeAdsBinding.includeGoogleNative.flGoogleNative.removeAllViews()
                        mActivityNativeAdsBinding.includeGoogleNative.flGoogleNative.addView(this.root)
                    }
                }

                override fun onNativeAdFailedToLoad() {
                    showCloneNativeSmallAd()
                }
            })
        } else {
            showCloneNativeSmallAd()
        }
    }

    private fun showCloneNativeSmallAd() {
        if (this@NativeAdsActivity.mAdsSharedPref.mIsNativeCloneAds && mCloneAdsDataList.isNotEmpty()) {
            mActivityNativeAdsBinding.includeGoogleNative.flSpaceLayout.beGone()
            mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
            AdCloneNativeSmallBinding.inflate(layoutInflater).apply {
                populateCloneNativeSmallAdView(this)
                mActivityNativeAdsBinding.includeGoogleNative.flGoogleNative.removeAllViews()
                mActivityNativeAdsBinding.includeGoogleNative.flGoogleNative.addView(this.root)
            }
        }
    }

    private fun showGoogleNativeXxlAd() {
        if (this@NativeAdsActivity.mAdsSharedPref.mIsSkipAllNativeAds) {
            mActivityNativeAdsBinding.includeGoogleNative.rlMainGoogleNative.beGone()
            return
        }
        mActivityNativeAdsBinding.includeGoogleNative.rlMainGoogleNative.beVisible()

        // Used_For_Shimmer_Layout
        val mScHeight = if (this@NativeAdsActivity.getScreenHeight() / 5 > 300) {
            (this@NativeAdsActivity.getScreenHeight() / 5)
        } else {
            300
        }
        if (this@NativeAdsActivity.mAdsSharedPref.mShowNativeShimmerLayout) {
            mActivityNativeAdsBinding.includeGoogleNative.flSpaceLayout.beGone()
            mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.beVisible()
            ShimmerNativeXxlBinding.inflate(layoutInflater).apply {
                mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.removeAllViews()
                val params: LinearLayout.LayoutParams = this.iv2.layoutParams as LinearLayout.LayoutParams
                params.height = mScHeight
                params.width = FrameLayout.LayoutParams.MATCH_PARENT
                this.iv2.layoutParams = params
                mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.addView(this.root)
            }
        } else {
            mActivityNativeAdsBinding.includeGoogleNative.flSpaceLayout.beVisible()
            mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
            val params: FrameLayout.LayoutParams = mActivityNativeAdsBinding.includeGoogleNative.spMain.layoutParams as FrameLayout.LayoutParams
            params.height = mScHeight
            params.width = FrameLayout.LayoutParams.MATCH_PARENT
            params.topMargin = 74.dp
            params.bottomMargin = 58.dp
            mActivityNativeAdsBinding.includeGoogleNative.spMain.setLayoutParams(params)
        }

        // Used_For_Load_Native_Ads
        if (!this@NativeAdsActivity.mAdsSharedPref.mIsForceNativeCloneAds) {
            if(mGoogleNativeAds == null){
                mGoogleNativeAds = GoogleNativeAds()
            }
            mGoogleNativeAds?.show(this@NativeAdsActivity, this@NativeAdsActivity.mAdsSharedPref.mNativeAds, object : NativeAdsCallback {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    mActivityNativeAdsBinding.includeGoogleNative.flSpaceLayout.beGone()
                    mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
                    AdUnifiedNativeXxlBinding.inflate(layoutInflater).apply {
                        this@NativeAdsActivity.populateNativeXXLAdView(nativeAd, this)
                        mActivityNativeAdsBinding.includeGoogleNative.flGoogleNative.removeAllViews()
                        mActivityNativeAdsBinding.includeGoogleNative.flGoogleNative.addView(this.root)
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
        if (this@NativeAdsActivity.mAdsSharedPref.mIsNativeCloneAds && mCloneAdsDataList.isNotEmpty()) {
            mActivityNativeAdsBinding.includeGoogleNative.flSpaceLayout.beGone()
            mActivityNativeAdsBinding.includeGoogleNative.flShimmerGoogleNative.beGone()
            AdCloneNativeXxlBinding.inflate(layoutInflater).apply {
                populateCloneNativeXXLAdView(this)
                mActivityNativeAdsBinding.includeGoogleNative.flGoogleNative.removeAllViews()
                mActivityNativeAdsBinding.includeGoogleNative.flGoogleNative.addView(this.root)
            }
        }
    }

}