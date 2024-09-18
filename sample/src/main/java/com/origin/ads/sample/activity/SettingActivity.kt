package com.origin.ads.sample.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.origin.ads.appopenads.GoogleAppOpenAdManager
import com.origin.ads.sample.databinding.ActivitySettingBinding
import com.origin.ads.sample.datastores.mAdsSharedPref
import com.origin.ads.utils.logE

class SettingActivity : AppCompatActivity() {
    private lateinit var mActivitySettingBinding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT), navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
        mActivitySettingBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(mActivitySettingBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sec = this@SettingActivity.mAdsSharedPref.splashIntervalInSeconds
        mActivitySettingBinding.tILSplashTimer.editText?.apply {
            this.setText(sec.toString())
            this.setSelection(sec.toString().length)
            this.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    val newSec = if (it.isNotEmpty()) {
                        it.toString().toInt()
                    } else {
                        10
                    }
                    logE("SettingActivity :newSec: $newSec")
                    this@SettingActivity.mAdsSharedPref.splashIntervalInSeconds = newSec
                }
            }
        }
        val forceSkipConfig = this@SettingActivity.mAdsSharedPref.mIsForceSkipConfig
        mActivitySettingBinding.switchEnableConfig.isChecked = forceSkipConfig
        mActivitySettingBinding.switchEnableConfig.setOnCheckedChangeListener { _, isChecked ->
            this@SettingActivity.mAdsSharedPref.mIsForceSkipConfig = isChecked
        }
        GoogleAppOpenAdManager.pauseAppOpenAds()
    }

    override fun onDestroy() {
        super.onDestroy()
        GoogleAppOpenAdManager.resumeAppOpenAds()
    }
}