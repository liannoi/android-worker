package org.itstep.liannoi.worker.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.paulinasadowska.rxworkmanagerobservers.extensions.getWorkDataByIdSingle
import org.itstep.liannoi.worker.R
import org.itstep.liannoi.worker.presentation.common.workers.ValuesWorker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val request: OneTimeWorkRequest = OneTimeWorkRequestBuilder<ValuesWorker>().build()
        WorkManager.getInstance(this).enqueue(request)

        var data: Data

        WorkManager.getInstance(this)
            .getWorkDataByIdSingle(request.id)
            .subscribe({
                data = it
                Log.d("Hello", "onCreate: " + data)
            }, {})
    }
}
