package com.example.bgg

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.bgg.sharedPreferences.Preferences
import com.example.bgg.sharedPreferences.PreferencesInt
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var enterNick: EditText
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = applicationContext.getSharedPreferences("pref", MODE_PRIVATE)
        preferences = PreferencesInt(sharedPref)

        if (preferences.ifFirstLoad()) {
            setContentView(R.layout.activity_main)

            enterNick = findViewById(R.id.enterNick)
            findViewById<Button>(R.id.submitNick).setOnClickListener {
                val intent = Intent(this, Profile::class.java)
                preferences.saveNick(enterNick.text.toString())
                preferences.changeFirstLoad(false)
                preferences.changeIsDataLoaded(false)
                startActivityForResult(intent, 0)
            }
        } else {
            val intent = Intent(this, Profile::class.java)
            startActivityForResult(intent, 0)
        }
    }
}