package com.openclassrooms.joiefull.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.openclassrooms.joiefull.domain.model.ClothingItem
import com.openclassrooms.joiefull.presentation.components.CategorySection
import androidx.compose.ui.unit.Dp

@Composable
fun HomeScreen(
  uiState: HomeUiState,
  windowSizeClass: WindowSizeClass,
  onProductClick: (ClothingItem) -> Unit,
  onToggleFavorite: (ClothingItem) -> Unit,
  modifier: Modifier = Modifier,
  reservedEndSpace: Dp = 0.dp,
  cardWidthOverride: Dp? = null
) {
  val configuration = LocalConfiguration.current
  val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
  val isTabletWidth = configuration.screenWidthDp >= 600
  val isTabletLandscape = isLandscape && isTabletWidth
  val columns = when {
    isTabletLandscape -> 4
    windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded -> 4
    windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium -> 2
    else -> 1
  }
  val textStyles = rememberHomeTextStyles(windowSizeClass)

  Scaffold(
    modifier = modifier,
    topBar = {},
    containerColor = Color.White,
    contentColor = MaterialTheme.colorScheme.onSurface
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
        modifier = Modifier
          .fillMaxSize()
          .padding(end = reservedEndSpace),
        contentPadding = PaddingValues(
          top = innerPadding.calculateTopPadding() + 16.dp,
          bottom = innerPadding.calculateBottomPadding() + 32.dp
        )
      ) {
        items(uiState.sections, key = { it.category.name }) { section ->
          CategorySection(
            section = section,
            columns = columns,
            textStyles = textStyles,
            useGridLayout = false,
            fixedCardWidth = cardWidthOverride,
            onProductClick = onProductClick,
            onToggleFavorite = onToggleFavorite
          )
        }
      }
    }
  }
}
