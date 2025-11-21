package com.openclassrooms.joiefull.presentation.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

data class HomeTextStyles(
  val sectionTitle: TextStyle,
  val emptyState: TextStyle,
  val productTitle: TextStyle,
  val productPrice: TextStyle,
  val productMeta: TextStyle
)

@Composable
fun rememberHomeTextStyles(windowSizeClass: WindowSizeClass): HomeTextStyles {
  val typography = MaterialTheme.typography
  val windowInfo = LocalWindowInfo.current
  val density = LocalDensity.current

  val screenWidthDp = remember(windowInfo.containerSize, density) {
    with(density) { windowInfo.containerSize.width.toDp().value }
  }
  val widthScale = (screenWidthDp / BASELINE_SCREEN_WIDTH_DP)
    .coerceIn(0.9f, 1.2f)
  val sizeClassScale = when (windowSizeClass.widthSizeClass) {
    WindowWidthSizeClass.Compact -> 1f
    WindowWidthSizeClass.Medium -> 1.08f
    else -> 1.16f
  }
  val textScale = (widthScale * sizeClassScale).coerceIn(0.9f, 1.25f)

  return remember(textScale, typography) {
    HomeTextStyles(
      sectionTitle = typography.headlineMedium.scale(textScale),
      emptyState = typography.bodyLarge.scale(textScale),
      productTitle = typography.titleLarge.scale(textScale),
      productPrice = typography.titleMedium.scale(textScale),
      productMeta = typography.bodyMedium.scale(textScale)
    )
  }
}

private fun TextStyle.scale(factor: Float): TextStyle {
  return this.copy(
    fontSize = fontSize.scaleOrUnspecified(factor),
    lineHeight = lineHeight.scaleOrUnspecified(factor)
  )
}

private fun TextUnit.scaleOrUnspecified(factor: Float): TextUnit {
  return if (this == TextUnit.Unspecified) this else this * factor
}

private const val BASELINE_SCREEN_WIDTH_DP = 360f
