package com.sample.simpsonsviewer

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.activity.MainActivity
import com.sample.commonlibrary.services.ServiceCallbacks
import com.sample.commonlibrary.utils.Constants.BASE
import com.sample.commonlibrary.utils.Constants.IMAGE_HEIGHT
import com.sample.commonlibrary.utils.Constants.IMAGE_WIDTH
import com.sample.commonlibrary.utils.Constants.TARGET_URL
import java.util.*


class MainSimpsonsActivity : MainActivity(), Callbacks, ServiceCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        TARGET_URL = "https://api.duckduckgo.com/?q=simpsons+characters&format=json"
        BASE = "https://api.duckduckgo.com"
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Objects.requireNonNull(manager).phoneType == TelephonyManager.PHONE_TYPE_NONE) {
            IMAGE_WIDTH = 200
            IMAGE_HEIGHT = 300
        } else {
            IMAGE_WIDTH = 400
            IMAGE_HEIGHT = 600
        }
        super.onCreate(savedInstanceState)
    }
}