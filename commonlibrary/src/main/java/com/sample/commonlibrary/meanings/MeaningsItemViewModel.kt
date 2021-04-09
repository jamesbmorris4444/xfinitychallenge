package com.sample.commonlibrary.meanings

import android.view.View
import androidx.databinding.ObservableField
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.recyclerview.RecyclerViewItemViewModel
import com.sample.commonlibrary.repository.storage.Meaning


class MeaningsItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<Meaning>() {

    val firstUrl: ObservableField<String> = ObservableField("")
    val result: ObservableField<String> = ObservableField("")

    override fun setItem(item: Meaning) {
        firstUrl.set("Name: ${basename(item.firstUrl)}")
        result.set(strip(item.result))
    }

    private fun basename(path: String): String {
        val last = path.lastIndexOf('/')
        return path.substring(last + 1)
    }

    private fun strip(text: String): String {
        val last = text.lastIndexOf('>')
        return text.substring(last + 1)
    }

    fun onElementClicked(view: View) {
        callbacks.fetchMeaningsListViewModel().onElementClicked(view)
    }

}
