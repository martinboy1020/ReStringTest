package com.example.restringtest

import android.content.Intent
import android.util.Log
import dev.b3nedikt.restring.Restring
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class SplashActivity: BaseActivity() {
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
            val jsonObj: JSONObject?
            try {
                Log.d("tttttt", "LanguageUtils.getCurrentLanguageString(this): " + LanguageUtils.getCurrentLanguageString(this))
                val inputStream = when (LanguageUtils.getCurrentLanguageString(this)) {
                    getString(R.string.simplified_chinese) -> am.open("sc_strings.xml")
                    getString(R.string.traditional_chinese) -> am.open("tc_strings.xml")
                    else -> am.open("en_strings.xml")
                }
                val xmlToJson = XmlToJson.Builder(inputStream, null).build()
                inputStream.close()
                jsonObj = xmlToJson.toJson()
                if (jsonObj != null) {
                    val jsonRes = jsonObj.getJSONObject("resources") as JSONObject
                    val jsonString = jsonRes.getJSONArray("string") as JSONArray
                    Log.d("hhhhhh", "jsonString.length(): " + jsonString.length())
                    val yourHashMap =
                        HashMap<String, String>()
                    for (i in 0 until jsonString.length()) {
                        val jsonContent = jsonString.getJSONObject(i)
                        yourHashMap[jsonContent.optString("name")] =
                            jsonContent.optString("content")
                    }
                    Restring.putStrings(LanguageUtils.getCurrentLocale(this), yourHashMap)
                    Log.d("hhhhhh", "yourHashMap: " + yourHashMap.size)
                    if (yourHashMap.size > 0 && yourHashMap.containsKey("test_word")) {
                        Log.d(
                            "hhhhhh",
                            "test_word: " + yourHashMap["test_word"]
                        )
                    }
                }
            } catch (e: IOException) {
                Log.d("hhhhhhh", "parserXmlToJson e: $e")
                e.printStackTrace()
            } catch (e: JSONException) {
                Log.d("hhhhhhh", "parserXmlToJson e: $e")
                e.printStackTrace()
            }
            runOnUiThread {
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        thread.start()
    }

}