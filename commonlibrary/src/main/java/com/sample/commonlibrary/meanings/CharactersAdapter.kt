package com.sample.commonlibrary.characters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.activity.R
import com.sample.commonlibrary.activity.databinding.CharactersListItemBinding
import com.sample.commonlibrary.recyclerview.RecyclerViewFilterAdapter
import com.sample.commonlibrary.repository.storage.Character
import com.sample.commonlibrary.ui.UIViewModel
import com.sample.commonlibrary.utils.DaggerViewModelDependencyInjector
import com.sample.commonlibrary.utils.ViewModelInjectorModule
import kotlinx.android.synthetic.main.characters_list_item.view.*
import javax.inject.Inject


class CharactersAdapter(private val callbacks: Callbacks) : RecyclerViewFilterAdapter<Character, CharactersItemViewModel>() {

    private var adapterFilter: AdapterFilter? = null

    @Inject
    lateinit var uiViewModel: UIViewModel

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
            .build()
            .inject(this)
    }

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val charactersListItemBinding: CharactersListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.characters_list_item, parent, false)
        val charactersItemViewModel = CharactersItemViewModel(callbacks)
        charactersListItemBinding.charactersItemViewModel = charactersItemViewModel
        charactersListItemBinding.uiViewModel = uiViewModel
        return CharactersViewHolder(charactersListItemBinding.root, charactersItemViewModel, charactersListItemBinding)
    }

    inner class CharactersViewHolder internal constructor(itemView: View, viewModel: CharactersItemViewModel, viewDataBinding: CharactersListItemBinding) :
        ItemViewHolder<Character, CharactersItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Character, CharactersItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        (holder.itemView.characters_item_root_view as ConstraintLayout).tag = position
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor1))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor2))
        }
    }

    override fun itemFilterable(character: Character, key: String): Boolean {
        val name = basename(character.firstUrl)
        return name.contains(key, true)
    }

    private fun basename(path: String): String {
        val last = path.lastIndexOf('/')
        return path.substring(last + 1)
    }

}