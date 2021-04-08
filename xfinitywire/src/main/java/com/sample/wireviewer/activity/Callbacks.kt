package com.sample.wireviewer.activity

import android.view.View
import com.sample.wireviewer.meanings.MeaningsListViewModel

interface Callbacks {
    fun fetchActivity(): MainActivity
    fun fetchRootView() : View
    fun fetchmeaningsListViewModel() : MeaningsListViewModel
}