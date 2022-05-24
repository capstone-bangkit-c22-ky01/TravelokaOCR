package com.example.travelokaocr.viewmodel.preferences

import android.content.Context
import android.content.SharedPreferences

class UserPreference (context: Context) {
    private var preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun putDataLogin(key: String, value: String){
        editor.putString(key, value).apply()
    }

    fun putSessionLogin(key: String, value: Boolean){
        editor.putBoolean(key, value).apply()
    }

    fun getDataLogin(key: String): String? {
        return preferences.getString(key, null)
    }

    fun getSessionLogin(key: String): Boolean{
        return preferences.getBoolean(key, false)
    }

    fun clear(){
        editor.clear()
            .apply()
    }

    companion object{
        const val PREFS_NAME = "user_pref"
    }
}