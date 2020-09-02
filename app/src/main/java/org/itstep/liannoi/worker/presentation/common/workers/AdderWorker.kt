package org.itstep.liannoi.worker.presentation.common.workers

import android.content.Context
import androidx.work.Data
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import io.reactivex.Single
import org.itstep.liannoi.worker.presentation.PresentationDefaults

class AdderWorker(
    context: Context,
    params: WorkerParameters
) : RxWorker(context, params) {

    override fun createWork(): Single<Result> =
        Single.fromCallable { inputData.getIntArray(PresentationDefaults.WORKER_VALUES)!! }
            .map { it.sum() }
            .map {
                val data: Data = Data.Builder()
                    .putInt(PresentationDefaults.WORKER_ADDER, it)
                    .build()

                Result.success(data)
            }
}
