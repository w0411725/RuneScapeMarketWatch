package com.example.runescapemarketwatchv3.Repositories

import android.util.Log
import com.example.runescapemarketwatchv3.ApiLayer.RetrofitInstance
import com.example.runescapemarketwatchv3.ItemResponse
import com.example.runescapemarketwatchv3.ViewModels.SortOrder
import com.example.runescapemarketwatchv3.database.ItemDao
import com.example.runescapemarketwatchv3.database.RoomItem
import kotlinx.coroutines.delay
import com.example.runescapemarketwatchv3.Item
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll


class ItemRepository(private val dao: ItemDao) {

    // Fetch the highest id already in the database
    suspend fun getMaxId(): Int {
        return dao.getMaxId() ?: 0 // If no data exists, return 0
    }

    suspend fun getItemsBatch(startId: Int, batchSize: Int): List<RoomItem> {
        return dao.getItemsBatch(startId, batchSize)
    }

    // Insert a single item into the Room database
    suspend fun insertItemToDatabase(item: RoomItem) {
        try {
            Log.d("DatabaseInsert", "Inserting item into DB: ${item.name}")
            dao.insertItem(item) // Pass the RoomItem instance to insert
        } catch (e: Exception) {
            Log.e("DatabaseInsertError", "Error inserting item: ${item.name}", e)
        }
    }

    // Fetch a single item by ID from the Room database
    suspend fun getItemById(itemId: Int): RoomItem? {
        return dao.getItemById(itemId) // Direct call to the DAO method
    }

    // Fetch items sorted by the selected sort order
    suspend fun getSortedItems(sortOrder: SortOrder): List<RoomItem> {
        return when (sortOrder) {
            SortOrder.PriceLowToHigh -> dao.getAllItemsSortedByPriceAscending()
            SortOrder.PriceHighToLow -> dao.getAllItemsSortedByPriceDescending()
            SortOrder.Name -> dao.getAllItemsSortedByName()
            SortOrder.Trend -> dao.getAllItemsSortedByTrend()
        }
    }

    // Continuous seeding with proper numeric price conversion
    suspend fun startContinuousSeeding(batchSize: Int = 100) {
        while (true) {
            try {
                val currentMaxId = getMaxId()
                val startId = currentMaxId + 1
                val endId = startId + batchSize - 1

                Log.d("ContinuousSeeding", "Seeding items sequentially from $startId to $endId")

                for (id in startId..endId) {
                    try {
                        val response = RetrofitInstance.api.getItemDetails(id)

                        if (response.isSuccessful) {
                            val itemResponse = response.body()
                            if (itemResponse != null) {
                                // Convert ItemResponse to RoomItem with extrapolated price
                                val roomItem = itemResponse.item.toRoomItemWithCleanPrice()
                                insertItemToDatabase(roomItem)
                                Log.d("SequentialSeeding", "Successfully seeded item ID: $id")
                            } else {
                                Log.w("SequentialSeeding", "Item ID: $id returned null response")
                            }
                        } else {
                            Log.w("SequentialSeeding", "Failed response for item ID: $id")
                        }
                    } catch (e: Exception) {
                        Log.e("SeedingError", "Error fetching item details for ID: $id", e)
                        delay(5000) // Longer delay before retrying the next item
                    }
                }
            } catch (e: Exception) {
                Log.e("SeedingError", "Error during continuous seeding batch", e)
            }
            delay(500) // Delay before the next batch
        }
    }

    // Fetch items by a range of IDs and insert them into the DB with extrapolated prices
    suspend fun fetchItemsByIdsRange(startId: Int, endId: Int): List<ItemResponse?> {
        return coroutineScope {
            val currentMaxId = getMaxId()
            val startRange = maxOf(currentMaxId + 1, startId)

            (startRange..endId).map { id ->
                async {
                    try {
                        val response = RetrofitInstance.api.getItemDetails(id)
                        if (response.isSuccessful) {
                            val itemResponse = response.body()
                            if (itemResponse != null) {
                                val roomItem = itemResponse.item.toRoomItemWithCleanPrice()
                                insertItemToDatabase(roomItem)
                            }
                            itemResponse
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        Log.e("APIRequestError", "Error fetching item details for ID: $id", e)
                        null
                    }
                }
            }.awaitAll()
        }
    }
}

// Extension function to convert Item to RoomItem with extrapolated price
fun Item.toRoomItemWithCleanPrice(): RoomItem {
    return RoomItem(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.current.getNumericPrice(), // Use extrapolated price
        icon = this.icon,
        members = this.members,
        iconLarge = this.iconLarge.orEmpty(), // Use an empty string if null
        trend = this.current.trend,
        category = this.category.orEmpty(), // Use an empty string if null
        day30Trend = this.day30.trend,
        day30Change = this.day30.change,
        day90Trend = this.day90.trend,
        day90Change = this.day90.change,
        day180Trend = this.day180.trend,
        day180Change = this.day180.change,
        todayTrend = this.today.trend,
        todayPrice = this.today.getNumericPrice(), // Extrapolate today's price
        type = this.type,
        typeIcon = this.typeIcon
    )
}
