package com.origin.ads.sample.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.origin.ads.appopenads.GoogleAppOpenAdManager
import com.origin.ads.interstitialads.GoogleInterstitialAds
import com.origin.ads.sample.MyApplication
import com.origin.ads.sample.R
import com.origin.ads.sample.databinding.ActivityMainBinding
import com.origin.ads.sample.datastores.mAdsSharedPref
import com.origin.ads.sample.utils.getInterAdsUnitId
import com.origin.ads.sample.utils.showRateUsPlayStore


class MainActivity : AppCompatActivity() {
    private lateinit var mActivityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT), navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
        mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mActivityMainBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // initialize app open ads
        initializeAppOpenAds()
        initializeInterstitialAds()

        mActivityMainBinding.switchShowShimmerNativeAds.isChecked = this@MainActivity.mAdsSharedPref.mShowNativeShimmerLayout
        mActivityMainBinding.switchShowShimmerNativeAds.setOnCheckedChangeListener { _, isChecked ->
            MyApplication.getInstance().mAdsSharedPref.mShowNativeShimmerLayout = isChecked
        }
        mActivityMainBinding.switchNativeCloneAds.isChecked = this@MainActivity.mAdsSharedPref.mIsNativeCloneAds
        mActivityMainBinding.switchNativeCloneAds.setOnCheckedChangeListener { _, isChecked ->
            MyApplication.getInstance().mAdsSharedPref.mIsNativeCloneAds = isChecked
        }
        mActivityMainBinding.switchNativeForceCloneAds.isChecked = this@MainActivity.mAdsSharedPref.mIsForceNativeCloneAds
        mActivityMainBinding.switchNativeForceCloneAds.setOnCheckedChangeListener { _, isChecked ->
            MyApplication.getInstance().mAdsSharedPref.mIsForceNativeCloneAds = isChecked
        }
        mActivityMainBinding.btnShowNative.setOnClickListener {
            Intent(this@MainActivity, NativeAdsActivity::class.java).apply {
                this.putExtra("native_size", mActivityMainBinding.rgNativeSize.checkedRadioButtonId)
                this@MainActivity.startActivity(this)
            }
        }
        mActivityMainBinding.btnShowInterstitial.setOnClickListener {
            val mInterType = mActivityMainBinding.rgInter.checkedRadioButtonId
            if (mInterType == R.id.rbInterOnClick) {
                GoogleInterstitialAds().show(this@MainActivity, getInterAdsUnitId()) {
                    Intent(this@MainActivity, InterstitialAdsActivity::class.java).apply {
                        this.putExtra("interstitial_type", mInterType)
                        this@MainActivity.startActivity(this)
                    }
                }
            } else {
                Intent(this@MainActivity, InterstitialAdsActivity::class.java).apply {
                    this.putExtra("interstitial_type", mInterType)
                    this@MainActivity.startActivity(this)
                }
            }
        }

        mActivityMainBinding.switchShowShimmerBannerAds.isChecked = this@MainActivity.mAdsSharedPref.mShowBannerShimmerLayout
        mActivityMainBinding.switchShowShimmerBannerAds.setOnCheckedChangeListener { _, isChecked ->
            MyApplication.getInstance().mAdsSharedPref.mShowBannerShimmerLayout = isChecked
        }
        mActivityMainBinding.switchBannerCloneAds.isChecked = this@MainActivity.mAdsSharedPref.mIsBannerCloneAds
        mActivityMainBinding.switchBannerCloneAds.setOnCheckedChangeListener { _, isChecked ->
            MyApplication.getInstance().mAdsSharedPref.mIsBannerCloneAds = isChecked
        }
        mActivityMainBinding.switchBannerForceCloneAds.isChecked = this@MainActivity.mAdsSharedPref.mIsForceBannerCloneAds
        mActivityMainBinding.switchBannerForceCloneAds.setOnCheckedChangeListener { _, isChecked ->
            MyApplication.getInstance().mAdsSharedPref.mIsForceBannerCloneAds = isChecked
        }
        mActivityMainBinding.btnBanner.setOnClickListener {
            Intent(this@MainActivity, BannerAdsActivity::class.java).apply {
                this@MainActivity.startActivity(this)
            }
        }
        mActivityMainBinding.btnRateUs.setOnClickListener {
            showRateUsPlayStore()
        }

        mActivityMainBinding.btnSettings.setOnClickListener {
            Intent(this@MainActivity, SettingActivity::class.java).apply {
                this@MainActivity.startActivity(this)
            }
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                moveTaskToBack(true)
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        mGoogleAppOpenAdManager?.destroyAppOpenAds()
    }

    /***
     * initialize app open ads */
    private var mGoogleAppOpenAdManager: GoogleAppOpenAdManager? = null
    private fun initializeAppOpenAds() {
        MyApplication.getInstance().apply {
            mGoogleAppOpenAdManager = GoogleAppOpenAdManager()
            mGoogleAppOpenAdManager?.initialize(this, this@MainActivity.mAdsSharedPref.mAppOpenAds, this@MainActivity.mAdsSharedPref.mIsSkipAppOpenAds)
        }
    }

    /***
     * initialize interstitial ads */
    private fun initializeInterstitialAds() {
        GoogleInterstitialAds().apply {
            this.load(this@MainActivity, getInterAdsUnitId())
        }
    }
}

