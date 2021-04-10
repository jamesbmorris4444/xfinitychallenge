package com.sample.commonlibrary.activity

import android.view.View
import com.sample.commonlibrary.characters.CharactersListViewModel
import com.sample.commonlibrary.individual.IndividualViewModel

interface Callbacks {
    fun fetchActivity(): MainActivity
    fun fetchRootView() : View
    fun fetchCharactersListViewModel() : CharactersListViewModel
    fun fetchIndividualViewModel() : IndividualViewModel
}