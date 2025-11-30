package com.openclassrooms.joiefull.data.repository

import com.openclassrooms.joiefull.data.local.ClothingLocalDataSource
import com.openclassrooms.joiefull.data.remote.ClothingRemoteDataSource
import com.openclassrooms.joiefull.data.remote.ClothingService
import com.openclassrooms.joiefull.data.remote.model.ClothingItemDto
import com.openclassrooms.joiefull.data.remote.model.RatingDto
import com.openclassrooms.joiefull.domain.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClothingRepositoryImplTest {

  @Test
  fun `toggle favorite adjusts likes up and down`() = runTest {
    val dispatcher = StandardTestDispatcher(testScheduler)
    val repository = repository(dispatcher = dispatcher, baseLikes = 10)

    advanceUntilIdle()

    val initial = repository.getClothingDetails("item-1")!!
    assertEquals(10, initial.likes)
    assertFalse(initial.isFavorite)

    repository.toggleFavorite("item-1")
    advanceUntilIdle()
    val favorited = repository.getClothingDetails("item-1")!!
    assertTrue(favorited.isFavorite)
    assertEquals(11, favorited.likes)

    repository.toggleFavorite("item-1")
    advanceUntilIdle()
    val unfavorited = repository.getClothingDetails("item-1")!!
    assertFalse(unfavorited.isFavorite)
    assertEquals(10, unfavorited.likes)
  }

  @Test
  fun `register share increments session share count`() = runTest {
    val dispatcher = StandardTestDispatcher(testScheduler)
    val repository = repository(dispatcher = dispatcher, baseShares = 3)

    advanceUntilIdle()

    val initial = repository.getClothingDetails("item-1")!!
    assertEquals(3, initial.shareCount)

    repository.registerShare("item-1")
    advanceUntilIdle()
    val afterShare = repository.getClothingDetails("item-1")!!
    assertEquals(4, afterShare.shareCount)
  }

  @Test
  fun `saving rating merges with remote rating and keeps user rating`() = runTest {
    val dispatcher = StandardTestDispatcher(testScheduler)
    val repository = repository(
      dispatcher = dispatcher,
      baseRating = RatingDto(value = 4.0f, count = 2)
    )

    advanceUntilIdle()

    repository.saveRating(id = "item-1", rating = 5f, comment = "Great item")
    advanceUntilIdle()

    val updated = repository.getClothingDetails("item-1")!!
    // Aggregated rating: (4.0 * 2 + 5) / 3 = 4.333...
    assertEquals(4.33f, updated.rating.value, 0.01f)
    assertEquals(3, updated.rating.count)
    assertEquals(5f, updated.userRating)
    assertEquals("Great item", updated.userComment)
  }

  private fun repository(
    dispatcher: CoroutineDispatcher,
    baseLikes: Int = 0,
    baseShares: Int = 0,
    baseRating: RatingDto = RatingDto(value = 0f, count = 0)
  ): ClothingRepositoryImpl {
    val service = FakeClothingService(
      listOf(
        ClothingItemDto(
          id = "item-1",
          name = "Test item",
          description = "Desc",
          price = 10.0,
          originalPrice = 12.0,
          imageUrl = "https://example.com/img.jpg",
          category = Category.TOPS,
          rating = baseRating,
          likes = baseLikes,
          shares = baseShares
        )
      )
    )
    val remote = ClothingRemoteDataSource(service)
    val local = ClothingLocalDataSource()
    return ClothingRepositoryImpl(
      remoteDataSource = remote,
      localDataSource = local,
      dispatcher = dispatcher
    )
  }

  private class FakeClothingService(
    private val items: List<ClothingItemDto>
  ) : ClothingService {
    override suspend fun getClothingItems(): List<ClothingItemDto> = items
  }
}
