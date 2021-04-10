package com.sample.wireviewer

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.activity.MainActivity
import com.sample.commonlibrary.services.ServiceCallbacks
import com.sample.commonlibrary.utils.Constants
import com.sample.commonlibrary.utils.Constants.BASE
import com.sample.commonlibrary.utils.Constants.TARGET_URL
import java.util.*


class MainWireActivity : MainActivity(), Callbacks, ServiceCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        TARGET_URL = "https://api.duckduckgo.com/?q=the+wire+characters&format=json"
        BASE = "https://api.duckduckgo.com"
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Objects.requireNonNull(manager).phoneType == TelephonyManager.PHONE_TYPE_NONE) {
            Constants.IMAGE_WIDTH = 400
            Constants.IMAGE_HEIGHT = 300
        } else {
            Constants.IMAGE_WIDTH = 800
            Constants.IMAGE_HEIGHT = 600
        }
        Constants.CHARACTERS_TITLE = "The Wire Characters"
        super.onCreate(savedInstanceState)
    }
}