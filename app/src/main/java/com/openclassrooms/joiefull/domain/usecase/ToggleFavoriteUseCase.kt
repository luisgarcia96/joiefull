package com.openclassrooms.joiefull.domain.usecase

import com.openclassrooms.joiefull.domain.model.ClothingItem
import com.openclassrooms.joiefull.domain.repository.ClothingRepository

class ToggleFavoriteUseCase(
  private val repository: ClothingRepository
) {
  suspend operator fun invoke(id: String): ClothingItem? = repository.toggleFavorite(id)
}
