package com.openclassrooms.joiefull.domain.repository

import com.openclassrooms.joiefull.domain.model.ClothingItem
import kotlinx.coroutines.flow.Flow

interface ClothingRepository {
  fun getClothingItems(): Flow<List<ClothingItem>>
  suspend fun getClothingDetails(id: String): ClothingItem?
  suspend fun saveRating(id: String, rating: Float, comment: String)
  suspend fun toggleFavorite(id: String): ClothingItem?
  suspend fun registerShare(id: String): ClothingItem?
}
