package com.example.runescapemarketwatchv3.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.runescapemarketwatchv3.Item  // Import your UI model
import com.example.runescapemarketwatchv3.CurrentPrice
import com.example.runescapemarketwatchv3.TodayPrice
import com.example.runescapemarketwatchv3.TrendChange

// Define the Items table schema
@Entity(tableName = "items", indices = [Index(value = ["name"], unique = true)])
data class RoomItem(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val icon: String,
    val iconLarge: String?,
    val price: Double,  // Storing price as a Double in Room
    val trend: String,
    val category: String?,
    val members: Boolean,
    val day30Trend: String,
    val day30Change: String,
    val day90Trend: String,
    val day90Change: String,
    val day180Trend: String,
    val day180Change: String,
    val todayTrend: String,
    val todayPrice: Double,
    val type: String,
    val typeIcon: String
)


// Extension function to map RoomItem to domain Item
fun RoomItem.toDomainItem(): Item {
    return Item(
        id = id,
        name = name,
        description = description,
        icon = icon,
        iconLarge = iconLarge,
        current = CurrentPrice(
            price = price.toString(),  // Converting Double back to String for the domain model
            trend = trend
        ),
        today = TodayPrice(
            todayTrend,
            todayPrice.toString()
        ),
        category = category,
        members = members,
        day30 = TrendChange(day30Trend, day30Change),
        day90 = TrendChange(day90Trend, day90Change),
        day180 = TrendChange(day180Trend, day180Change),
        type = type,
        typeIcon = typeIcon
    )
}


// Extension function to map domain Item to RoomItem (for inserting into Room)
fun Item.toRoomItem(): RoomItem {
    return RoomItem(
        id = id,
        name = name,
        description = description,
        icon = icon,
        iconLarge = iconLarge,
        price = current.getNumericPrice(),  // Convert price from String to Double
        trend = current.trend,
        category = category,
        members = members,
        day30Trend = day30.trend,
        day30Change = day30.change,
        day90Trend = day90.trend,
        day90Change = day90.change,
        day180Trend = day180.trend,
        day180Change = day180.change,
        todayTrend = today.trend,
        todayPrice = today.getNumericPrice(),  // Assuming todayPrice is also in a similar format
        type = type,
        typeIcon = typeIcon
    )
}
