package com.example.bgg.sharedPreferences

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class PreferencesInt(private val sharedPreferences: SharedPreferences): Preferences {
    override fun loadNick(): String {
        return sharedPreferences.getString(Preferences.KEY_NICK, " ")?:""
    }

    override fun saveNick(nick: String) {
        sharedPreferences.edit().putString(Preferences.KEY_NICK, nick).apply()
    }

    override fun ifFirstLoad(): Boolean {
        return sharedPreferences.getBoolean(Preferences.KEY_FIRST, false)
    }

    override fun changeFirstLoad(load: Boolean) {
        sharedPreferences.edit().putBoolean(Preferences.KEY_FIRST, load).apply()
    }

    override fun isDataLoaded(): Boolean {
        return sharedPreferences.getBoolean(Preferences.KEY_DATA_LOADED, true)
    }

    override fun changeIsDataLoaded(load: Boolean) {
        sharedPreferences.edit().putBoolean(Preferences.KEY_DATA_LOADED, load).apply()
    }

    override fun loadTime(): String {
        return sharedPreferences.getString(Preferences.KEY_TIME, " ")?:""
    }

    override fun changeTime(load: Boolean) {
        if (load) {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDate = sdf.format(Calendar.getInstance().time)
            sharedPreferences.edit().putString(Preferences.KEY_TIME, currentDate).apply()
        } else
            sharedPreferences.edit().putString(Preferences.KEY_TIME, " ").apply()
    }

}