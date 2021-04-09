package com.sample.wireviewer

import android.os.Bundle
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.activity.MainActivity
import com.sample.commonlibrary.services.ServiceCallbacks
import com.sample.commonlibrary.utils.Constants.BASE
import com.sample.commonlibrary.utils.Constants.TARGET_URL


class MainWireActivity : MainActivity(), Callbacks, ServiceCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        TARGET_URL = "https://api.duckduckgo.com/?q=the+wire+characters&format=json"
        BASE = "https://api.duckduckgo.com"
        super.onCreate(savedInstanceState)
    }
}