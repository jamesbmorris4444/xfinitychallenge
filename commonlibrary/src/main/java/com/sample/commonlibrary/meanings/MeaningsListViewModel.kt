package com.sample.commonlibrary.meanings

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.activity.R
import com.sample.commonlibrary.repository.Repository
import com.sample.commonlibrary.repository.storage.Meaning
import com.sample.commonlibrary.ui.UIViewModel
import com.sample.commonlibrary.utils.Constants
import com.sample.commonlibrary.utils.DaggerViewModelDependencyInjector
import com.sample.commonlibrary.utils.Utils
import com.sample.commonlibrary.utils.ViewModelInjectorModule
import com.spample.commonlibrary.recyclerview.RecyclerViewViewModel
import javax.inject.Inject

class MeaningsListViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MeaningsListViewModel(callbacks) as T
    }
}

class MeaningsListViewModel(private val callbacks: Callbacks) : RecyclerViewViewModel(callbacks.fetchActivity().application) {

    override var adapter: MeaningsAdapter = MeaningsAdapter(callbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    val listIsVisible: ObservableField<Int> = ObservableField(View.VISIBLE)
    val errorIsVisible: ObservableField<Int> = ObservableField(View.GONE)
    val submitVisible: ObservableField<Int> = ObservableField(View.GONE)
    val searchVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    val webVisibility: ObservableField<Int> = ObservableField(View.GONE)
    private lateinit var allMeanings: List<Meaning>

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

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(getApplication<Application>().applicationContext) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return true
            }
        }
    }

    fun initialize(view: View) {
        val textInputLayout: TextInputLayout = view.findViewById(R.id.edit_text_input_name)
        val textInputEditText: TextInputEditText = view.findViewById(R.id.edit_text_input_name_editText)
        textInputLayout.setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        textInputEditText.requestFocus()
        Utils.showKeyboard(textInputEditText)
    }

    // observable used for two-way donations binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextNameInput: ObservableField<String> = ObservableField("")
    fun onTextNameChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        adapter.filter.filter(key)
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextName: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.meanings_hint_text))
    var editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onSearchClicked(view: View) {
        val enteredText: String = editTextNameInput.get() ?: ""
        if (enteredText == "s") {
            callbacks.fetchActivity().startPretendLongRunningTask()
        } else if (enteredText == "p") {
            callbacks.fetchActivity().pausePretendLongRunningTask()
        } else if (enteredText == "r") {
            callbacks.fetchActivity().resumePretendLongRunningTask()
        } else {
            Utils.hideKeyboard(view)
            val progressBar = callbacks.fetchActivity().getMainProgressBar()
            progressBar.visibility = View.VISIBLE
            repository.getUrbanDictionaryMeanings(this::showMeanings)
        }
    }

    private fun showMeanings(meanings: List<Meaning>) {
        val progressBar = callbacks.fetchActivity().getMainProgressBar()
        progressBar.visibility = View.GONE
        if (meanings.isEmpty()) {
            listIsVisible.set(View.GONE)
            errorIsVisible.set(View.VISIBLE)
        } else {
            listIsVisible.set(View.VISIBLE)
            errorIsVisible.set(View.GONE)
//            val fullList = meanings.sortedByDescending { meaning -> Utils.stargazerComparison(meaning) }
//            val smallList: MutableList<Meaning> = mutableListOf()
//            for (index in 0 until kotlin.math.min(fullList.size, 3)) {
//                smallList.add(fullList[index])
//            }
            adapter.addAll(meanings)
            submitVisible.set(View.GONE)
        }
        allMeanings = meanings
    }
    fun onElementClicked(view: View) {
        val position = view.tag as Int
        callbacks.fetchActivity().loadIndividualFragment(basename(allMeanings[position].firstUrl), "${Constants.BASE}${allMeanings[position].icon.url}", strip(allMeanings[position].result))
    }

    private fun basename(path: String): String {
        val last = path.lastIndexOf('/')
        return path.substring(last + 1)
    }

    private fun strip(text: String): String {
        val last = text.lastIndexOf('>')
        return text.substring(last + 1)
    }

    fun backPressed() {
        callbacks.fetchActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}