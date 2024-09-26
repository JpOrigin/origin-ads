package com.origin.ads.sample.activity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.origin.ads.nativeads.GoogleNativePreLoadAds
import com.origin.ads.sample.clone.loadCloneAdsData
import com.origin.ads.sample.databinding.ActivitySplashBinding
import com.origin.ads.sample.datastores.mAdsSharedPref
import com.origin.ads.sample.dialogs.NetworkDialog
import com.origin.ads.sample.remotedata.isConfigDataFetched
import com.origin.ads.sample.remotedata.loadConfigData
import com.origin.ads.sample.utils.isNetworkAvailable
import com.origin.ads.splashopenads.GoogleSplashAppOpenAdManager
import com.origin.ads.utils.logE


class SplashActivity : AppCompatActivity() {
    private lateinit var mActivitySplashBinding: ActivitySplashBinding

    companion object {
        private var mSplashAppOpenAdManager: GoogleSplashAppOpenAdManager? = null
        var mGoogleNativePreLoadAds: GoogleNativePreLoadAds? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT), navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
        mActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mActivitySplashBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkNetwork()
        startHandler(true)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        val animator = ValueAnimator.ofInt(0, mActivitySplashBinding.pbSplash.max)
        val splashDelayMillis = (this@SplashActivity.mAdsSharedPref.splashIntervalInSeconds * 1000).toLong() // (sec * 1000)
        animator.setDuration(splashDelayMillis)
        animator.addUpdateListener { animation ->
            val mProgress = animation.animatedValue as Int
            mActivitySplashBinding.pbSplash.progress = mProgress
            when {
                mProgress >= 200 -> mActivitySplashBinding.tvLoading.text = "Ready now..."
                mProgress >= 100 -> mActivitySplashBinding.tvLoading.text = "Preparing..."
                else -> mActivitySplashBinding.tvLoading.text = "Loading..."
            }
        }
        animator.start()
    }

    override fun onPause() {
        super.onPause()
        mSplashAppOpenAdManager?.pauseAds()
        isSplashPaused = true
        mSplashHandler?.removeCallbacks(mSplashRunnable!!)
    }

    override fun onResume() {
        super.onResume()
        mSplashAppOpenAdManager?.resumeAds()
        if (isSplashPaused) {
            isSplashPaused = false
            startHandler(false)
        }
    }

    private var isSplashPaused = false
    private var mSplashHandler: Handler? = null
    private var mSplashRunnable: Runnable? = null
    private fun startHandler(isFromCreate: Boolean = false) {
        if (!isNetworkAvailable()) {
            showNetworkDialog()
            return
        }
        updateUI()
        hideNetworkDialog()
        getRemoteConfigData(isFromCreate)
        checkInitializeLoadSplashAds()
        if (mSplashHandler == null) {
            mSplashHandler = Handler(Looper.getMainLooper())
        }
        mSplashRunnable = Runnable {
            mSplashAppOpenAdManager?.pauseAds()
            mOnShowAdCompleteListener.onShowAdComplete()
        }
        val splashDelayMillis = (this@SplashActivity.mAdsSharedPref.splashIntervalInSeconds * 1000).toLong() // (sec * 1000)
        mSplashHandler?.postDelayed(mSplashRunnable!!, splashDelayMillis)
    }

    /***
     *
     * Get/Load Remote Config Data
     *
     * */
    private fun getRemoteConfigData(fromCreate: Boolean) {
        if (fromCreate) {
            isConfigDataFetched = false
        }
        loadConfigData {
            runOnUiThread {
                checkInitializeLoadSplashAds()
                initializeNativePreLoadAds()
                loadCloneAdsData()
            }
        }
    }

    /***
     * Initialize Google_Splash_AppOpen_Ads
     * */
    private val mOnShowAdCompleteListener = object : GoogleSplashAppOpenAdManager.OnShowAdCompleteListener {
        override fun onShowAd() {
            mActivitySplashBinding.pbSplash.clearAnimation()
        }

        override fun onShowAdComplete() {
            startMyNextActivity()
        }
    }

    private fun checkInitializeLoadSplashAds() {
        if (mSplashAppOpenAdManager == null) {
            mSplashAppOpenAdManager = GoogleSplashAppOpenAdManager()
        }
        mSplashAppOpenAdManager?.showAdIfAvailableWithDelay(this@SplashActivity, mOnShowAdCompleteListener)
        if (!this@SplashActivity.mAdsSharedPref.mIsSkipSplashOpenAds) {
            mSplashAppOpenAdManager?.loadAd(this@SplashActivity, this@SplashActivity.mAdsSharedPref.mSplashOpenAds, mOnShowAdCompleteListener)
        }
    }

    /***
     * Initialize Google_Native_Pre_Load_Ads
     * */
    private fun initializeNativePreLoadAds() {
        if (this@SplashActivity.mAdsSharedPref.mAppLanguage.isEmpty() && !this@SplashActivity.mAdsSharedPref.mIsSkipAllNativeAds && !this@SplashActivity.mAdsSharedPref.mIsForceNativeCloneAds) {
            if (mGoogleNativePreLoadAds == null) {
                mGoogleNativePreLoadAds = GoogleNativePreLoadAds(1)
            }
            mGoogleNativePreLoadAds?.load(this@SplashActivity, this@SplashActivity.mAdsSharedPref.mLanguageNativeAds)
        }
    }

    // start next activity
    private fun startMyNextActivity() {
        if (this@SplashActivity.mAdsSharedPref.mAppLanguage.isEmpty()) {
            Intent(this@SplashActivity, SelectLanguageActivity::class.java).apply {
                this@SplashActivity.startActivity(this)
                this@SplashActivity.finish()
            }
        } else {
            Intent(this@SplashActivity, MainActivity::class.java).apply {
                this@SplashActivity.startActivity(this)
                this@SplashActivity.finish()
            }
        }
    }
    // start next activity


    /***
     *
     * Check Network Connection
     *
     * */
    var mIsNetworkAvailable = false
    private fun checkNetwork() {
        val networkRequest = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).build()
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                mIsNetworkAvailable = true
                hideNetworkDialog()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                mIsNetworkAvailable = false
                showNetworkDialog()
            }
        }
        val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    /***
     *
     * No Network Dialog
     *
     * */
    private var mNetworkDialog: NetworkDialog? = null
    private fun initNetworkDialog() {
        if (mNetworkDialog == null) {
            mNetworkDialog = NetworkDialog(mActivity = this@SplashActivity) {
                startHandler(false)
            }
        }
    }

    private fun showNetworkDialog() {
        this@SplashActivity.runOnUiThread {
            initNetworkDialog()
            mNetworkDialog?.show()
        }
    }

    private fun hideNetworkDialog() {
        this@SplashActivity.runOnUiThread {
            initNetworkDialog()
            mNetworkDialog?.dismiss()
        }
    }
}