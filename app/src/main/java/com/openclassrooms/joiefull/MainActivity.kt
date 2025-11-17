package com.openclassrooms.joiefull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.openclassrooms.joiefull.di.AppContainer
import com.openclassrooms.joiefull.navigation.JoiefullNavHost
import com.openclassrooms.joiefull.presentation.theme.JoiefullTheme

class MainActivity : ComponentActivity() {
  private val appContainer: AppContainer by lazy { AppContainer() }

  @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val windowSizeClass = calculateWindowSizeClass(activity = this)
      JoiefullTheme {
        JoiefullNavHost(
          windowSizeClass = windowSizeClass,
          appContainer = appContainer
        )
      }
    }
  }
}
