package com.kottland.qrcanner.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kottland.qrcanner.model.ScanResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScanResultDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var scanResultDao: ScanResultDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        scanResultDao = database.scanResultDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetScanResult() = runBlocking {
        val scanResult = ScanResult(content = "test", timestamp = 123)
        scanResultDao.insert(scanResult)
        val allScanResults = scanResultDao.getAll().first()
        assert(allScanResults.contains(scanResult))
    }

    @Test
    fun deleteAllScanResults() = runBlocking {
        val scanResult1 = ScanResult(content = "test1", timestamp = 123)
        val scanResult2 = ScanResult(content = "test2", timestamp = 456)
        scanResultDao.insert(scanResult1)
        scanResultDao.insert(scanResult2)
        scanResultDao.deleteAll()
        val allScanResults = scanResultDao.getAll().first()
        assert(allScanResults.isEmpty())
    }
}
