package com.example.restringtest

import android.content.Intent

class SplashActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreated() {
        Preference.init(this)
        parserXmlToJson()
//        finish()
//        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun parserXmlToJson() {
        val am = this.assets
        val thread = Thread {
            ParserStringUtils.parserXmlToMap(this, am)
            runOnUiThread {
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        thread.start()
    }

}