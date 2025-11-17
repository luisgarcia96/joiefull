package com.openclassrooms.joiefull.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.openclassrooms.joiefull.di.AppContainer
import com.openclassrooms.joiefull.presentation.details.DetailsScreen
import com.openclassrooms.joiefull.presentation.details.DetailsViewModel
import com.openclassrooms.joiefull.presentation.home.HomeScreen
import com.openclassrooms.joiefull.presentation.home.HomeViewModel

@Composable
fun JoiefullNavHost(
  windowSizeClass: WindowSizeClass,
  appContainer: AppContainer
) {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = NavRoutes.HOME) {
    composable(NavRoutes.HOME) {
      val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(appContainer)
      )
      val state by homeViewModel.uiState.collectAsStateWithLifecycle()
      HomeScreen(
        uiState = state,
        windowSizeClass = windowSizeClass,
        onProductClick = { navController.navigate(NavRoutes.detailsRoute(it.id)) },
        onToggleFavorite = { homeViewModel.toggleFavorite(it) }
      )
    }
    composable(
      route = "${NavRoutes.DETAILS}/{${NavRoutes.DETAILS_ARG_ID}}",
      arguments = listOf(
        navArgument(NavRoutes.DETAILS_ARG_ID) { type = NavType.StringType }
      )
    ) { entry ->
      val itemId = entry.arguments?.getString(NavRoutes.DETAILS_ARG_ID) ?: return@composable
      val detailsViewModel: DetailsViewModel = viewModel(
        factory = DetailsViewModel.provideFactory(
          appContainer = appContainer,
          itemId = itemId
        )
      )
      val state by detailsViewModel.uiState.collectAsStateWithLifecycle()
      DetailsScreen(
        uiState = state,
        windowSizeClass = windowSizeClass,
        onBack = { navController.popBackStack() },
        onShare = { context ->
          detailsViewModel.shareItem(context)
        },
        onRatingSelected = { rating -> detailsViewModel.updateRating(rating) },
        onCommentChanged = { detailsViewModel.updateComment(it) },
        onSaveRating = { detailsViewModel.saveRating() },
        onToggleFavorite = { detailsViewModel.toggleFavorite() }
      )
    }
  }
}
