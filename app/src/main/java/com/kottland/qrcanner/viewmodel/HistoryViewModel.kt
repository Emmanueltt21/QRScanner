package com.kottland.qrcanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kottland.qrcanner.data.AppDatabase
import com.kottland.qrcanner.model.ScanResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val scanResultDao = AppDatabase.getDatabase(application).scanResultDao()
    val history: Flow<List<ScanResult>> = scanResultDao.getAll()

    fun clearHistory() {
        viewModelScope.launch {
            scanResultDao.deleteAll()
        }
    }
}
