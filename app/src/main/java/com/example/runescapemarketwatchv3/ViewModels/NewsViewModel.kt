package com.example.runescapemarketwatchv3.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.runescapemarketwatchv3.Item
import com.example.runescapemarketwatchv3.MarketNews
import com.example.runescapemarketwatchv3.Repositories.ItemRepository
import com.example.runescapemarketwatchv3.Repositories.MarketNewsRepository
import com.example.runescapemarketwatchv3.database.toDomainItem
import com.example.runescapemarketwatchv3.database.toRoomItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MarketNewsViewModel(private val repository: MarketNewsRepository) : ViewModel() {
    private val _marketNews = MutableStateFlow<List<MarketNews>>(emptyList())
    val marketNews: StateFlow<List<MarketNews>> = _marketNews

    init {
        fetchMarketNews()
    }

    private fun fetchMarketNews() {
        viewModelScope.launch {
            val news = repository.getMarketNews()
            _marketNews.value = news
        }
    }

    fun getNewsById(newsId: Int): MarketNews {
        return repository.getNewsById(newsId)
            ?: throw IllegalArgumentException("News item with ID $newsId not found")
    }

    // Add a new hard-coded news item
    fun addNews(news: MarketNews) {
        repository.addNews(news)
        fetchMarketNews() // Refresh the list
    }
}


class MarketNewsViewModelFactory(private val repository: MarketNewsRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarketNewsViewModel::class.java)) {
            return MarketNewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



