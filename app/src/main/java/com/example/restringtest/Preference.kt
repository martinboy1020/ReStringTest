package com.example.restringtest

import android.content.Context
import android.content.SharedPreferences

object Preference {

    private var mSharedPreferences: SharedPreferences? = null
    fun init(context: Context?) {
        mSharedPreferences =
            context?.getSharedPreferences(Constants.SETTING_PREFERENCE, Context.MODE_PRIVATE)
    }

    fun clearAll(context: Context?) {
        if (mSharedPreferences == null) {
            init(context)
        }
        mSharedPreferences!!.edit().clear().commit()
    }

    fun saveInt(context: Context?, key: String?, value: Int) {
        if (mSharedPreferences == null) {
            init(context)
        }
        mSharedPreferences!!.edit().putInt(key, value).commit()
    }

    fun getInt(context: Context?, key: String?, value: Int): Int {
        if (mSharedPreferences == null) {
            init(context)
        }
        return mSharedPreferences!!.getInt(key, value)
    }

    fun saveString(context: Context?, key: String?, value: String?) {
        if (mSharedPreferences == null) {
            init(context)
        }
        mSharedPreferences!!.edit().putString(key, value).commit()
    }

    fun getString(context: Context?, key: String?, value: String?): String? {
        if (mSharedPreferences == null) {
            init(context)
        }
        return mSharedPreferences!!.getString(key, value)
    }

    fun removeString(context: Context?, key: String?) {
        if (mSharedPreferences == null) {
            init(context)
        }
        mSharedPreferences!!.edit().remove(key).commit()
    }

    fun saveBoolean(context: Context?, key: String?, value: Boolean) {
        if (mSharedPreferences == null) {
            init(context)
        }
        mSharedPreferences!!.edit().putBoolean(key, value).commit()
    }

    fun getBoolean(context: Context?, key: String?, value: Boolean): Boolean {
        if (mSharedPreferences == null) {
            init(context)
        }
        return mSharedPreferences!!.getBoolean(key, value)
    }

    fun saveStringSet(context: Context?, key: String?, value: Set<String?>?) {
        if (mSharedPreferences == null) {
            init(context)
        }
        mSharedPreferences!!.edit().putStringSet(key, value).commit()
    }

    fun getStringSet(context: Context?, key: String?, value: Set<String?>?): Set<String>? {
        if (mSharedPreferences == null) {
            init(context)
        }
        return mSharedPreferences!!.getStringSet(key, value)
    }

    fun removeStrings(context: Context?, vararg keys: String?) {
        if (mSharedPreferences == null) {
            init(context)
        }
        for (key in keys) {
            mSharedPreferences!!.edit().remove(key).commit()
        }
    }

    fun contains(context: Context?, key: String?): Boolean {
        if (mSharedPreferences == null) {
            init(context)
        }
        return mSharedPreferences!!.contains(key)
    }
}