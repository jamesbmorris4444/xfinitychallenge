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


class MainWireActivity : MainActivity(), Callbacks, ServiceCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        TARGET_URL = "https://api.duckduckgo.com/?q=the+wire+characters&format=json"
        BASE = "https://api.duckduckgo.com"
        if (isTablet(this)) {
            Constants.IMAGE_WIDTH = 400
            Constants.IMAGE_HEIGHT = 300
        } else {
            Constants.IMAGE_WIDTH = 800
            Constants.IMAGE_HEIGHT = 600
        }
        Constants.CHARACTERS_TITLE = "The Wire Characters"
        super.onCreate(savedInstanceState)
    }

    private fun isTablet(context: Context): Boolean {
        return context.resources.getBoolean(R.bool.isTablet)
    }
}