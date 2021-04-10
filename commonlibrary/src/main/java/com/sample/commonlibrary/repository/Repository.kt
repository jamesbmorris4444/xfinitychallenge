package com.sample.commonlibrary.repository
import com.sample.commonlibrary.activity.Callbacks
import com.sample.commonlibrary.activity.MainActivity
import com.sample.commonlibrary.activity.R
import com.sample.commonlibrary.logger.LogUtils
import com.sample.commonlibrary.modal.StandardModal
import com.sample.commonlibrary.repository.network.APIClient
import com.sample.commonlibrary.repository.network.APIInterface
import com.sample.commonlibrary.repository.storage.Character
import com.sample.commonlibrary.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.util.concurrent.TimeUnit


class Repository(private val callbacks: Callbacks) {

    private val charactersService: APIInterface = APIClient.client

    fun getCharacters(showCharacters: (characters: List<Character>) -> Unit) {
        var disposable: Disposable? = null
        val url = Constants.TARGET_URL.toHttpUrlOrNull()
        url?.let {
            disposable = charactersService.getCharacters(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .timeout(15L, TimeUnit.SECONDS)
                .subscribe({ header ->
                    disposable?.dispose()
                    showCharacters(header.relatedTopics)
                },
                { throwable ->
                    disposable?.dispose()
                    showCharacters(listOf())
                    getCharactersFailure(callbacks.fetchActivity(),"getCharacters", throwable)
                })
        }
    }

    private fun getCharactersFailure(
        activity: MainActivity,
        method: String,
        throwable: Throwable
    ) {
        LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.EXC), method, throwable)
        StandardModal(
            activity,
            modalType = StandardModal.ModalType.STANDARD,
            titleText = activity.getString(R.string.std_modal_characters_failure_title),
            bodyText = activity.getString(R.string.std_modal_characters_failure_body),
            positiveText = activity.getString(R.string.std_modal_ok),
            dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                override fun onPositive(string: String) {}
                override fun onNegative() {}
                override fun onNeutral() {}
                override fun onBackPressed() {}
            }
        ).show(activity.supportFragmentManager, "MODAL")
    }

}