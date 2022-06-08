package com.example.bgg

import java.lang.StringBuilder
import java.util.*

class Ranks {
    var id: Long
    var name: String
    var place: Int
    var date: String

    constructor(id: Long, name: String, place: Int, date: String) {
        this.id = id
        this.name = name
        this.place = place
        this.date = date
    }
}