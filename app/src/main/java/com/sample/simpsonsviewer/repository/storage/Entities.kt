package com.sample.simpsonsviewer.repository.storage

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Meaning(

    @SerializedName(value = "FirstURL") var firstUrl: String,
    @SerializedName(value = "Icon") var icon: Icon,
    @SerializedName(value = "Result") var result: String

) : Parcelable

@Parcelize
data class Icon(

    @SerializedName(value = "Height") var height: String,
    @SerializedName(value = "URL") var url: String,
    @SerializedName(value = "Width") var width: String

) : Parcelable

@Parcelize
data class Header(

    @SerializedName(value = "Abstract") var abstract: String,
    @SerializedName(value = "AbstractSource") var abstractSource: String,
    @SerializedName(value = "AbstractText") var abstractText: String,
    @SerializedName(value = "RelatedTopics") var relatedTopics: List<Meaning>

) : Parcelable