package com.example.restringtest

import android.content.Context
import android.content.res.Configuration
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.ViewPumpAppCompatDelegate
import dev.b3nedikt.restring.Restring

open class ReStringAndLanguageActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        val newContext: Context? = newBase?.let { LanguageUtils.getAttachBaseContext(it) }
        //升級AndroidX appcompat 1.2+版本導致多語言切換失敗解決方案
        val configuration = newContext?.resources?.configuration
        val wrappedContext: ContextThemeWrapper = object : ContextThemeWrapper(
            newContext,
            R.style.Theme_AppCompat_Empty
        ) {
            override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
                val uiMode = overrideConfiguration.uiMode
                overrideConfiguration.setTo(configuration)
                overrideConfiguration.uiMode = uiMode
                super.applyOverrideConfiguration(overrideConfiguration)
            }
        }
        super.attachBaseContext(wrappedContext)
    }

//    private val appCompatDelegate: AppCompatDelegate by lazy {
//        ViewPumpAppCompatDelegate(
//            baseDelegate = super.getDelegate(),
//            baseContext = this,
//            wrapContext = Restring::wrapContext
//        )
//    }
//
//    override fun getDelegate(): AppCompatDelegate {
//        return appCompatDelegate
//    }

}