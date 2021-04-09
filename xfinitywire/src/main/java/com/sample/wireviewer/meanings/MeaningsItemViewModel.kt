package com.sample.wireviewer.meanings

import android.view.View
import androidx.databinding.ObservableField
import com.sample.commonlibrary.recyclerview.RecyclerViewItemViewModel
import com.sample.wireviewer.activity.Callbacks
import com.sample.wireviewer.repository.storage.Meaning


class MeaningsItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<Meaning>() {

    val firstUrl: ObservableField<String> = ObservableField("")
    val icon: ObservableField<String> = ObservableField("")
    val result: ObservableField<String> = ObservableField("")

    override fun setItem(item: Meaning) {
        firstUrl.set("FirstUrl: ${item.firstUrl}")
        icon.set("Icon: ${item.icon.url}")
        result.set(item.result)
    }

    fun onUrlClicked(view: View) {
        result.get()?.let {
            callbacks.fetchmeaningsListViewModel().onUrlClicked(view, it)
        }
    }

}
