package org.itstep.liannoi.worker.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.itstep.liannoi.worker.databinding.FragmentMainBinding
import org.itstep.liannoi.worker.presentation.common.getViewModelFactory

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels { getViewModelFactory() }
    private lateinit var viewDataBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentMainBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
    }
}
