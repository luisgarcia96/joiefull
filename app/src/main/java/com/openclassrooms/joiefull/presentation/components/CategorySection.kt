package com.openclassrooms.joiefull.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.openclassrooms.joiefull.domain.model.ClothingItem
import com.openclassrooms.joiefull.presentation.home.CategorySectionUiModel
import com.openclassrooms.joiefull.presentation.home.HomeTextStyles
import kotlin.math.min

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySection(
  section: CategorySectionUiModel,
  columns: Int,
  textStyles: HomeTextStyles,
  modifier: Modifier = Modifier,
  useGridLayout: Boolean = false,
  fixedCardWidth: Dp? = null,
  onProductClick: (ClothingItem) -> Unit,
  onToggleFavorite: (ClothingItem) -> Unit
) {
  val horizontalPadding = 16.dp
  val spacing = 12.dp

  Text(
    text = section.category.displayName,
    style = textStyles.sectionTitle,
    modifier = Modifier
      .padding(horizontal = horizontalPadding)
      .semantics { heading() }
  )
  Spacer(modifier = Modifier.height(8.dp))
  BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
    val peekOffset = 48.dp
    val columnsForWidth = if (useGridLayout) {
      min(columns, section.articles.size.coerceAtLeast(1))
    } else {
      columns
    }
    val cardWidth = fixedCardWidth ?: run {
      val availableWidth = (maxWidth - (horizontalPadding * 2) - spacing * (columnsForWidth - 1)).coerceAtLeast(0.dp)
      val baseCardWidth = if (columnsForWidth > 1) {
        availableWidth / columnsForWidth
      } else {
        if (useGridLayout) availableWidth else (availableWidth - peekOffset).coerceAtLeast(0.dp)
      }
      val rowCardWidth = (baseCardWidth * CARD_WIDTH_FACTOR).coerceAtLeast(0.dp)
      if (useGridLayout) baseCardWidth else rowCardWidth
    }

    if (section.articles.isEmpty()) {
      Text(
        text = "Aucun article dans ${section.category.displayName}",
        style = textStyles.emptyState,
        modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 8.dp)
      )
    } else if (useGridLayout) {
      FlowRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        section.articles.forEach { item ->
          ProductCard(
            item = item,
            modifier = Modifier.width(cardWidth),
            textStyles = textStyles,
            onClick = onProductClick,
            onToggleFavorite = onToggleFavorite
          )
        }
      }
    } else {
      LazyRow(
        modifier = Modifier.fillMaxWidth(),
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
  }
  Spacer(modifier = Modifier.height(24.dp))
}

const val CARD_WIDTH_FACTOR = 0.8f
