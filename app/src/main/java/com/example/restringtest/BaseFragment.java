package com.example.restringtest;

import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

public abstract class BaseFragment<T extends ViewBinding> extends BaseViewBindingFragment<T> {

    public BaseFragment(int layoutId) {
        super(layoutId);
    }

}
