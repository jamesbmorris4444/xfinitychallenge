package com.sample.commonlibrary.characters

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
import com.sample.commonlibrary.repository.storage.Character
import com.sample.commonlibrary.ui.UIViewModel
import com.sample.commonlibrary.utils.Constants
import com.sample.commonlibrary.utils.DaggerViewModelDependencyInjector
import com.sample.commonlibrary.utils.Utils
import com.sample.commonlibrary.utils.ViewModelInjectorModule
import com.spample.commonlibrary.recyclerview.RecyclerViewViewModel
import javax.inject.Inject

class CharactersListViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharactersListViewModel(callbacks) as T
    }
}

class CharactersListViewModel(private val callbacks: Callbacks) : RecyclerViewViewModel(callbacks.fetchActivity().application) {

    override var adapter: CharactersAdapter = CharactersAdapter(callbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    val listIsVisible: ObservableField<Int> = ObservableField(View.VISIBLE)
    val errorIsVisible: ObservableField<Int> = ObservableField(View.GONE)
    val submitVisible: ObservableField<Int> = ObservableField(View.GONE)
    val searchVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    val webVisibility: ObservableField<Int> = ObservableField(View.GONE)
    private lateinit var allCharacters: List<Character>

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
        //textInputEditText.requestFocus()
        Utils.showKeyboard(textInputEditText)
    }

    // observable used for two-way donations binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextNameInput: ObservableField<String> = ObservableField("")
    fun onTextNameChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        adapter.filter.filter(key)
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextName: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.characters_hint_text))
    var editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onLoadDataClicked(view: View) {
        editTextNameInput.set("")
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
            repository.getCharacters(this::showCharacters)
        }
    }

    private fun showCharacters(characters: List<Character>) {
        val progressBar = callbacks.fetchActivity().getMainProgressBar()
        progressBar.visibility = View.GONE
        if (characters.isEmpty()) {
            listIsVisible.set(View.GONE)
            errorIsVisible.set(View.VISIBLE)
        } else {
            listIsVisible.set(View.VISIBLE)
            errorIsVisible.set(View.GONE)
            adapter.addAll(characters)
            submitVisible.set(View.GONE)
        }
        allCharacters = characters
    }
    fun onElementClicked(view: View) {
        Utils.hideKeyboard(view)
        val character = view.tag as Character
        callbacks.fetchActivity().loadIndividualFragment(basename(character.firstUrl), "${Constants.BASE}${character.icon.url}", strip(character.result))
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
        if (Utils.isTablet(callbacks.fetchActivity())) {
            callbacks.fetchActivity().onBackPressed()
        } else {
            callbacks.fetchActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

}