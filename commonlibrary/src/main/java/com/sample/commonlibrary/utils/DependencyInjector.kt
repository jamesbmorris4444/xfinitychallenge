package com.sample.commonlibrary.utils


import androidx.lifecycle.ViewModelProvider
import com.sample.commonlibrary.activity.MainActivity
import com.sample.commonlibrary.individual.IndividualFragment
import com.sample.commonlibrary.individual.IndividualViewModel
import com.sample.commonlibrary.meanings.MeaningsAdapter
import com.sample.commonlibrary.meanings.MeaningsFragment
import com.sample.commonlibrary.meanings.MeaningsListViewModel
import com.sample.commonlibrary.modal.StandardModal
import com.sample.commonlibrary.repository.Repository
import com.sample.commonlibrary.ui.UIViewModel
import com.sample.commonlibrary.ui.UIViewModelFactory
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
    fun inject(fragment: IndividualFragment)
    fun inject(viewModel: IndividualViewModel)
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