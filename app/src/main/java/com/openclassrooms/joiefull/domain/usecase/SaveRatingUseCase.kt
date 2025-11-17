package com.openclassrooms.joiefull.domain.usecase

import com.openclassrooms.joiefull.domain.repository.ClothingRepository

class SaveRatingUseCase(
  private val repository: ClothingRepository
) {
  suspend operator fun invoke(id: String, rating: Float, comment: String) {
    repository.saveRating(id, rating, comment)
  }
}
