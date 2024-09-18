package com.origin.ads.sample.clone

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CloneAdsData {
    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("DATA")
    @Expose
    var data: List<CloneAdsDataItem>? = null

    inner class CloneAdsDataItem {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("app_name")
        @Expose
        var appName: String? = null

        @SerializedName("app_description")
        @Expose
        var appDescription: String? = null

        @SerializedName("app_link")
        @Expose
        var appLink: String? = null

        @SerializedName("app_icon")
        @Expose
        var appIcon: String? = null

        @SerializedName("app_banner")
        @Expose
        var appBanner: String? = null

        @SerializedName("app_screenshot")
        @Expose
        private val appScreenshot: String? = null
    }
}