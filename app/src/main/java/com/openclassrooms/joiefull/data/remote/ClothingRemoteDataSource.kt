package com.openclassrooms.joiefull.data.remote

import com.openclassrooms.joiefull.data.remote.model.ClothingItemDto

class ClothingRemoteDataSource(
  private val service: ClothingService
) {
  suspend fun fetchClothingItems(): List<ClothingItemDto> = service.getClothingItems()
}
