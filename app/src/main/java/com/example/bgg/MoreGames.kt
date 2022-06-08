package com.example.bgg

import CustomAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg.sorters.SortByName
import com.example.bgg.sorters.SortByRank
import com.example.bgg.sorters.SortByYear
import com.example.databaseexample.Database
import java.util.*


class MoreGames : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moregames)

        val intent = intent

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)

        var data = ArrayList<Games>()
        val dbHandler = Database(this, null, null, 1)
        val total = dbHandler.countExtras() + dbHandler.countGames()
        for (i in 0..total-1) {
            val game = dbHandler[i]
            if (game.rank >= 0) {
                data.add(game)
            }
        }

        val adapter = CustomAdapter(data, this) {
            val text = it
            val dbHandler = Database(this, null, null, 1)
            val mes = dbHandler.findById(text)
            val intent = Intent(this, History::class.java)
            intent.putExtra("Message", mes)
            startActivityForResult(intent, 0)
        }

        findViewById<Button>(R.id.backButton).setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivityForResult(intent, 0)
        }

        findViewById<Button>(R.id.nameSort).setOnClickListener {
            Collections.sort<Any>(data as List<Any>?, SortByName)
            adapter.notifyDataSetChanged()
        }

        findViewById<Button>(R.id.rankSort).setOnClickListener {
            Collections.sort<Any>(data as List<Any>?, SortByRank)
            adapter.notifyDataSetChanged()
        }

        findViewById<Button>(R.id.yearSort).setOnClickListener {
            Collections.sort<Any>(data as List<Any>?, SortByYear)
            adapter.notifyDataSetChanged()
        }

        recyclerview.adapter = adapter
    }
}