package com.example.databaseexample

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.bgg.Games
import com.example.bgg.Ranks
import java.util.*


class Database(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "gamesDB.db"

        val TABLE_GAMES = "games"
        val COLUMN_ID = "_id"
        val COLUMN_NAME = "name"
        val COLUMN_YEAR = "year"
        val COLUMN_THUMBNAIL = "thumbnail"
        val COLUMN_RANK = "rank"

        val TABLE_RANKS = "ranks"
        val COLUMN_PLACE = "place"
        val COLUMN_DATE = "datePlace"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_GAMES_TABLE = ("CREATE TABLE " + TABLE_GAMES + "(" + COLUMN_ID + " BIGINT PRIMARY KEY,"
                + COLUMN_NAME + " TEXT," + COLUMN_YEAR + " TEXT," + COLUMN_THUMBNAIL + " TEXT," + COLUMN_RANK + " INTEGER" + ")")
        db.execSQL(CREATE_GAMES_TABLE)

        val CREATE_RANKS_TABLE = ("CREATE TABLE " + TABLE_RANKS + "(" + COLUMN_ID + " BIGINT,"
                + COLUMN_NAME + " TEXT," + COLUMN_PLACE + " INTEGER, " + COLUMN_DATE + " TEXT" + ")")
        db.execSQL(CREATE_RANKS_TABLE)
    }

    fun addGame(game: Games) {
        val values = ContentValues()
        values.put(COLUMN_ID, game.id)
        values.put(COLUMN_NAME, game.name)
        values.put(COLUMN_YEAR, game.year)
        values.put(COLUMN_THUMBNAIL, game.thumbnail)
        values.put(COLUMN_RANK, game.rank)
        val db = this.writableDatabase
        db.insert(TABLE_GAMES, null, values)
        db.close()
    }

    fun addRank(rank: Ranks) {
        val values = ContentValues()
        values.put(COLUMN_ID, rank.id)
        values.put(COLUMN_NAME, rank.name)
        values.put(COLUMN_PLACE, rank.place)
        values.put(COLUMN_DATE, rank.date)
        val db = this.writableDatabase
        db.insert(TABLE_RANKS, null, values)
        db.close()
    }

    fun countGames(): Int {
        val query = "SELECT COUNT($COLUMN_ID) FROM $TABLE_GAMES WHERE $COLUMN_RANK >= 0"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        db.close()
        return count
    }

    fun countExtras(): Int {
        val query = "SELECT COUNT($COLUMN_ID) FROM $TABLE_GAMES WHERE $COLUMN_RANK < 0"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        db.close()
        return count
    }

    fun countRanks(): Int {
        val query = "SELECT COUNT($COLUMN_ID) FROM $TABLE_RANKS"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        db.close()
        return count
    }

    fun deleteAll() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_GAMES")
        db.execSQL("DELETE FROM $TABLE_RANKS")
        db.close();
    }

    operator fun get(i: Int): Games {
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_GAMES", null)
        cursor.moveToPosition(i)
        val id = Integer.parseInt(cursor.getString(0)).toLong()
        val name = cursor.getString(1)
        val year = cursor.getString(2)
        val thumbnail = cursor.getString(3)
        val rank = cursor.getInt(4)
        val game = Games(id, name, year, thumbnail, rank)
        cursor.close()
        db.close()
        return game
    }

    fun getIth(i: Int): Ranks {
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_RANKS", null)
        cursor.moveToPosition(i)
        val id = Integer.parseInt(cursor.getString(0)).toLong()
        val name = cursor.getString(1)
        val place = cursor.getInt(2)
        val date = cursor.getString(3)
        val rank = Ranks(id, name, place, date)
        cursor.close()
        db.close()
        return rank
    }

    fun findGame(gameId: Long, rank: Int) : Long {
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_ID LIKE \"$gameId\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0)).toLong()
            val oldRank = cursor.getInt(4)
            if (oldRank != rank) {
                db.execSQL("UPDATE $TABLE_GAMES SET $COLUMN_RANK = \"$rank\" WHERE $COLUMN_ID = \"$id\"")
            }
            cursor.close()
            db.close()
            return id
        }

        db.close()
        return -1
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RANKS)
        onCreate(db)
    }

    fun findById(id: Long) : String {
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_ID LIKE \"$id\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var name: String = ""

        if (cursor.moveToFirst()) {
            name = cursor.getString(1)
            cursor.close()
        }

        db.close()
        return name
    }
}