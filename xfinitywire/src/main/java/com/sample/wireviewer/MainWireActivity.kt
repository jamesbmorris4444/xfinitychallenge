package com.sample.wireviewer

import android.content.Context
import android.os.Bundle
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.activity.MainActivity
import com.sample.commonlibrary.activity.R
import com.sample.commonlibrary.services.ServiceCallbacks
import com.sample.commonlibrary.utils.Constants
import com.sample.commonlibrary.utils.Constants.BASE
import com.sample.commonlibrary.utils.Constants.TARGET_URL
import com.sample.commonlibrary.utils.Utils


class MainWireActivity : MainActivity(), Callbacks, ServiceCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        TARGET_URL = "https://api.duckduckgo.com/?q=the+wire+characters&format=json"
        BASE = "https://api.duckduckgo.com"
        val deviceWidth = resources.displayMetrics.widthPixels
        if (Utils.isTablet(this)) {
            val width = kotlin.math.min((2.0f * deviceWidth / 3.0f).toInt(), 800)
            Constants.IMAGE_WIDTH = width
            Constants.IMAGE_HEIGHT = (width / 1.618f).toInt()
        } else {
            val width = kotlin.math.min((2.0f * deviceWidth / 3.0f).toInt(), 800)
            Constants.IMAGE_WIDTH = width
            Constants.IMAGE_HEIGHT = (width / 1.618f).toInt()
        }
        Constants.CHARACTERS_TITLE = "The Wire Characters"
        super.onCreate(savedInstanceState)
    }

    private fun isTablet(context: Context): Boolean {
        return context.resources.getBoolean(R.bool.isTablet)
    }
}