package com.example.runescapemarketwatchv3

// Data class representing the response from the API
import android.util.Log
import com.example.runescapemarketwatchv3.database.RoomItem
import com.google.gson.annotations.SerializedName  // For Retrofit serialization

data class ItemResponse(
    @SerializedName("item") val item: Item
)

// The main item class that contains the details of an item
data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val icon: String,
    val iconLarge: String?,
    val current: CurrentPrice,
    val today: TodayPrice,
    val category: String?,
    val members: Boolean,
    val day30: TrendChange,
    val day90: TrendChange,
    val day180: TrendChange,
    val type: String,
    val typeIcon: String,
    val priceHistory: List<PriceHistory>? = emptyList()
)

data class CurrentPrice(
    val trend: String,
    val price: String
) {
    fun getNumericPrice(): Double {
        return parsePrice(price)
    }
}

data class TodayPrice(
    val trend: String,
    val price: String
) {
    fun getNumericPrice(): Double {
        return parsePrice(price)
    }
}

data class TrendChange(
    val trend: String,
    val change: String
)

data class PriceHistory(
    val date: String,
    val price: Int
)

// Utility function to parse price strings
fun parsePrice(price: String): Double {
    // Remove spaces and other unwanted characters
    val cleanedPrice = price.replace(" ", "").trim()
    Log.d("PriceDebug", "Original Price: $price, Cleaned Price: $cleanedPrice")

    return when {
        cleanedPrice.endsWith("m", ignoreCase = true) -> {
            // If the price ends with "m", assume it's in millions
            cleanedPrice.dropLast(1).toDoubleOrNull()?.times(1_000_000) ?: 0.0
        }
        cleanedPrice.endsWith("k", ignoreCase = true) -> {
            // If the price ends with "k", assume it's in thousands
            cleanedPrice.dropLast(1).toDoubleOrNull()?.times(1_000) ?: 0.0
        }
        else -> {
            // Parse the cleaned price directly as a number
            cleanedPrice.toDoubleOrNull() ?: 0.0
        }
    }
}