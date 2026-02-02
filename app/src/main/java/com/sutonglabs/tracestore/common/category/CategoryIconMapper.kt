package com.sutonglabs.tracestore.common.category

import com.sutonglabs.tracestore.R


object CategoryIconMapper {

    fun iconFor(name: String): Int {
        return when (categoryKey(name)) {
            "electronics" -> R.drawable.placeholder
//            "home_appliances" -> R.drawable.ic_home
//            "men_fashion" -> R.drawable.ic_men
//            "women_fashion" -> R.drawable.ic_women
//            "sports_and_fitness" -> R.drawable.ic_sports
            else -> R.drawable.placeholder
        }
    }
}