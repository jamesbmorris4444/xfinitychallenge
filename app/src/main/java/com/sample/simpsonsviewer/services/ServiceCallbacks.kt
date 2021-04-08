package com.sample.simpsonsviewer.services

interface ServiceCallbacks {
    fun setServiceProgress(progress: Int)
    fun setProgressMaxValue(maxValue: Int)
}