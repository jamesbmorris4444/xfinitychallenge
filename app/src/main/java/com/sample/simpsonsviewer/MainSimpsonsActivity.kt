package com.sample.simpsonsviewer

import android.content.Context
import android.os.Bundle
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.activity.MainActivity
import com.sample.commonlibrary.activity.R
import com.sample.commonlibrary.services.ServiceCallbacks
import com.sample.commonlibrary.utils.Constants.BASE
import com.sample.commonlibrary.utils.Constants.CHARACTERS_TITLE
import com.sample.commonlibrary.utils.Constants.IMAGE_HEIGHT
import com.sample.commonlibrary.utils.Constants.IMAGE_WIDTH
import com.sample.commonlibrary.utils.Constants.TARGET_URL


class MainSimpsonsActivity : MainActivity(), Callbacks, ServiceCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        TARGET_URL = "https://api.duckduckgo.com/?q=simpsons+characters&format=json"
        BASE = "https://api.duckduckgo.com"
        if (isTablet(this)) {
            IMAGE_WIDTH = 200
            IMAGE_HEIGHT = 300
        } else {
            IMAGE_WIDTH = 400
            IMAGE_HEIGHT = 600
        }
        CHARACTERS_TITLE = "Simpsons Characters"
        super.onCreate(savedInstanceState)
    }

    private fun isTablet(context: Context): Boolean {
        return context.resources.getBoolean(R.bool.isTablet)
    }
}