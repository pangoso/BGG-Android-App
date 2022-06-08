package com.example.bgg.sorters

import com.example.bgg.Ranks

object SortByDate : Comparator<Any> {
    override fun compare(o1: Any, o2: Any): Int {
        val p1: Ranks = o1 as Ranks
        val p2: Ranks = o2 as Ranks
        return p1.date.compareTo(p2.date)
    }
}
