package com.example.bgg

import HistoryAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg.sorters.SortByDate
import com.example.databaseexample.Database
import java.text.SimpleDateFormat
import java.util.*


class History : AppCompatActivity() {
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val intent = intent
        val result = intent.getStringExtra("Message")
        title = findViewById(R.id.titleTV)
        title.text = result

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<Ranks>()
        val dbHandler = Database(this, null, null, 1)
        val total = dbHandler.countRanks()
        for (i in 0..total-1) {
            val rank = dbHandler.getIth(i)
            if (rank.name == result) {
                data.add(rank)
            }
        }

        Collections.sort<Any>(data as List<Any>?, SortByDate.reversed())

        val adapter = HistoryAdapter(data, this)

        findViewById<Button>(R.id.backButton).setOnClickListener {
            title.text = ""
            val intent = Intent(this, MoreGames::class.java)
            startActivityForResult(intent, 0)
        }

        recyclerview.adapter = adapter
    }
}