package com.openclassrooms.joiefull.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.openclassrooms.joiefull.domain.model.ClothingItem
import com.openclassrooms.joiefull.presentation.home.CategorySectionUiModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySection(
  section: CategorySectionUiModel,
  columns: Int,
  modifier: Modifier = Modifier,
  onProductClick: (ClothingItem) -> Unit,
  onToggleFavorite: (ClothingItem) -> Unit
) {
  Text(
    text = section.category.displayName,
    style = MaterialTheme.typography.titleMedium,
    modifier = Modifier
      .padding(horizontal = 16.dp)
      .semantics { heading() }
  )
  Spacer(modifier = Modifier.height(8.dp))
  if (section.articles.isEmpty()) {
    Text(
      text = "Aucun article dans ${section.category.displayName}",
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
  } else {
    FlowRow(
      modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      maxItemsInEachRow = columns,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      section.articles.forEach { item ->
        ProductCard(
          item = item,
          modifier = Modifier.fillMaxWidth(fraction = 1f / columns.toFloat()),
          onClick = onProductClick,
          onToggleFavorite = onToggleFavorite
        )
      }
    }
  }
  Spacer(modifier = Modifier.height(24.dp))
}
