package com.example.runescapemarketwatchv3.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ItemDao {

    // Fetch the highest ID in the database
    @Query("SELECT MAX(id) FROM items")
    suspend fun getMaxId(): Int?

    // Insert a single item into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: RoomItem)

    // Insert multiple items into the database
    @Transaction
    suspend fun insertItems(items: List<RoomItem>) {
        items.forEach { insertItem(it) } // Insert each item one by one
    }

    // Fetch a batch of items by ID range
    @Query("SELECT * FROM items WHERE id >= :startId ORDER BY id ASC LIMIT :batchSize")
    suspend fun getItemsBatch(startId: Int, batchSize: Int): List<RoomItem>

    // Fetch all items
    @Query("SELECT * FROM items")
    suspend fun getAllItems(): List<RoomItem>

    // Fetch a single item by ID
    @Query("SELECT * FROM items WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): RoomItem?

    // Sorting queries
    @Query("SELECT * FROM items ORDER BY price ASC")
    suspend fun getAllItemsSortedByPriceAscending(): List<RoomItem>

    @Query("SELECT * FROM items ORDER BY price DESC")
    suspend fun getAllItemsSortedByPriceDescending(): List<RoomItem>

    @Query("SELECT * FROM items ORDER BY name ASC")
    suspend fun getAllItemsSortedByName(): List<RoomItem>

    @Query("SELECT * FROM items ORDER BY trend ASC")
    suspend fun getAllItemsSortedByTrend(): List<RoomItem>
}
