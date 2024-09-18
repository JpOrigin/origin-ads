package com.origin.ads.sample.dialogs

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.origin.ads.sample.databinding.DialogNetworkBinding

class NetworkDialog(private val mActivity: Activity, private val callback: () -> Unit) {
    private var mDialogNetworkBinding: DialogNetworkBinding = DialogNetworkBinding.inflate(mActivity.layoutInflater, null, false)
    private var networkDialog: BottomSheetDialog? = null


    init {
        if (networkDialog == null) {
            BottomSheetDialog(mActivity).apply {
                this.setContentView(mDialogNetworkBinding.root)
                (mDialogNetworkBinding.root.parent as View).setBackgroundColor(ContextCompat.getColor(mActivity, android.R.color.transparent))
                this.setCanceledOnTouchOutside(false)
                this.setCancelable(false)
                this.window?.let {
                    it.setBackgroundDrawableResource(android.R.color.transparent)
                    it.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                }
                val standardBottomSheetBehavior = BottomSheetBehavior.from(mDialogNetworkBinding.mainContainer)
                standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                standardBottomSheetBehavior.isDraggable = false
                networkDialog = this
            }
        }

        mDialogNetworkBinding.btnWifiOn.setOnClickListener {
            dismiss(skipExitLoop = true)
            try {
                mActivity.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(mActivity, "It cannot open settings!", Toast.LENGTH_LONG).show()
            }
        }
        mDialogNetworkBinding.btnMobileDataOn.setOnClickListener {
            dismiss(skipExitLoop = true)
            try {
                mActivity.startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(mActivity, "It cannot open settings!", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(mActivity, "It cannot open settings!", Toast.LENGTH_LONG).show()
            }
        }
        mDialogNetworkBinding.close.setOnClickListener {
            dismiss()
        }
    }

    var isExitFromLoop = false
    fun show() {
        networkDialog?.let {
            if (!mActivity.isFinishing && !it.isShowing) {
                it.setOnCancelListener {
                    forceShowAgain()
//                    AppOpenManagerNew.mIsShowingAnyInterruption = false
                }
                it.setOnShowListener {
//                    AppOpenManagerNew.mIsShowingAnyInterruption = true
                }
                it.setOnDismissListener {
                    forceShowAgain()
//                    AppOpenManagerNew.mIsShowingAnyInterruption = false
                }
                it.show()
            }
        }
    }

    private fun forceShowAgain() {
        if (!isExitFromLoop) {
            isExitFromLoop = true
            Handler(Looper.getMainLooper()).postDelayed({ callback.invoke() }, 3000L)
        }
    }

    fun dismiss(skipExitLoop: Boolean = false) {
        networkDialog?.let {
            if (!mActivity.isFinishing && it.isShowing) {
                if (skipExitLoop) {
                    isExitFromLoop = true
                } else {
                    isExitFromLoop = false
                }
                it.dismiss()
            }
        }
    }

}