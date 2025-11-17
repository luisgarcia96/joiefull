package com.openclassrooms.joiefull.data.remote

import com.openclassrooms.joiefull.data.remote.model.ClothingItemDto

interface ClothingService {
  suspend fun getClothingItems(): List<ClothingItemDto>
}
