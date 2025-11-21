package com.openclassrooms.joiefull.data.remote

import com.openclassrooms.joiefull.data.remote.model.ClothingItemDto
import com.openclassrooms.joiefull.data.remote.model.RatingDto
import com.openclassrooms.joiefull.domain.model.Category

/**
 * Fake remote service returning static data. Later, this can be replaced with a Retrofit service.
 */
class FakeClothingService : ClothingService {
  override suspend fun getClothingItems(): List<ClothingItemDto> = clothingItemsMock
}

private val clothingItemsMock = listOf(
  ClothingItemDto(
    id = "top_1",
    name = "Chemise Lin Aurore",
    description = "Chemise oversize en lin biologique, respirante et douce pour l'été.",
    price = 89.0,
    originalPrice = 129.0,
    imageUrl = "https://picsum.photos/400?random=1",
    category = Category.TOPS,
    rating = RatingDto(value = 4.5f, count = 120),
    likes = 96
  ),
  ClothingItemDto(
    id = "top_2",
    name = "Pull Côtelé Nuit",
    description = "Pull côtelé col montant en laine mérinos certifiée RWS.",
    price = 119.0,
    originalPrice = 139.0,
    imageUrl = "https://picsum.photos/400?random=2",
    category = Category.TOPS,
    rating = RatingDto(value = 4.8f, count = 87),
    likes = 132
  ),
  ClothingItemDto(
    id = "bottom_1",
    name = "Pantalon Voyage",
    description = "Pantalon fluide coupe droite avec ceinture ajustable et poches profondes.",
    price = 99.0,
    originalPrice = 149.0,
    imageUrl = "https://picsum.photos/400?random=3",
    category = Category.BOTTOMS,
    rating = RatingDto(value = 4.3f, count = 64),
    likes = 78
  ),
  ClothingItemDto(
    id = "bottom_2",
    name = "Jupe Mistral",
    description = "Jupe portefeuille midi, tissu satiné infroissable pour vos journées actives.",
    price = 79.0,
    originalPrice = 109.0,
    imageUrl = "https://picsum.photos/400?random=4",
    category = Category.BOTTOMS,
    rating = RatingDto(value = 4.0f, count = 45),
    likes = 54
  ),
  ClothingItemDto(
    id = "bag_1",
    name = "Cabas Horizon",
    description = "Cabas vegan convertible en sac à dos, compartiment ordinateur 14 pouces.",
    price = 159.0,
    originalPrice = 189.0,
    imageUrl = "https://picsum.photos/400?random=5",
    category = Category.BAGS,
    rating = RatingDto(value = 4.9f, count = 210),
    likes = 201
  ),
  ClothingItemDto(
    id = "bag_2",
    name = "Mini Sac Mystère",
    description = "Mini sac bandoulière modulable avec chaîne amovible et poches RFID.",
    price = 129.0,
    originalPrice = 149.0,
    imageUrl = "https://picsum.photos/400?random=6",
    category = Category.BAGS,
    rating = RatingDto(value = 4.4f, count = 74),
    likes = 88
  )
)
