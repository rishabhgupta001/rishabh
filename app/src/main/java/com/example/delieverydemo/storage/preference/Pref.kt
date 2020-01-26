package com.example.delieverydemo.storage.preference

import android.content.Context
import android.content.SharedPreferences

private const val PREF_FILE_NAME = "com.example.delieverydemo.pref"
private const val IS_FIRST_TIME_LAUNCH = "IS_FIRST_TIME_LAUNCH"

//loader in paging adapter
const val TYPE_PROGRESS = 0
const val TYPE_ITEM = 1

class Pref(context: Context) {


    private lateinit var sharedPreferences: SharedPreferences

    private fun getSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            PREF_FILE_NAME, 0
        )
    }

    fun setString(context: Context, key: String, value: String?) {
        getSharedPreferences(
            context
        )
        val editor = sharedPreferences.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getString(context: Context, key: String, defaultValue: String): String? {
        getSharedPreferences(
            context
        )
        return sharedPreferences.getString(key, defaultValue)
    }

    fun setInt(context: Context, key: String, value: Int?) {
        getSharedPreferences(
            context
        )
        val editor = sharedPreferences.edit()
        if (value != null) {
            editor?.putInt(key, value)
        }
        editor?.apply()
    }

    fun getInt(context: Context, key: String): Int {
        getSharedPreferences(
            context
        )
        return sharedPreferences.getInt(key, 0)
    }

    fun setBoolean(context: Context, key: String, value: Boolean) {
        getSharedPreferences(
            context
        )
        val editor = sharedPreferences.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    fun getBoolean(context: Context, key: String): Boolean? {
        getSharedPreferences(
            context
        )
        return sharedPreferences.getBoolean(key, false)
    }

    fun clearPref(context: Context) {
        getSharedPreferences(
            context
        )
        sharedPreferences.edit()?.clear()?.apply()
    }

    fun isFirstTimeLaunch(context: Context): Boolean? {
        getSharedPreferences(
            context
        )
        return sharedPreferences.getBoolean(
            IS_FIRST_TIME_LAUNCH, true
        )
    }

    fun setFirstTimeLaunch(context: Context, isFirstTime: Boolean) {
        getSharedPreferences(
            context
        )
        val editor = sharedPreferences.edit()
        editor?.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor?.apply()
    }
}