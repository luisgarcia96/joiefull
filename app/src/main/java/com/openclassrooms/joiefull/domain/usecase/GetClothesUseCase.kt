package com.openclassrooms.joiefull.domain.usecase

import com.openclassrooms.joiefull.domain.model.ClothingItem
import com.openclassrooms.joiefull.domain.repository.ClothingRepository
import kotlinx.coroutines.flow.Flow

class GetClothesUseCase(
  private val repository: ClothingRepository
) {
  operator fun invoke(): Flow<List<ClothingItem>> = repository.getClothingItems()
}
