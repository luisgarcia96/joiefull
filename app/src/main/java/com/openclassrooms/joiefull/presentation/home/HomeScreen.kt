package com.openclassrooms.joiefull.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.openclassrooms.joiefull.domain.model.ClothingItem
import com.openclassrooms.joiefull.presentation.components.AppTopBar
import com.openclassrooms.joiefull.presentation.components.CategorySection

@Composable
fun HomeScreen(
  uiState: HomeUiState,
  windowSizeClass: WindowSizeClass,
  onProductClick: (ClothingItem) -> Unit,
  onToggleFavorite: (ClothingItem) -> Unit
) {
  val columns = when (windowSizeClass.widthSizeClass) {
    WindowWidthSizeClass.Compact -> 1
    WindowWidthSizeClass.Medium -> 2
    else -> 4
  }

  Scaffold(
    topBar = { AppTopBar(title = "Joiefull") }
  ) { innerPadding ->
    if (uiState.isLoading) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        CircularProgressIndicator(
          modifier = Modifier
            .align(Alignment.Center)
            .semantics { contentDescription = "Chargement des vêtements" },
          strokeWidth = 4.dp
        )
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
          top = innerPadding.calculateTopPadding() + 16.dp,
          bottom = innerPadding.calculateBottomPadding() + 32.dp
        )
      ) {
        items(uiState.sections, key = { it.category.name }) { section ->
          CategorySection(
            section = section,
            columns = columns,
            onProductClick = onProductClick,
            onToggleFavorite = onToggleFavorite
          )
        }
      }
    }
  }
}
