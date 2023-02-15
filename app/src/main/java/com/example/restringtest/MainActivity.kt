package com.example.restringtest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.restringtest.databinding.ActivityMainBinding
import dev.b3nedikt.restring.Restring
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreated() {
        val btnActivityB = findViewById<Button>(R.id.btn_activity_b)
        btnActivityB.setOnClickListener {
//            startActivity(Intent(this, ActivityB::class.java))
            LanguageUtils.showChangeLanguageDialog(this, supportFragmentManager, object :
                LanguageUtils.ChangeLanguageDialogFragment.ChangeLanguageDialogListener {
                override fun changeLanguage(languageId: String) {
                    val settingLanguage =
                        Preference.getString(this@MainActivity, Constants.LANGUAGE.APP_LANGUAGE, "")
                    if (settingLanguage == "") {
                        val mobileDefaultLanguage =
                            LanguageUtils.getCurrentLocaleLanguage(this@MainActivity)
                        if (mobileDefaultLanguage != languageId) {
                            Preference.saveString(this@MainActivity, Constants.LANGUAGE.APP_LANGUAGE, languageId)
                            val intent = Intent(this@MainActivity, SplashActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    } else {
                        if (settingLanguage != languageId) {
                            Preference.saveString(this@MainActivity, Constants.LANGUAGE.APP_LANGUAGE, languageId)
                            val intent = Intent(this@MainActivity, SplashActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                }
            })
        }
    }

}