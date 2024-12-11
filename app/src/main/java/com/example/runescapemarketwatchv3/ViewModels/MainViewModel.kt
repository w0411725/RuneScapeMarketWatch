package com.example.runescapemarketwatchv3.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.runescapemarketwatchv3.Item
import com.example.runescapemarketwatchv3.Repositories.ItemRepository
import com.example.runescapemarketwatchv3.database.toDomainItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class SortOrder {
    PriceLowToHigh,
    PriceHighToLow,
    Name,
    Trend
}

class MainViewModel(private val repository: ItemRepository) : ViewModel() {

    private val _itemDetails = MutableStateFlow<Item?>(null)
    val itemDetails: StateFlow<Item?> get() = _itemDetails

    private val _allItems = MutableStateFlow<List<Item>>(emptyList())
    val allItems: StateFlow<List<Item>> get() = _allItems

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isFetchingMore = MutableStateFlow(false)
    val isFetchingMore: StateFlow<Boolean> get() = _isFetchingMore

    private val _sortOrder = MutableStateFlow(SortOrder.PriceHighToLow)
    val sortOrder: StateFlow<SortOrder> get() = _sortOrder

    private var lastFetchedId = 0 // Tracks the last item ID fetched
    private val batchSize = 20   // Number of items to fetch per batch

    // Fetch item details by ID
    fun fetchItemDetails(itemId: Int) {
        viewModelScope.launch {
            val roomItem = repository.getItemById(itemId)
            _itemDetails.value = roomItem?.toDomainItem() // Convert RoomItem to Domain Item
        }
    }

    // Fetch initial items
    fun fetchInitialItems() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val roomItems = repository.getSortedItems(_sortOrder.value)
                    .filter { it.price > 0 } // Exclude items with price 0
                    .take(batchSize)

                lastFetchedId = roomItems.lastOrNull()?.id ?: 0
                _allItems.value = roomItems.map { it.toDomainItem() }
            } catch (e: Exception) {
                Log.e("FetchError", "Error fetching initial items", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Load more items when triggered by the UI
    fun fetchRemainingItems() {
        if (_isFetchingMore.value) return // Prevent multiple overlapping fetches

        _isFetchingMore.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val roomItems = repository.getItemsBatch(lastFetchedId + 1, batchSize)
                    .filter { it.price > 0 } // Exclude items with price 0

                if (roomItems.isNotEmpty()) {
                    lastFetchedId = roomItems.last().id
                    _allItems.value = _allItems.value + roomItems.map { it.toDomainItem() }
                }
            } catch (e: Exception) {
                Log.e("FetchError", "Error fetching remaining items", e)
            } finally {
                _isFetchingMore.value = false
            }
        }
    }

    // Update sort order
    fun updateSortOrder(order: SortOrder) {
        _sortOrder.value = order
        fetchInitialItems() // Re-fetch items with new sort order
    }

    // Start continuous seeding in the background
    fun startSeeding() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.startContinuousSeeding()
        }
    }
}


class MainViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
