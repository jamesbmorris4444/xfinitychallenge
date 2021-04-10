package com.sample.commonlibrary.characters

import android.view.View
import androidx.databinding.ObservableField
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.recyclerview.RecyclerViewItemViewModel
import com.sample.commonlibrary.repository.storage.Character


class CharactersItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<Character>() {

    val firstUrl: ObservableField<String> = ObservableField("")
    val result: ObservableField<String> = ObservableField("")

    override fun setItem(item: Character) {
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
        callbacks.fetchCharactersListViewModel().onElementClicked(view)
    }

}
