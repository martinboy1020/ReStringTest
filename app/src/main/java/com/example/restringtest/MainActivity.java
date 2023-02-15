package com.example.restringtest;

import android.content.Intent;
import android.widget.Button;

public class MainActivity extends BaseActivity{
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreated() {
        Button btnActivityB = findViewById(R.id.btn_activity_b);
        btnActivityB.setOnClickListener(view -> {
            LanguageUtils.INSTANCE.showChangeLanguageDialog(this, getSupportFragmentManager(), languageId -> {
                String settingLanguage = Preference.INSTANCE.getString(this, Constants.LANGUAGE.APP_LANGUAGE, "");
                if (settingLanguage.equals("")) {
                    String mobileDefaultLanguage = LanguageUtils.INSTANCE.getCurrentLocaleLanguage(this);
                    if (!mobileDefaultLanguage.equals(languageId)) {
                        Preference.INSTANCE.saveString(this, Constants.LANGUAGE.APP_LANGUAGE, languageId);
                        Intent intent = new Intent(this, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    if (!settingLanguage.equals(languageId)) {
                        Preference.INSTANCE.saveString(this, Constants.LANGUAGE.APP_LANGUAGE, languageId);
                        Intent intent = new Intent(this, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }, false);
        });
    }
}
