package com.sample.simpsonsviewer.activity

import android.view.View
import com.sample.simpsonsviewer.meanings.MeaningsListViewModel

interface Callbacks {
    fun fetchActivity(): MainActivity
    fun fetchRootView() : View
    fun fetchmeaningsListViewModel() : MeaningsListViewModel
}