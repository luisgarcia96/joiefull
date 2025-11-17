package com.openclassrooms.joiefull.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.openclassrooms.joiefull.di.AppContainer
import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.domain.model.ClothingItem
import com.openclassrooms.joiefull.domain.usecase.GetClothesUseCase
import com.openclassrooms.joiefull.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class CategorySectionUiModel(
  val category: Category,
  val articles: List<ClothingItem>
)

data class HomeUiState(
  val isLoading: Boolean = true,
  val sections: List<CategorySectionUiModel> = emptyList()
)

class HomeViewModel(
  private val getClothesUseCase: GetClothesUseCase,
  private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

  private val _uiState = MutableStateFlow(HomeUiState())
  val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
  private var hasDisplayedData = false

  init {
    observeClothes()
  }

  private fun observeClothes() {
    viewModelScope.launch {
      getClothesUseCase().collectLatest { items ->
        val sections = Category.entries.map { category ->
          CategorySectionUiModel(
            category = category,
            articles = items.filter { it.category == category }
          )
        }
        val stillLoading = !hasDisplayedData && items.isEmpty()
        if (!stillLoading) {
          hasDisplayedData = true
        }
        _uiState.value = HomeUiState(isLoading = stillLoading, sections = sections)
      }
    }
  }

  fun toggleFavorite(item: ClothingItem) {
    viewModelScope.launch { toggleFavoriteUseCase(item.id) }
  }

  companion object {
    fun provideFactory(appContainer: AppContainer): ViewModelProvider.Factory =
      object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          @Suppress("UNCHECKED_CAST")
          return HomeViewModel(
            getClothesUseCase = appContainer.getClothesUseCase,
            toggleFavoriteUseCase = appContainer.toggleFavoriteUseCase
          ) as T
        }
      }
  }
}
