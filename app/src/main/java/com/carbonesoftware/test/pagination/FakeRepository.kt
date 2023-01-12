package com.carbonesoftware.test.pagination

import kotlinx.coroutines.delay

data class FakeItem(
    val title: String,
    val description: String)

class FakeRepository {
    private val remoteDataSource = (1..100).map {
        FakeItem(title = "Item Title $it", description = "Item description #$it")
    }

    /**
     * Simula una peticion con delay
     */
    suspend fun getItems(page: Int, pageSize: Int): Result<List<FakeItem>>{
        delay(2000L)
        val startingIndex = page * pageSize
        return if(startingIndex + pageSize <= remoteDataSource.size){
            Result.success(
                remoteDataSource.slice(startingIndex until startingIndex + pageSize)
            )
        } else Result.success(emptyList())
    }
}