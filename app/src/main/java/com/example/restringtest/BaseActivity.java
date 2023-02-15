package com.example.restringtest;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.ViewPumpAppCompatDelegate;

import dev.b3nedikt.restring.Restring;
import dev.b3nedikt.reword.Reword;

public abstract class BaseActivity extends ReStringAndLanguageActivity {

    private AppCompatDelegate appCompatDelegate = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Reword.reword(rootView);
        onCreated();
    }

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        if (appCompatDelegate == null) {
            appCompatDelegate = new ViewPumpAppCompatDelegate(
                    super.getDelegate(),
                    this,
                    Restring::wrapContext
            );
        }
        return appCompatDelegate;
    }

    public abstract int getLayoutId();
    public abstract void onCreated();

}
