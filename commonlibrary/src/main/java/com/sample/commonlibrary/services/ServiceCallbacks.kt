package com.sample.commonlibrary.services

interface ServiceCallbacks {
    fun setServiceProgress(progress: Int)
    fun setProgressMaxValue(maxValue: Int)
}