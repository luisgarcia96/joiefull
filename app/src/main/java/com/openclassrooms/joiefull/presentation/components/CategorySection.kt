package com.openclassrooms.joiefull.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.openclassrooms.joiefull.domain.model.ClothingItem
import com.openclassrooms.joiefull.presentation.home.CategorySectionUiModel
import com.openclassrooms.joiefull.presentation.home.HomeTextStyles

@Composable
fun CategorySection(
  section: CategorySectionUiModel,
  columns: Int,
  textStyles: HomeTextStyles,
  modifier: Modifier = Modifier,
  onProductClick: (ClothingItem) -> Unit,
  onToggleFavorite: (ClothingItem) -> Unit
) {
  val density = LocalDensity.current
  val windowInfo = LocalWindowInfo.current
  val screenWidth = with(density) { windowInfo.containerSize.width.toDp() }
  val horizontalPadding = 16.dp
  val spacing = 12.dp
  val availableWidth = (screenWidth - (horizontalPadding * 2) - spacing * (columns - 1)).coerceAtLeast(0.dp)
  val peekOffset = 48.dp
  val baseCardWidth = if (columns > 1) {
    availableWidth / columns
  } else {
    (availableWidth - peekOffset).coerceAtLeast(0.dp)
  }
  val cardWidth = (baseCardWidth * CARD_WIDTH_FACTOR).coerceAtLeast(0.dp)

  Text(
    text = section.category.displayName,
    style = textStyles.sectionTitle,
    modifier = Modifier
      .padding(horizontal = horizontalPadding)
      .semantics { heading() }
  )
  Spacer(modifier = Modifier.height(8.dp))
  if (section.articles.isEmpty()) {
    Text(
      text = "Aucun article dans ${section.category.displayName}",
      style = textStyles.emptyState,
      modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 8.dp)
    )
  } else {
    LazyRow(
      modifier = modifier.fillMaxWidth(),
      contentPadding = PaddingValues(horizontal = horizontalPadding),
      horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
      items(section.articles, key = { it.id }) { item ->
        ProductCard(
          item = item,
          modifier = Modifier.width(cardWidth),
          textStyles = textStyles,
          onClick = onProductClick,
          onToggleFavorite = onToggleFavorite
        )
      }
    }
  }
  Spacer(modifier = Modifier.height(24.dp))
}

private const val CARD_WIDTH_FACTOR = 0.8f
