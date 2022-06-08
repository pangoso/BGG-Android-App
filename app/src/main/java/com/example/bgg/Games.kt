package com.example.bgg

import java.lang.StringBuilder
import java.util.*

class Games {
    var id: Long
    var name: String
    var year: String
    var thumbnail: String
    var rank: Int

    constructor(id: Long, name: String, year: String, thumbnail: String, rank: Int) {
        this.id = id
        this.name = name
        this.year = year
        this.thumbnail = thumbnail
        this.rank = rank
    }
}