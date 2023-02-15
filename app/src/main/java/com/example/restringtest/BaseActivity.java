package com.example.restringtest;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import dev.b3nedikt.reword.Reword;

public abstract class BaseActivity extends ReStringAndLanguageActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Reword.reword(rootView);
        onCreated();
    }

    public abstract int getLayoutId();
    public abstract void onCreated();

}
