package com.openclassrooms.joiefull.di

import com.openclassrooms.joiefull.data.local.ClothingLocalDataSource
import com.openclassrooms.joiefull.data.remote.ClothingRemoteDataSource
import com.openclassrooms.joiefull.data.remote.HttpClothingService
import com.openclassrooms.joiefull.data.repository.ClothingRepositoryImpl
import com.openclassrooms.joiefull.domain.usecase.GetClothesUseCase
import com.openclassrooms.joiefull.domain.usecase.GetClothingDetailsUseCase
import com.openclassrooms.joiefull.domain.usecase.RegisterShareUseCase
import com.openclassrooms.joiefull.domain.usecase.SaveRatingUseCase
import com.openclassrooms.joiefull.domain.usecase.ToggleFavoriteUseCase

/**
 * Very small Service Locator used to keep the sample self-contained.
 */
class AppContainer {
  private val remoteService = HttpClothingService()
  private val remoteDataSource = ClothingRemoteDataSource(remoteService)
  private val localDataSource = ClothingLocalDataSource()

  private val clothingRepository = ClothingRepositoryImpl(
    remoteDataSource = remoteDataSource,
    localDataSource = localDataSource
  )

  val getClothesUseCase = GetClothesUseCase(clothingRepository)
  val getClothingDetailsUseCase = GetClothingDetailsUseCase(clothingRepository)
  val saveRatingUseCase = SaveRatingUseCase(clothingRepository)
  val toggleFavoriteUseCase = ToggleFavoriteUseCase(clothingRepository)
  val registerShareUseCase = RegisterShareUseCase(clothingRepository)
}
