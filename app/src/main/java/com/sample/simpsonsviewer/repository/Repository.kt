package com.sample.simpsonsviewer.repository
import com.sample.commonlibrary.R
import com.sample.commonlibrary.logger.LogUtils
import com.sample.simpsonsviewer.activity.Callbacks
import com.sample.simpsonsviewer.activity.MainActivity
import com.sample.simpsonsviewer.modal.StandardModal
import com.sample.simpsonsviewer.repository.network.APIClient
import com.sample.simpsonsviewer.repository.network.APIInterface
import com.sample.simpsonsviewer.repository.storage.Meaning
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Repository(private val callbacks: Callbacks) {

    private val meaningsService: APIInterface = APIClient.client

    fun getUrbanDictionaryMeanings(term: String, showMeanings: (meaningsList: List<Meaning>) -> Unit) {
        var disposable: Disposable? = null
        disposable = meaningsService.getMeanings(term, 100, 1, "asc")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe ({ meaningsResponse ->
                disposable?.dispose()
                showMeanings(meaningsResponse)
            },
            { throwable ->
                disposable?.dispose()
                showMeanings(listOf())
                getUrbanDictionaryMeaningsFailure(callbacks.fetchActivity(),"getUrbanDictionaryMeanings", throwable)
            })
    }

    private fun getUrbanDictionaryMeaningsFailure(activity: MainActivity, method: String, throwable: Throwable) {
        LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.EXC), method, throwable)
        StandardModal(
            activity,
            modalType = StandardModal.ModalType.STANDARD,
            titleText = activity.getString(R.string.std_modal_urban_dictionary_failure_title),
            bodyText = activity.getString(R.string.std_modal_urban_dictionary_failure_body),
            positiveText = activity.getString(R.string.std_modal_ok),
            dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                override fun onPositive(string: String) { }
                override fun onNegative() { }
                override fun onNeutral() { }
                override fun onBackPressed() { }
            }
        ).show(activity.supportFragmentManager, "MODAL")
    }

}