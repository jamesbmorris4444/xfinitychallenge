package com.sample.wireviewer.utils

import com.sample.wireviewer.repository.storage.Meaning

object Constants {

    const val URBANDICT_ARRAY_DATA_TAG = "list"
    const val ROOT_FRAGMENT_TAG = "root fragment"
    val URBANDICT_LIST_CLASS_TYPE = (ArrayList<Meaning>()).javaClass
    const val URBANDICT_BASE_URL = "https://api.github.com/"
    const val URBANDICT_RAPID_API_HOST = "x-rapidapi-host: mashape-community-urban-dictionary.p.rapidapi.com"
    const val URBANDICT_RAPID_API_KEY = "x-rapidapi-key: f674a66b81msh663e5810b0cdd7ep162635jsnb30c4b837dd9"
    const val URBANDICT_TERM = "user"
    const val STANDARD_LEFT_AND_RIGHT_MARGIN = 20f
    const val STANDARD_EDIT_TEXT_SMALL_MARGIN = 10f
    const val STANDARD_EDIT_TEXT_HEIGHT = 60f
    const val STANDARD_GRID_EDIT_TEXT_HEIGHT = 60f
    const val STANDARD_BUTTON_HEIGHT = 50f
    const val STANDARD_GRID_HEIGHT = 120f
    const val EDIT_TEXT_TO_BUTTON_RATIO = 3  // 3:1
    const val URBANDICT_TITLE = "Urban Dictionary Results"
    const val PER_PAGE = "per_page"
    const val PAGE = "page"
    const val DIRECTION = "direction"
}