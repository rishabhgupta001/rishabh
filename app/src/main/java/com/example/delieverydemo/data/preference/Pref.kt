package com.example.delieverydemo.data.preference

import android.content.Context
import android.content.SharedPreferences

private const val PREF_FILE_NAME = "com.example.delieverydemo.pref"
private const val IS_FIRST_TIME_LAUNCH = "IS_FIRST_TIME_LAUNCH"

const val NEXT_OFFSET_COUNT = "NEXT_OFFSET_COUNT"


class Pref(context: Context) {
    private val appContext = context.applicationContext

    private val sharedPreferences: SharedPreferences
        get() = appContext.getSharedPreferences(PREF_FILE_NAME, 0)


    fun setString(key: String, value: String?) {
        val editor = sharedPreferences.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getString(key: String, defaultValue: String): String? =
        sharedPreferences.getString(key, defaultValue)

    fun setInt(key: String, value: Int?) {
        val editor = sharedPreferences.edit()
        if (value != null) {
            editor?.putInt(key, value)
        }
        editor?.apply()
    }

    fun getInt(key: String): Int = sharedPreferences.getInt(key, 0)


    fun setBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    fun getBoolean(key: String): Boolean? = sharedPreferences.getBoolean(key, false)

    fun clearPref() {
        sharedPreferences.edit()?.clear()?.apply()
    }

    fun isFirstTimeLaunch(): Boolean? = sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true)

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        val editor = sharedPreferences.edit()
        editor?.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor?.apply()
    }
}