package com.example.bgg.sharedPreferences

import java.util.*

interface Preferences {
    fun loadNick():String
    fun saveNick(nick: String)

    fun ifFirstLoad(): Boolean
    fun changeFirstLoad(load: Boolean)

    fun isDataLoaded(): Boolean
    fun changeIsDataLoaded(load: Boolean)

    fun loadTime(): String
    fun changeTime(load: Boolean)

    companion object {
        const val KEY_NICK = "nick"
        const val KEY_FIRST = "first"
        const val KEY_DATA_LOADED = "data"
        const val KEY_TIME = "time"
    }
}