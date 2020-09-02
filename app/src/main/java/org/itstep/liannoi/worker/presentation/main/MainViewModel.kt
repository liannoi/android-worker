package org.itstep.liannoi.worker.presentation.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.paulinasadowska.rxworkmanagerobservers.extensions.getWorkDataByIdSingle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import org.itstep.liannoi.worker.presentation.PresentationDefaults
import org.itstep.liannoi.worker.presentation.common.workers.AdderWorker
import org.itstep.liannoi.worker.presentation.common.workers.AverageWorker
import org.itstep.liannoi.worker.presentation.common.workers.ValuesWorker

class MainViewModel constructor(
    private val workManager: WorkManager
) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    init {
        subscribeOnRequests()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Requests
    ///////////////////////////////////////////////////////////////////////////

    private fun subscribeOnRequests() {
        val valuesRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<ValuesWorker>().build()
        val adderRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<AdderWorker>().build()
        val averageRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<AverageWorker>().build()

        workManager.beginWith(valuesRequest)
            .then(mutableListOf(adderRequest, averageRequest))
            .enqueue()

        subscribeOnValuesRequest(valuesRequest)
        subscribeOnAdderRequest(adderRequest)
        subscribeOnAverageRequest(averageRequest)
    }

    private fun subscribeOnAverageRequest(request: WorkRequest) {
        request.subscribe {
            val average: Int = it.getInt(PresentationDefaults.WORKER_AVERAGE, 0)
            Log.d(TAG, "averageRequest: $average")
        }
    }

    private fun subscribeOnAdderRequest(request: WorkRequest) {
        request.subscribe {
            val sum: Int = it.getInt(PresentationDefaults.WORKER_ADDER, 0)
            Log.d(TAG, "adderRequest: $sum")
        }
    }

    private fun subscribeOnValuesRequest(request: WorkRequest) {
        request.subscribe {
            val array: IntArray = it.getIntArray(PresentationDefaults.WORKER_VALUES)!!
            Log.d(TAG, "valuesRequest: ${array.contentToString()}")
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Local extension methods
    ///////////////////////////////////////////////////////////////////////////

    private fun WorkRequest.subscribe(onSuccess: (t: Data) -> Unit) {
        workManager.getWorkDataByIdSingle(this.id)
            .subscribe(onSuccess, {})
            .addTo(disposable)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Disposable
    ///////////////////////////////////////////////////////////////////////////

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        disposable.dispose()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Companion object
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        private const val TAG: String = "org.itstep.liannoi.worker.presentation.main.MainViewModel"
    }
}
