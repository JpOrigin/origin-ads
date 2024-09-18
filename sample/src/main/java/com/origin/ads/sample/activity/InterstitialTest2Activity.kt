package com.origin.ads.sample.activity

import android.app.Activity
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
import com.origin.ads.interstitialads.GoogleInterstitialAds
import com.origin.ads.sample.R
import com.origin.ads.sample.databinding.ActivityInterstitialTest2Binding
import com.origin.ads.sample.utils.getInterAdsUnitId

class InterstitialTest2Activity : AppCompatActivity() {
    private lateinit var mActivityInterstitialTest2Binding: ActivityInterstitialTest2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT), navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
        mActivityInterstitialTest2Binding = ActivityInterstitialTest2Binding.inflate(layoutInflater)
        setContentView(mActivityInterstitialTest2Binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val mInterType = intent.getIntExtra("interstitial_type", R.id.rbInterOnClick)
        if (mInterType == R.id.rbInterOnCreate) {
            GoogleInterstitialAds().show(this@InterstitialTest2Activity, getInterAdsUnitId()) {
                // noting to do
            }
        }
        mActivityInterstitialTest2Binding.btnShowInterstitial1.setOnClickListener { testInterstitial(mInterType, 1) }
        mActivityInterstitialTest2Binding.btnShowInterstitial3.setOnClickListener { testInterstitial(mInterType, 3) }
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mInterType == R.id.rbInterOnBackPressed) {
                    GoogleInterstitialAds().show(this@InterstitialTest2Activity, getInterAdsUnitId()) {
                        finish()
                    }
                } else {
                    finish()
                }
            }
        })
    }
    private inline fun <reified T : Activity> launchMyActivity(base: Intent? = null) {
        Intent(this, T::class.java).apply {
            if (base != null) {
                this.putExtras(base)
            }
            startActivity(this)
        }
    }
    private fun startInterTestAct(mInterType: Int, clickPos: Int) {
        when (clickPos) {
            1 -> launchMyActivity<InterstitialTest1Activity>(base = Intent().putExtra("interstitial_type", mInterType))
            2 -> launchMyActivity<InterstitialTest2Activity>(base = Intent().putExtra("interstitial_type", mInterType))
            3 -> launchMyActivity<InterstitialTest3Activity>(base = Intent().putExtra("interstitial_type", mInterType))
        }
    }
    private fun testInterstitial(mInterType: Int, clickPos: Int) {
        if (mInterType == R.id.rbInterOnClick) {
            GoogleInterstitialAds().show(this@InterstitialTest2Activity, getInterAdsUnitId()) { startInterTestAct(mInterType, clickPos) }
        } else {
            startInterTestAct(mInterType, clickPos)
        }
    }
}