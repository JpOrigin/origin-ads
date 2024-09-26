package com.origin.ads.sample.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdView
import com.origin.ads.bannerads.BannerAdsCallback
import com.origin.ads.bannerads.GoogleBannerAds
import com.origin.ads.sample.clone.mCloneAdsDataList
import com.origin.ads.sample.databinding.ActivityBannerAdsBinding
import com.origin.ads.sample.databinding.AdCloneBannerBinding
import com.origin.ads.sample.databinding.ShimmerBannerLayoutBinding
import com.origin.ads.sample.datastores.mAdsSharedPref
import com.origin.ads.sample.utils.beGone
import com.origin.ads.sample.utils.beVisible
import com.origin.ads.sample.utils.getAdSize
import com.origin.ads.sample.utils.populateCloneBannerAdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BannerAdsActivity : AppCompatActivity() {
    private lateinit var mActivityBannerAdsBinding: ActivityBannerAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT), navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
        mActivityBannerAdsBinding = ActivityBannerAdsBinding.inflate(layoutInflater)
        setContentView(mActivityBannerAdsBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showGoogleBannerAd()
    }

    override fun onPause() {
        super.onPause()
        mGoogleBannerAds?.pauseLoadedAds()
    }

    override fun onResume() {
        super.onResume()
        mGoogleBannerAds?.resumeLoadedAds()
    }

    override fun onDestroy() {
        super.onDestroy()
        mGoogleBannerAds?.destroyLoadedAds()
    }

    private var mGoogleBannerAds: GoogleBannerAds? = null
    private fun showGoogleBannerAd() {
        if (this@BannerAdsActivity.mAdsSharedPref.mIsSkipAllBannerAds) {
            mActivityBannerAdsBinding.includeGoogleBanner.rlMainGoogleBanner.beGone()
            return
        }
        mActivityBannerAdsBinding.includeGoogleBanner.rlMainGoogleBanner.beVisible()

        // Used_For_Shimmer_Layout
        if (this@BannerAdsActivity.mAdsSharedPref.mShowBannerShimmerLayout) {
            mActivityBannerAdsBinding.includeGoogleBanner.flSpaceLayout.beGone()
            mActivityBannerAdsBinding.includeGoogleBanner.flShimmerGoogleBanner.beVisible()
            ShimmerBannerLayoutBinding.inflate(layoutInflater).apply {
                mActivityBannerAdsBinding.includeGoogleBanner.flShimmerGoogleBanner.removeAllViews()
                mActivityBannerAdsBinding.includeGoogleBanner.flShimmerGoogleBanner.addView(this.root)
            }
        } else {
            mActivityBannerAdsBinding.includeGoogleBanner.flSpaceLayout.beVisible()
            mActivityBannerAdsBinding.includeGoogleBanner.flShimmerGoogleBanner.beGone()
        }

        // Used_For_Load_Banner_Ads
        if (!this@BannerAdsActivity.mAdsSharedPref.mIsForceBannerCloneAds) {
            if (mGoogleBannerAds == null) {
                mGoogleBannerAds = GoogleBannerAds()
            }
            CoroutineScope(Dispatchers.IO).launch {
                runOnUiThread {
                    mGoogleBannerAds?.show(
                        this@BannerAdsActivity, this@BannerAdsActivity.mAdsSharedPref.mBannerAds, this@BannerAdsActivity.getAdSize(mActivityBannerAdsBinding.includeGoogleBanner.flGoogleBanner),
                        object : BannerAdsCallback {
                            override fun onBannerAdLoaded(adView: AdView) {
                                mActivityBannerAdsBinding.includeGoogleBanner.flSpaceLayout.beGone()
                                mActivityBannerAdsBinding.includeGoogleBanner.flShimmerGoogleBanner.beGone()
                                mActivityBannerAdsBinding.includeGoogleBanner.flGoogleBanner.removeAllViews()
                                mActivityBannerAdsBinding.includeGoogleBanner.flGoogleBanner.addView(adView)
                            }

                            override fun onBannerAdFailedToLoad() {
                                showCloneBannerAd()
                            }
                        })
                }
            }
        } else {
            showCloneBannerAd()
        }
    }

    private fun showCloneBannerAd() {
        if (this@BannerAdsActivity.mAdsSharedPref.mIsBannerCloneAds && mCloneAdsDataList.isNotEmpty()) {
            mActivityBannerAdsBinding.includeGoogleBanner.flSpaceLayout.beGone()
            mActivityBannerAdsBinding.includeGoogleBanner.flShimmerGoogleBanner.beGone()
            AdCloneBannerBinding.inflate(layoutInflater).apply {
                populateCloneBannerAdView(this)
                mActivityBannerAdsBinding.includeGoogleBanner.flGoogleBanner.removeAllViews()
                mActivityBannerAdsBinding.includeGoogleBanner.flGoogleBanner.addView(this.root)
            }
        }
    }
}