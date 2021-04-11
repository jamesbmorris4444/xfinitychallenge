package com.sample.simpsonsviewer

import android.os.Bundle
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.activity.MainActivity
import com.sample.commonlibrary.services.ServiceCallbacks
import com.sample.commonlibrary.utils.Constants.BASE
import com.sample.commonlibrary.utils.Constants.CHARACTERS_TITLE
import com.sample.commonlibrary.utils.Constants.IMAGE_HEIGHT
import com.sample.commonlibrary.utils.Constants.IMAGE_WIDTH
import com.sample.commonlibrary.utils.Constants.TARGET_URL
import com.sample.commonlibrary.utils.Utils


class MainSimpsonsActivity : MainActivity(), Callbacks, ServiceCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        TARGET_URL = "https://api.duckduckgo.com/?q=simpsons+characters&format=json"
        BASE = "https://api.duckduckgo.com"
        val deviceWidth = resources.displayMetrics.widthPixels
        if (Utils.isTablet(this)) {
            val width = kotlin.math.min((2.0f * deviceWidth / 3.0f).toInt(), 300)
            IMAGE_WIDTH = width
            IMAGE_HEIGHT = (width * 1.618f).toInt()
        } else {
            val width = kotlin.math.min((2.0f * deviceWidth / 3.0f).toInt(), 400)
            IMAGE_WIDTH = width
            IMAGE_HEIGHT = (width * 1.618f).toInt()
        }
        CHARACTERS_TITLE = "Simpsons Characters"
        super.onCreate(savedInstanceState)
    }

}