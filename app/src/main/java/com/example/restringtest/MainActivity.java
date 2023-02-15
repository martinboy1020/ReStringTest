package com.example.restringtest;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreated() {
        transFragment();
    }

    private void transFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_view, new FirstFragment());
        fragmentTransaction.commit();
    }

}
