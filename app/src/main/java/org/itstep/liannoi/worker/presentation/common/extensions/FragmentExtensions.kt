package org.itstep.liannoi.worker.presentation.common.extensions

import androidx.fragment.app.Fragment
import androidx.work.WorkManager
import org.itstep.liannoi.worker.presentation.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory =
    ViewModelFactory(WorkManager.getInstance(requireContext()))
