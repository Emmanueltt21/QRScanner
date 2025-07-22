package com.kottland.qrcanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.kottland.qrcanner.data.AppDatabase
import com.kottland.qrcanner.model.ScanResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val scanResultDao = AppDatabase.getDatabase(application).scanResultDao()
    val history: Flow<List<ScanResult>> = scanResultDao.getAll()
    
    private val _selectedItem = mutableStateOf<ScanResult?>(null)
    val selectedItem: State<ScanResult?> = _selectedItem

    fun getById(id: Int): ScanResult? {
        // This is a simplified approach - in a real app, you'd want to use a suspend function
        // and collect from a Flow, but for this demo we'll return null and handle it in the UI
        return null
    }
    
    fun deleteById(id: Int) {
        viewModelScope.launch {
            scanResultDao.deleteById(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            scanResultDao.deleteAll()
        }
    }
}
