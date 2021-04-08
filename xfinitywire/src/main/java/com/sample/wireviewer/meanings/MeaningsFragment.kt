package com.sample.wireviewer.meanings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sample.commonlibrary.R
import com.sample.commonlibrary.databinding.MeaningsFragmentBinding
import com.sample.commonlibrary.meanings.MeaningsListViewModel
import com.sample.commonlibrary.meanings.MeaningsListViewModelFactory
import com.sample.commonlibrary.ui.UIViewModel
import com.sample.commonlibrary.utils.Constants
import com.sample.commonlibrary.utils.DaggerViewModelDependencyInjector
import com.sample.commonlibrary.utils.ViewModelInjectorModule
import com.sample.wireviewer.activity.Callbacks
import com.sample.wireviewer.activity.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MeaningsFragment : Fragment(), Callbacks {

    lateinit var meaningsListViewModel: MeaningsListViewModel
    private lateinit var binding: MeaningsFragmentBinding
    private lateinit var mainActivity: MainActivity

    companion object {
        fun newInstance(): MeaningsFragment {
            return MeaningsFragment()
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
        (activity as MainActivity).toolbar.title = Constants.URBANDICT_TITLE
        meaningsListViewModel.initialize(binding.root)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.meanings_fragment, container, false) as MeaningsFragmentBinding
        binding.lifecycleOwner = this
        meaningsListViewModel = ViewModelProvider(this, MeaningsListViewModelFactory(this)).get(MeaningsListViewModel::class.java)
        binding.meaningsListViewModel = meaningsListViewModel
        binding.uiViewModel = uiViewModel
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

    override fun fetchmeaningsListViewModel() : MeaningsListViewModel {
        return meaningsListViewModel
    }

}