package com.example.restringtest

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import dev.b3nedikt.restring.PluralKeyword
import dev.b3nedikt.restring.Restring
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import org.json.JSONArray
import org.json.JSONObject

object ParserStringUtils {

    fun parserXmlToMap(context: Context, am: AssetManager) {
        val jsonObj: JSONObject?
        Log.d(
            "tttttt",
            "LanguageUtils.getCurrentLanguageString(this): " + LanguageUtils.getCurrentLanguageString(
                context
            )
        )
        val inputStream = when (LanguageUtils.getCurrentLanguageString(context)) {
            context.resources.getString(R.string.simplified_chinese) -> am.open("sc_strings.xml")
            context.resources.getString(R.string.traditional_chinese) -> am.open("tc_strings.xml")
            else -> am.open("en_strings.xml")
        }
        val xmlToJson = XmlToJson.Builder(inputStream, null).build()
        inputStream.close()
        jsonObj = xmlToJson.toJson()
        Log.d("hhhhhh", "jsonObj: $jsonObj")
        if (jsonObj != null) {
            val jsonRes = jsonObj.getJSONObject("resources") as JSONObject
            val stringHashMap = parserStringToMap(jsonRes)
            Restring.putStrings(LanguageUtils.getCurrentLocale(context), stringHashMap)
            Log.d("hhhhhh", "yourHashMap: " + stringHashMap.size)
            if (stringHashMap.size > 0 && stringHashMap.containsKey("test_word")) {
                Log.d(
                    "hhhhhh",
                    "test_word: " + stringHashMap["test_word"]
                )
            }
            val pluralsHashMap = parserPluralsToMap(jsonRes)
            Restring.putQuantityStrings(LanguageUtils.getCurrentLocale(context), pluralsHashMap)
        }
    }

    private fun parserStringToMap(jsonObj: JSONObject): HashMap<String, String> {
        val stringHashMap = HashMap<String, String>()
        val jsonString = jsonObj.getJSONArray("string") as JSONArray
        Log.d("hhhhhh", "jsonString.length(): " + jsonString.length())
        for (i in 0 until jsonString.length()) {
            val jsonContent = jsonString.getJSONObject(i)
            stringHashMap[jsonContent.optString("name")] =
                jsonContent.optString("content")
        }
        return stringHashMap
    }

    private fun parserPluralsToMap(jsonObj: JSONObject): HashMap<String, HashMap<PluralKeyword, CharSequence>> {
        val pluralsHashMap = HashMap<String, HashMap<PluralKeyword, CharSequence>>()
        val jsonPlurals = jsonObj.getJSONArray("plurals") as JSONArray
        for (i in 0 until jsonPlurals.length()) {
            val jsonName = jsonPlurals.getJSONObject(i).getString("name")
            val itemHashMap = HashMap<PluralKeyword, CharSequence>()
            try {
                val jsonItems = jsonPlurals.getJSONObject(i).getJSONArray("item")
                for (j in 0 until jsonItems.length()) {
                    val jsonItemContent = jsonItems.getJSONObject(j)
                    if (!jsonItemContent.getString("quantity")
                            .isNullOrEmpty() && !jsonItemContent.getString("content")
                            .isNullOrEmpty()
                    ) {
                        when (jsonItemContent.getString("quantity")) {
                            "one" -> {
                                itemHashMap[PluralKeyword.ONE] =
                                    jsonItemContent.getString("content")
                            }
                            "other" -> {
                                itemHashMap[PluralKeyword.OTHER] =
                                    jsonItemContent.getString("content")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                val jsonItemContent = jsonPlurals.getJSONObject(i).getJSONObject("item")
                if (!jsonItemContent.getString("quantity")
                        .isNullOrEmpty() && !jsonItemContent.getString("content").isNullOrEmpty()
                ) {
                    when (jsonItemContent.getString("quantity")) {
                        "one" -> {
                            itemHashMap[PluralKeyword.ONE] = jsonItemContent.getString("content")
                        }
                        "other" -> {
                            itemHashMap[PluralKeyword.OTHER] = jsonItemContent.getString("content")
                        }
                    }
                }
            }
            pluralsHashMap[jsonName] = itemHashMap
        }
        return pluralsHashMap
    }

}