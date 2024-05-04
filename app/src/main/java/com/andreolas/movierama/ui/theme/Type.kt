package com.andreolas.movierama.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.andreolas.movierama.R

private val appFontFamily = FontFamily(
  Font(
    resId = R.font.font_light,
    weight = FontWeight.W300, // FontWeight.Light
  ),
  Font(
    resId = R.font.font_regular,
    weight = FontWeight.W400, // FontWeight.Normal
  ),
  Font(
    resId = R.font.font_medium,
    weight = FontWeight.W500, // FontWeight.Medium
  )
)

val AppTypography = Typography(
  labelLarge = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.Medium,
    letterSpacing = 0.sp,
    lineHeight = 20.sp,
    fontSize = 14.sp
  ),
  labelMedium = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.Medium,
    letterSpacing = 0.10000000149011612.sp,
    lineHeight = 16.sp,
    fontSize = 12.sp
  ),
  labelSmall = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.Medium,
    letterSpacing = 0.10000000149011612.sp,
    lineHeight = 16.sp,
    fontSize = 11.sp
  ),
  bodyLarge = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.W400,
    letterSpacing = 0.sp,
    lineHeight = 24.sp,
    fontSize = 16.sp
  ),
  bodyMedium = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.W400,
    letterSpacing = 0.sp,
    lineHeight = 20.sp,
    fontSize = 14.sp
  ),
  bodySmall = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.W400,
    letterSpacing = 0.10000000149011612.sp,
    lineHeight = 16.sp,
    fontSize = 12.sp
  ),
  headlineLarge = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.W400,
    letterSpacing = 0.sp,
    lineHeight = 40.sp,
    fontSize = 32.sp
  ),
  headlineMedium = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.W400,
    letterSpacing = 0.sp,
    lineHeight = 36.sp,
    fontSize = 28.sp
  ),
  headlineSmall = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.W400,
    letterSpacing = 0.sp,
    lineHeight = 32.sp,
    fontSize = 24.sp
  ),
  displayLarge = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.W400,
    letterSpacing = 0.sp,
    lineHeight = 64.sp,
    fontSize = 57.sp
  ),
  displayMedium = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.W400,
    letterSpacing = 0.sp,
    lineHeight = 52.sp,
    fontSize = 45.sp
  ),
  displaySmall = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.W400,
    letterSpacing = 0.sp,
    lineHeight = 44.sp,
    fontSize = 36.sp
  ),
  titleLarge = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.W400,
    letterSpacing = 0.sp,
    lineHeight = 28.sp,
    fontSize = 22.sp
  ),
  titleMedium = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.Medium,
    letterSpacing = 0.sp,
    lineHeight = 24.sp,
    fontSize = 18.sp
  ),
  titleSmall = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = FontWeight.Medium,
    letterSpacing = 0.sp,
    lineHeight = 20.sp,
    fontSize = 14.sp
  ),
)

val MD2Typography = androidx.compose.material.Typography(
  defaultFontFamily = appFontFamily,
  subtitle1 = AppTypography.bodyMedium,
  caption = AppTypography.bodySmall,
)
