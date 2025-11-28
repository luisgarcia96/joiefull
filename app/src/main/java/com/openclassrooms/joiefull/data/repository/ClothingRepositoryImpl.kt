package com.openclassrooms.joiefull.data.repository

import com.openclassrooms.joiefull.data.local.ClothingLocalDataSource
import com.openclassrooms.joiefull.data.remote.ClothingRemoteDataSource
import com.openclassrooms.joiefull.data.remote.model.ClothingItemDto
import com.openclassrooms.joiefull.domain.model.ClothingItem
import com.openclassrooms.joiefull.domain.model.Rating
import com.openclassrooms.joiefull.domain.repository.ClothingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClothingRepositoryImpl(
  private val remoteDataSource: ClothingRemoteDataSource,
  private val localDataSource: ClothingLocalDataSource,
  dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ClothingRepository {

  private val scope = CoroutineScope(SupervisorJob() + dispatcher)
  private val clothingFlow = MutableStateFlow<List<ClothingItem>>(emptyList())
  private var cachedRemote: List<ClothingItemDto> = emptyList()

  init {
    scope.launch { refreshFromRemote() }
  }

  override fun getClothingItems(): Flow<List<ClothingItem>> {
    if (cachedRemote.isEmpty()) {
      scope.launch { refreshFromRemote() }
    }
    return clothingFlow.asStateFlow()
  }

  override suspend fun getClothingDetails(id: String): ClothingItem? {
    if (cachedRemote.isEmpty()) {
      refreshFromRemote()
    }
    return clothingFlow.value.firstOrNull { it.id == id }
  }

  override suspend fun saveRating(id: String, rating: Float, comment: String) {
    localDataSource.saveRating(id, rating, comment)
    rebuildFlow()
  }

  override suspend fun toggleFavorite(id: String): ClothingItem? {
    localDataSource.toggleFavorite(id)
    rebuildFlow()
    return clothingFlow.value.firstOrNull { it.id == id }
  }

  override suspend fun registerShare(id: String): ClothingItem? {
    localDataSource.incrementShare(id)
    rebuildFlow()
    return clothingFlow.value.firstOrNull { it.id == id }
  }

  private suspend fun refreshFromRemote() {
    cachedRemote = remoteDataSource.fetchClothingItems()
    rebuildFlow()
  }

  private fun rebuildFlow() {
    val updated = cachedRemote.map { it.toDomain(localDataSource) }
    clothingFlow.value = updated
  }
}

private fun ClothingItemDto.toDomain(localDataSource: ClothingLocalDataSource): ClothingItem {
  val savedRating = localDataSource.getRating(id)
  val savedVoteCount = localDataSource.getRatingVoteCount(id)
  val totalVotes = rating.count + savedVoteCount
  val aggregatedRating = if (totalVotes > 0) {
    val baseTotal = rating.value * rating.count
    val localTotal = savedRating?.rating ?: 0f
    (baseTotal + localTotal) / totalVotes
  } else {
    0f
  }
  val totalShares = shares + localDataSource.getShareCount(id)
  val isFavorite = localDataSource.isFavorite(id)
  val likesWithFavorite = likes + if (isFavorite) 1 else 0
  return ClothingItem(
    id = id,
    name = name,
    description = description,
    price = price,
    originalPrice = originalPrice,
    imageUrl = imageUrl,
    category = category,
    rating = Rating(value = aggregatedRating, count = totalVotes),
    userRating = savedRating?.rating,
    likes = likesWithFavorite,
    shareCount = totalShares,
    isFavorite = isFavorite,
    userComment = savedRating?.comment
  )
}
