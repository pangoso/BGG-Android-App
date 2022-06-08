package com.example.bgg

import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bgg.sharedPreferences.Preferences
import com.example.bgg.sharedPreferences.PreferencesInt
import com.example.databaseexample.Database
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory


class Profile : AppCompatActivity() {
    private lateinit var preferences: Preferences
    private lateinit var nick: TextView
    private lateinit var gamesTV: TextView
    private lateinit var extrasTV: TextView
    private lateinit var lastSynchTV: TextView

    private var date1: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = applicationContext.getSharedPreferences("pref", MODE_PRIVATE)
        preferences = PreferencesInt(sharedPref)

        setContentView(R.layout.activity_profile)

        val intent = intent

        nick = findViewById(R.id.displayNick)
        nick.text = preferences.loadNick()

        gamesTV = findViewById(R.id.gamesTV)
        extrasTV = findViewById(R.id.extrasTV)
        lastSynchTV = findViewById(R.id.lastSynch)

        findViewById<Button>(R.id.clearButton).setOnClickListener {
            val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
            dialog.setTitle("Modify Customer Details")
            dialog.setMessage("Are you sure you want to reset data?")
            dialog.setPositiveButton("YES",
                DialogInterface.OnClickListener { dialog, id ->
                    preferences.saveNick("")
                    preferences.changeFirstLoad(true)
                    preferences.changeTime(false)

                    val dbHandler = Database(this, null, null, 1)
                    dbHandler.deleteAll()
                    gamesTV.text = ""
                    extrasTV.text = ""
                    preferences.changeIsDataLoaded(false)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivityForResult(intent, 0)
                })
            dialog.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, id ->

            })
            dialog.create().show()
        } 

        findViewById<Button>(R.id.synchButton).setOnClickListener {
            val intent = Intent(this, Synch::class.java)
            startActivityForResult(intent, 0)
        }

        findViewById<Button>(R.id.moreGamesButton).setOnClickListener{
            val intent = Intent(this, MoreGames::class.java)
            startActivityForResult(intent, 0)
        }

        findViewById<Button>(R.id.moreExtrasButton).setOnClickListener{
            val intent = Intent(this, MoreExtras::class.java)
            startActivityForResult(intent, 0)
        }

        if (!preferences.isDataLoaded()){
            preferences.changeTime(true)
            downloadData()
            preferences.changeIsDataLoaded(true)
            lastSynchTV.text = "Last synch: ${preferences.loadTime()}"
        } else {
            val dbHandler = Database(this, null, null, 1)
            val games = dbHandler.countGames()
            val extras = dbHandler.countExtras()
            nick.text = preferences.loadNick()
            gamesTV.text = "No. Games: $games"
            extrasTV.text = "No. Extras: $extras"
            lastSynchTV.text = "Last synch: ${preferences.loadTime()}"
        }
    }

    @Suppress ("DEPRECATION")
    private inner class Downloader: AsyncTask<String, Int, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            loadData()
            showData()
        }

        override fun doInBackground(vararg p0: String?): String {
            try {
                val url = URL("https://www.boardgamegeek.com/xmlapi2/collection?username=${preferences.loadNick()}&stats=1")
                val connection = url.openConnection()
                connection.connect()
                val lenghtOfFile = connection.contentLength
                val isStream = url.openStream()
                val testDirectory = File("$filesDir/XML")
                if (!testDirectory.exists())
                    testDirectory.mkdir()
                val fos = FileOutputStream("$testDirectory/gry.xml")
                val data = ByteArray(1024)
                var count = 0
                var total:Long = 0
                var progress = 0
                count = isStream.read(data)
                while (count!= -1) {
                    total += count.toLong()
                    val progress_temp = total.toInt() * 100 / lenghtOfFile
                    if (progress_temp % 10 == 0 && progress != progress_temp) {
                        progress = progress_temp
                    }
                    fos.write(data, 0, count)
                    count = isStream.read(data)
                }
                isStream.close()
                fos.close()
            } catch (e: MalformedURLException) {
                return "Bad URL"
            } catch (e: FileNotFoundException) {
                return "No file"
            } catch (e: IOException) {
                return "IO Exception"
            }
            return "success"
        }
    }

    private fun showData() {
        val dbHandler = Database(this, null, null, 1)
        val games = dbHandler.countGames()
        val extras = dbHandler.countExtras()
        gamesTV.text = "No. Games: $games"
        extrasTV.text = "No. Extras: $extras"

    }

    fun loadData() {
        val filename = "gry.xml"
        val path = filesDir
        val inDir = File(path, "XML")

        val dbHandler = Database(this, null, null, 1)
        dbHandler.writableDatabase;

        if (inDir.exists()) {
            val file = File(inDir, filename)
            if (file.exists()) {
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

                xmlDoc.documentElement.normalize()

                val items: NodeList = xmlDoc.getElementsByTagName("item")
                val ranks: NodeList = xmlDoc.getElementsByTagName("ranks")

                for (i in 0..items.length-1) {
                    val itemNode: Node = items.item(i)
                    if (itemNode.nodeType == Node.ELEMENT_NODE) {
                        val elem = itemNode as Element
                        val children = elem.childNodes

                        var currentName: String? = null
                        var currentYear: String? = null
                        val currentId = elem.getAttribute("objectid").toLong()
                        var currentThumbnail: String? = null

                        for (j in 0..children.length - 1) {
                            val node = children.item(j)
                            if (node is Element) {
                                when (node.nodeName) {
                                    "name" -> {
                                        currentName = node.textContent
                                    }
                                    "yearpublished" -> {
                                        currentYear = node.textContent
                                    }
                                    "thumbnail" -> {
                                        currentThumbnail = node.textContent
                                    }
                                }
                            }
                        }

                        var finalRank: Int = -1

                        val rankNode: Node = ranks.item(i)
                        if (rankNode.nodeType == Node.ELEMENT_NODE) {
                            val elem = rankNode as Element
                            val children = elem.childNodes
                            for (j in 0..children.length - 1) {
                                val node = children.item(j)
                                if (node is Element) {
                                    when (node.nodeName) {
                                        "rank" -> {
                                            val currentRank = node.getAttribute("value")
                                            val currentType = node.getAttribute("type")
                                            val currentName = node.getAttribute("name")
                                            if (currentType.toString() == "subtype" && currentName.toString() == "boardgame") {
                                                if (currentRank == "Not Ranked") {
                                                    finalRank = -1
                                                } else {
                                                    finalRank = currentRank.toInt()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (currentName!=null && currentYear!=null && currentThumbnail!=null) {
                            val game = Games(currentId, currentName, currentYear, currentThumbnail, finalRank)
                            dbHandler.addGame(game)

                            if (finalRank>=0) {
                                val rank = Ranks(currentId, currentName, finalRank, preferences.loadTime())
                                dbHandler.addRank(rank)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun downloadData() {
        val downloader = Downloader()
        downloader.execute()
    }
}