package com.sample.simpsonsviewer.utils

import androidx.lifecycle.ViewModelProvider
import com.sample.commonlibrary.ui.UIViewModel
import com.sample.commonlibrary.ui.UIViewModelFactory
import com.sample.simpsonsviewer.activity.MainActivity
import com.sample.simpsonsviewer.meanings.MeaningsAdapter
import com.sample.simpsonsviewer.meanings.MeaningsFragment
import com.sample.simpsonsviewer.meanings.MeaningsListViewModel
import com.sample.simpsonsviewer.modal.StandardModal
import com.sample.simpsonsviewer.repository.Repository


import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [MapperInjectorModule::class])
interface MapperDependencyInjector {
    fun inject(viewModel: UIViewModel)
}

@Singleton
@Component(modules = [ViewModelInjectorModule::class])
interface ViewModelDependencyInjector {
    fun inject(fragment: MeaningsFragment)
    fun inject(modal: StandardModal)
    fun inject(viewModel: MeaningsListViewModel)
    fun inject(activity: MainActivity)
    fun inject(adapter: MeaningsAdapter)
}

@Module
class MapperInjectorModule {
    @Provides
    @Singleton
    fun colorMapperProvider() : ColorMapper {
        val colorMapper = ColorMapper()
        colorMapper.initialize()
        return colorMapper
    }
    @Provides
    @Singleton
    fun textSizeMapperProvider() : TextSizeMapper {
        val textSizeMapper = TextSizeMapper()
        textSizeMapper.initialize()
        return textSizeMapper
    }
    @Provides
    @Singleton
    fun typefaceMapperProvider() : TypefaceMapper {
        val typefaceMapper = TypefaceMapper()
        typefaceMapper.initialize()
        return typefaceMapper
    }
}

@Module
class ViewModelInjectorModule(val activity: MainActivity) {
    @Provides
    @Singleton
    fun uiViewModelProvider() : UIViewModel {
        return ViewModelProvider(activity, UIViewModelFactory(activity.application)).get(UIViewModel::class.java)
    }
    @Provides
    @Singleton
    fun repositoryProvider() : Repository {
        return activity.repository
    }

}