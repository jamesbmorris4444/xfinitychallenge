package com.sample.commonlibrary.activity

import android.view.View
import com.sample.commonlibrary.individual.IndividualViewModel
import com.sample.commonlibrary.meanings.MeaningsListViewModel

interface Callbacks {
    fun fetchActivity(): MainActivity
    fun fetchRootView() : View
    fun fetchMeaningsListViewModel() : MeaningsListViewModel
    fun fetchIndividualViewModel() : IndividualViewModel
}