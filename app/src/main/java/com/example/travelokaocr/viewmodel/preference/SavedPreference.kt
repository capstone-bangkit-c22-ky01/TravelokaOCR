package com.example.travelokaocr.viewmodel.preference

import android.content.Context
import android.content.SharedPreferences
import com.example.travelokaocr.utils.Constants

class SavedPreference (context: Context) {
    private var pref = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun putData(key: String, value: String){
        editor.putString(key, value).apply()
    }

    fun putSession(key: String, value: Boolean){
        editor.putBoolean(key, value).apply()
    }

    fun getData(key: String): String? {
        return pref.getString(key, null)
    }

    fun getSession(key: String): Boolean{
        return pref.getBoolean(key, false)
    }

    fun getInstall(key: String): Boolean{
        return pref.getBoolean(key, true)
    }

    fun putInstall(key: String, value: Boolean){
        editor.putBoolean(key, value).apply()
    }

    fun clear(){
        editor.clear()
            .apply()
    }
}