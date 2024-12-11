package com.example.runescapemarketwatchv3.Repositories

import com.example.runescapemarketwatchv3.MarketNews

class MarketNewsRepository {

    // Hardcoded list of news articles
    private val newsList = mutableListOf(
        MarketNews(1, "New Item Released", "A new rare item is now available.", "2024-12-08", "Item Release"),
        MarketNews(2, "Double XP Weekend Announced", "Players will earn double XP for all skills.", "2024-12-07", "Event"),
        MarketNews(3, "Grand Exchange Tax Update", "Taxes have been adjusted for high-value trades.", "2024-12-06", "Economy Update"),
        MarketNews(4, "Event Update", "Player Event", "2024-12-02", "A major player-driven event is underway."),
        MarketNews(5, "Economic Shift", "Market", "2024-12-03", "Prices of rare items have skyrocketed.")
    )

    // Fetch all news
    fun getMarketNews(): List<MarketNews> {
        return newsList
    }

    // Fetch a single news item by ID
    fun getNewsById(newsId: Int): MarketNews? {
        return newsList.find { it.id == newsId }
    }

    // Add a new article (hard-coded)
    fun addNews(news: MarketNews) {
        newsList.add(news)
    }
}
