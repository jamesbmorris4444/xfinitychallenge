package com.sample.wireviewer.meanings

import android.view.View
import androidx.databinding.ObservableField
import com.sample.commonlibrary.recyclerview.RecyclerViewItemViewModel
import com.sample.commonlibrary.repository.storage.Meaning
import com.sample.wireviewer.activity.Callbacks


class MeaningsItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<Meaning>() {

    val name: ObservableField<String> = ObservableField("")
    val stargazersCount: ObservableField<String> = ObservableField("")
    val htmlUrl: ObservableField<String> = ObservableField("")

    override fun setItem(item: Meaning) {
        name.set("Name: ${item.name}")
        stargazersCount.set("Stars: ${item.stargazersCount}")
        htmlUrl.set(item.htmlUrl)
    }

    fun onUrlClicked(view: View) {
        htmlUrl.get()?.let {
            callbacks.fetchmeaningsListViewModel().onUrlClicked(view, it)
        }
    }

}
