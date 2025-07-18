package com.kottland.qrcanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kottland.qrcanner.data.AppDatabase
import com.kottland.qrcanner.model.ScanResult
import kotlinx.coroutines.launch

class ScannerViewModel(application: Application) : AndroidViewModel(application) {
    private val scanResultDao = AppDatabase.getDatabase(application).scanResultDao()

    fun addScanResult(content: String) {
        viewModelScope.launch {
            val scanResult = ScanResult(content = content, timestamp = System.currentTimeMillis())
            scanResultDao.insert(scanResult)
        }
    }
}
