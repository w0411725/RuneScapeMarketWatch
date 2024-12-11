package com.example.runescapemarketwatchv3.database

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun initDatabase(context: Context) {
    withContext(Dispatchers.IO) {
        // Initialize the Room database
        val db = AppDatabase.getDatabase(context)

        // Example of inserting an item (this should be done from the repository or view model)
//        val item = RoomItem(
//            id = 21787,
//            name = "Steadfast Boots",
//            description = "A pair of powerful-looking boots.",
//            icon = "https://secure.runescape.com/m=itemdb_rs/1733145443057_obj_sprite.gif?id=21787",
//            iconLarge = "https://secure.runescape.com/m=itemdb_rs/1733145443057_obj_big.gif?id=21787",
//            price = "4.8m",  // Example price
//            trend = "neutral",
//            category = "Miscellaneous",
//            members = true,
//            day30Trend = "negative",
//            day30Change = "-2.0%",
//            day90Trend = "positive",
//            day90Change = "+4.0%",
//            day180Trend = "positive",
//            day180Change = "+25.0%",
//            todayTrend = "neutral",
//            todayPrice = 0,
//            type = "Miscellaneous",
//            typeIcon = "https://www.runescape.com/img/categories/Miscellaneous"
//        )

//        // Insert item into the database
//        db.itemDao().insertItem(item)

        // Optionally, check if the item exists or fetch data
        val allItems = db.itemDao().getAllItems()
        println("Items in DB: ${allItems.size}")
    }
}
