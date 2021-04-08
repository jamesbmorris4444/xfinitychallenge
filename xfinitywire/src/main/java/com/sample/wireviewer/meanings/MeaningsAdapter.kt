package com.sample.wireviewer.meanings

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sample.commonlibrary.recyclerview.RecyclerViewFilterAdapter
import com.sample.commonlibrary.ui.UIViewModel
import com.sample.wireviewer.activity.Callbacks
import com.sample.wireviewer.activity.R
import com.sample.wireviewer.activity.databinding.MeaningsListItemBinding
import com.sample.wireviewer.repository.storage.Meaning
import com.sample.wireviewer.utils.DaggerViewModelDependencyInjector
import com.sample.wireviewer.utils.ViewModelInjectorModule
import javax.inject.Inject


class MeaningsAdapter(private val callbacks: Callbacks) : RecyclerViewFilterAdapter<Meaning, MeaningsItemViewModel>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningsViewHolder {
        val meaningsListItemBinding: MeaningsListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.meanings_list_item, parent, false)
        val meaningsItemViewModel = MeaningsItemViewModel(callbacks)
        meaningsListItemBinding.meaningsItemViewModel = meaningsItemViewModel
        meaningsListItemBinding.uiViewModel = uiViewModel
        return MeaningsViewHolder(meaningsListItemBinding.root, meaningsItemViewModel, meaningsListItemBinding)
    }

    inner class MeaningsViewHolder internal constructor(itemView: View, viewModel: MeaningsItemViewModel, viewDataBinding: MeaningsListItemBinding) :
        ItemViewHolder<Meaning, MeaningsItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Meaning, MeaningsItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor1))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor2))
        }
    }

    override fun itemFilterable(item: Meaning, constraint: String): Boolean {
        return true
    }

}