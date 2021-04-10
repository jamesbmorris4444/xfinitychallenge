package com.sample.commonlibrary.characters

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
import com.sample.commonlibrary.activity.databinding.CharactersFragmentBinding
import com.sample.commonlibrary.individual.IndividualViewModel
import com.sample.commonlibrary.ui.UIViewModel
import com.sample.commonlibrary.utils.Constants
import com.sample.commonlibrary.utils.DaggerViewModelDependencyInjector
import com.sample.commonlibrary.utils.Utils
import com.sample.commonlibrary.utils.ViewModelInjectorModule
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class CharactersFragment : Fragment(), Callbacks {

    lateinit var charactersListViewModel: CharactersListViewModel
    private lateinit var binding: CharactersFragmentBinding
    private lateinit var mainActivity: MainActivity

    companion object {
        fun newInstance(): CharactersFragment {
            return CharactersFragment()
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
        charactersListViewModel.initialize(binding.root)
        Utils.hideKeyboard(fetchRootView())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.characters_fragment, container, false) as CharactersFragmentBinding
        binding.lifecycleOwner = this
        charactersListViewModel = ViewModelProvider(this, CharactersListViewModelFactory(this)).get(CharactersListViewModel::class.java)
        binding.charactersListViewModel = charactersListViewModel
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

    override fun fetchCharactersListViewModel() : CharactersListViewModel {
        return charactersListViewModel
    }

    override fun fetchIndividualViewModel() : IndividualViewModel {
        return IndividualViewModel(this)
    }

}