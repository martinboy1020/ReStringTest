package com.example.restringtest

import android.app.Application
import dev.b3nedikt.restring.Restring
import dev.b3nedikt.reword.RewordInterceptor
import dev.b3nedikt.viewpump.ViewPump

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Restring.init(this)
        ViewPump.init(RewordInterceptor)
    }

}