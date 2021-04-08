package com.sample.wireviewer.services

interface ServiceCallbacks {
    fun setServiceProgress(progress: Int)
    fun setProgressMaxValue(maxValue: Int)
}