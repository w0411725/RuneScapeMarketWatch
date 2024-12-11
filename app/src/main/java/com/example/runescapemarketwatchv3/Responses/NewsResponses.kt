package com.example.runescapemarketwatchv3

data class MarketNews(
    val id: Int,
    val title: String,
    val description: String,
    val date: String, // Format: "YYYY-MM-DD"
    val type: String // e.g., "Item Release", "Event", "Economy Update"
)
