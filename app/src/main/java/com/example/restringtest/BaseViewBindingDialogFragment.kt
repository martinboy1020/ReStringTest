package com.example.restringtest

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseViewBindingDialogFragment<T : ViewBinding>(layoutId: Int) : DialogFragment(layoutId) {
    private var _binding: T? = null

    val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = initBinding(view)
    }

    abstract fun initBinding(view: View): T

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}