package com.sample.commonlibrary.individual

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.activity.MainActivity
import com.sample.commonlibrary.activity.R
import com.sample.commonlibrary.activity.databinding.IndividualFragmentBinding
import com.sample.commonlibrary.characters.CharactersListViewModel
import com.sample.commonlibrary.logger.LogUtils
import com.sample.commonlibrary.ui.UIViewModel
import com.sample.commonlibrary.utils.Constants
import com.sample.commonlibrary.utils.DaggerViewModelDependencyInjector
import com.sample.commonlibrary.utils.ViewModelInjectorModule
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class IndividualFragment : Fragment(), Callbacks {

    private lateinit var individualViewModel: IndividualViewModel
    private lateinit var binding: IndividualFragmentBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var name: String
    private lateinit var url: String
    private lateinit var description: String

    companion object {
        fun newInstance(name: String, url: String, description: String): IndividualFragment {
            val fragment = IndividualFragment()
            fragment.name = name
            fragment.url = url
            fragment.description = description
            return fragment
        }
    }

    @Inject
    lateinit var uiViewModel: UIViewModel

    override fun onAttach(context: Context) {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity as MainActivity))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).toolbar.title = Constants.CHARACTERS_TITLE
        individualViewModel.initialize(binding.root)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.individual_fragment, container, false) as IndividualFragmentBinding
        binding.lifecycleOwner = this
        individualViewModel = ViewModelProvider(this, IndividualViewModelFactory(this)).get(IndividualViewModel::class.java)
        binding.individualViewModel = individualViewModel
        binding.uiViewModel = uiViewModel
        individualViewModel.name = name
        individualViewModel.url = url
        LogUtils.D(
            "JIMX",
            LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM),
            String.format("three=%s   %s   %s", name, url, description)
        )
        individualViewModel.description = description

        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    override fun fetchActivity(): MainActivity {
        return if (::mainActivity.isInitialized) {
            mainActivity
        } else {
            activity as MainActivity
        }
    }

    override fun fetchRootView(): View {
        return binding.root
    }

    override fun fetchCharactersListViewModel() : CharactersListViewModel {
        return CharactersListViewModel(this)
    }

    override fun fetchIndividualViewModel() : IndividualViewModel {
        return individualViewModel
    }

}