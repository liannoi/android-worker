package org.itstep.liannoi.worker.presentation.common.workers

import android.content.Context
import androidx.work.Data
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import io.reactivex.Observable
import io.reactivex.Single

class ValuesWorker(
    context: Context,
    params: WorkerParameters
) : RxWorker(context, params) {

    override fun createWork(): Single<Result> =
        Observable.fromCallable { (0..40).random() }
            .repeat()
            .take(20)
            .toList()
            .map { Result.success(Data.Builder().putIntArray("KEY", it.toIntArray()).build()) }
}
