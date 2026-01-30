package com.sutonglabs.tracestore.data

import com.sutonglabs.tracestore.models.Product
import javax.inject.Inject

class DemoDB @Inject constructor() {
    private val description =
        "Wireless Controller for PS4™ gives you what you want in your gaming from over precision control your games to sharing …"


    fun getProduct(): List<Product> {
        return listOf(
            Product(
                id = 1,
                name = "Wireless Controller for PS4™",
                description = description,
                image = "ggg",
                price = 79.0,
                ),
            Product(
                id = 1,
                name = "Nike Sport White - Man Pant",
                description = description,
                image = "ggg",
                price = 433.0,
            ),
            Product(
                id = 1,
                name = "Gloves XC Omega - Polygon",
                description = description,
                image = "ggg",
                price = 43.0,
            ),
            Product(
                id = 1,
                name = "Gloves XC Omega - Polygon",
                description = description,
                image = "ggg",
                price = 34.0,
            )
            )

    }
}