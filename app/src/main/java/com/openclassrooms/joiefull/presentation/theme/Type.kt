package com.openclassrooms.joiefull.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.openclassrooms.joiefull.R

private val provider = GoogleFont.Provider(
  providerAuthority = "com.google.android.gms.fonts",
  providerPackage = "com.google.android.gms",
  certificates = R.array.com_google_android_gms_fonts_certs
)

private val openSans = GoogleFont("Open Sans")

val OpenSansFontFamily = FontFamily(
  Font(googleFont = openSans, fontProvider = provider, weight = FontWeight.Normal),
  Font(googleFont = openSans, fontProvider = provider, weight = FontWeight.Medium),
  Font(googleFont = openSans, fontProvider = provider, weight = FontWeight.SemiBold)
)

val Typography = Typography(
  displayLarge = TextStyle(
    fontFamily = OpenSansFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 40.sp,
    lineHeight = 44.sp
  ),
  headlineMedium = TextStyle(
    fontFamily = OpenSansFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 22.sp,
    lineHeight = 22.sp
  ),
  titleLarge = TextStyle(
    fontFamily = OpenSansFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 14.sp
  ),
  titleMedium = TextStyle(
    fontFamily = OpenSansFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 14.sp
  ),
  bodyLarge = TextStyle(
    fontFamily = OpenSansFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 20.sp
  ),
  bodyMedium = TextStyle(
    fontFamily = OpenSansFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 14.sp
  ),
  labelLarge = TextStyle(
    fontFamily = OpenSansFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 16.sp
  )
)
