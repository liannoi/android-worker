package org.itstep.liannoi.worker.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.paulinasadowska.rxworkmanagerobservers.extensions.getWorkDataByIdSingle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.itstep.liannoi.worker.presentation.PresentationDefaults
import org.itstep.liannoi.worker.presentation.common.workers.AdderWorker
import org.itstep.liannoi.worker.presentation.common.workers.AverageWorker
import org.itstep.liannoi.worker.presentation.common.workers.ValuesWorker

class MainViewModel constructor(
    private val workManager: WorkManager
) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    init {
        sendRequests()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Helpers
    ///////////////////////////////////////////////////////////////////////////

    private fun sendRequests() {
        val valuesRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<ValuesWorker>().build()
        val adderRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<AdderWorker>().build()
        val averageRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<AverageWorker>().build()

        workManager.beginWith(valuesRequest)
            .then(mutableListOf(adderRequest, averageRequest))
            .enqueue()

        handleValuesRequest(valuesRequest)
        handleAdderRequest(adderRequest)
        handleAverageRequest(averageRequest)
    }

    private fun handleAverageRequest(request: WorkRequest) {
        request.handle {
            val average: Int = it.getInt(PresentationDefaults.WORKER_AVERAGE, 0)
            Log.d(TAG, "averageRequest: $average")
        }
    }

    private fun handleAdderRequest(request: WorkRequest) {
        request.handle {
            val sum: Int = it.getInt(PresentationDefaults.WORKER_ADDER, 0)
            Log.d(TAG, "adderRequest: $sum")
        }
    }

    private fun handleValuesRequest(request: WorkRequest) {
        request.handle {
            val array: IntArray = it.getIntArray(PresentationDefaults.WORKER_VALUES)!!
            Log.d(TAG, "valuesRequest: ${array.contentToString()}")
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Local extension methods
    ///////////////////////////////////////////////////////////////////////////

    private fun WorkRequest.handle(function: (t: Data) -> Unit) {
        workManager.getWorkDataByIdSingle(this.id)
            .subscribe(function, {})
            .follow()
    }

    private fun Disposable.follow() {
        disposable.add(this)
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
