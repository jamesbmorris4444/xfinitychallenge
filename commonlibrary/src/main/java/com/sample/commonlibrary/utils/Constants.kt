package com.sample.commonlibrary.utils

import com.sample.commonlibrary.repository.storage.Character

object Constants {

    lateinit var TARGET_URL: String
    lateinit var BASE: String
    var IMAGE_WIDTH = 0
    var IMAGE_HEIGHT = 0
    var CHARACTERS_TITLE = ""

    const val CHARACTERS_DATA_TAG = "list"
    const val ROOT_FRAGMENT_TAG = "root fragment"
    val CHARACTERS_CLASS_TYPE = (ArrayList<Character>()).javaClass
    const val STANDARD_LEFT_AND_RIGHT_MARGIN = 20f
    const val STANDARD_EDIT_TEXT_SMALL_MARGIN = 10f
    const val STANDARD_EDIT_TEXT_HEIGHT = 60f
    const val STANDARD_GRID_EDIT_TEXT_HEIGHT = 60f
    const val STANDARD_BUTTON_HEIGHT = 50f
    const val STANDARD_GRID_HEIGHT = 120f
    const val EDIT_TEXT_TO_BUTTON_RATIO = 3  // 3:1
}