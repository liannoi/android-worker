package org.itstep.liannoi.worker.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.paulinasadowska.rxworkmanagerobservers.extensions.getWorkDataByIdSingle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.itstep.liannoi.worker.presentation.PresentationDefaults
import org.itstep.liannoi.worker.presentation.common.workers.ValuesWorker

class MainViewModel constructor(
    private val workManager: WorkManager
) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    init {
        generateValues()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Helpers
    ///////////////////////////////////////////////////////////////////////////

    private fun generateValues() {
        val request: OneTimeWorkRequest = OneTimeWorkRequestBuilder<ValuesWorker>().build()
        workManager.enqueue(request)

        workManager.getWorkDataByIdSingle(request.id)
            .subscribe({
                val array: IntArray = it.getIntArray(PresentationDefaults.WORKER_VALUES)!!
                Log.d(TAG, "onCreate: ${array.contentToString()}")
            }, {})
            .follow()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Disposable
    ///////////////////////////////////////////////////////////////////////////

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        disposable.dispose()
    }

    private fun Disposable.follow() {
        disposable.add(this)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Companion object
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        private const val TAG: String = "org.itstep.liannoi.worker.presentation.main.MainViewModel"
    }
}
