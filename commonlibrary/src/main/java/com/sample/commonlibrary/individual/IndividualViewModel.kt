package com.sample.commonlibrary.individual

import android.app.Application
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.activity.R
import com.sample.commonlibrary.repository.Repository
import com.sample.commonlibrary.ui.UIViewModel
import com.sample.commonlibrary.utils.Constants
import com.sample.commonlibrary.utils.DaggerViewModelDependencyInjector
import com.sample.commonlibrary.utils.ViewModelInjectorModule
import kotlinx.android.synthetic.main.individual_fragment.view.*
import javax.inject.Inject

class IndividualViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IndividualViewModel(callbacks) as T
    }
}

class IndividualViewModel(private val callbacks: Callbacks) : AndroidViewModel(callbacks.fetchActivity().application) {

    lateinit var name: String
    lateinit var url: String
    lateinit var description: String
    val individualText: ObservableField<String> = ObservableField("")
    val descText: ObservableField<String> = ObservableField("")
    val imageWidth: ObservableField<Int> = ObservableField(Constants.IMAGE_WIDTH)
    val imageHeight: ObservableField<Int> = ObservableField(Constants.IMAGE_HEIGHT)
    val backupImage: ObservableField<Drawable> = ObservableField()
    val imageVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    val backupVisibility: ObservableField<Int> = ObservableField(View.GONE)

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
            .build()
            .inject(this)
    }

    fun initialize(view: View) {
        individualText.set(name)
        descText.set(description)
        if (url == Constants.BASE) {
            imageVisibility.set(View.GONE)
            backupVisibility.set(View.VISIBLE)
            backupImage.set(ContextCompat.getDrawable(getApplication<Application>().applicationContext, R.drawable.butterfly))
        } else {
            imageVisibility.set(View.VISIBLE)
            backupVisibility.set(View.GONE)
            Glide.with(callbacks.fetchActivity()).load(url).into(callbacks.fetchRootView().char_image)
        }
    }
}