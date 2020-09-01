package org.itstep.liannoi.worker.presentation.common.workers

import android.content.Context
import androidx.work.Data
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import io.reactivex.Observable
import io.reactivex.Single
import org.itstep.liannoi.worker.presentation.PresentationDefaults

class ValuesWorker(
    context: Context,
    params: WorkerParameters
) : RxWorker(context, params) {

    override fun createWork(): Single<Result> =
        Observable.fromCallable { (0..40).random() }
            .repeat()
            .take(20)
            .toList()
            .map {
                val data: Data = Data.Builder()
                    .putIntArray(PresentationDefaults.WORKER_VALUES, it.toIntArray())
                    .build()

                Result.success(data)
            }
}
