package com.example.bgg.sorters

import com.example.bgg.Games

object SortByRank : Comparator<Any> {
    override fun compare(o1: Any, o2: Any): Int {
        val p1: Games = o1 as Games
        val p2: Games = o2 as Games
        return p1.rank.compareTo(p2.rank)
    }
}
