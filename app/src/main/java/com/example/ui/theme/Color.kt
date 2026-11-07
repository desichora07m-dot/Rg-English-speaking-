package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Indigo & Violet
val IndigoPrimary = Color(0xFF4F46E5)
val IndigoDark = Color(0xFF3730A3)
val IndigoLight = Color(0xFF818CF8)
val IndigoContainer = Color(0xFFEEF2FF)
val OnIndigoContainer = Color(0xFF312E81)

// Soft Cyan Accents
val CyanAccent = Color(0xFF06B6D4)
val CyanLight = Color(0xFF67E8F9)
val CyanContainer = Color(0xFFECFEFF)
val OnCyanContainer = Color(0xFF164E63)

// Soft Pink Accents
val PinkAccent = Color(0xFFEC4899)
val PinkLight = Color(0xFFF472B6)
val PinkContainer = Color(0xFFFDF2F8)
val OnPinkContainer = Color(0xFF831843)

// Success & Warning
val SuccessGreen = Color(0xFF10B981)
val SuccessContainer = Color(0xFFECFDF5)
val WarningAmber = Color(0xFFF59E0B)
val ErrorRed = Color(0xFFEF4444)
val ErrorContainer = Color(0xFFFEF2F2)

// Background & Surfaces
val BackgroundLavender = Color(0xFFF7F7FD)
val SurfaceWhite = Color(0xFFFFFFFF)
val SurfaceCard = Color(0xFFFFFFFF)
val SurfaceSoftLavender = Color(0xFFF1F0FB)

// Text Colors
val TextPrimary = Color(0xFF1E1B4B)
val TextSecondary = Color(0xFF64748B)
val TextTertiary = Color(0xFF94A3B8)
val BorderLight = Color(0xFFE2E8F0)

// Gradients
val PrimaryGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF4F46E5), Color(0xFF7C3AED))
)
val CyanPinkGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF06B6D4), Color(0xFFEC4899))
)
val HeroBannerGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF4F46E5), Color(0xFF6366F1), Color(0xFF8B5CF6))
)
val CardShimmerGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFFFFFFF), Color(0xFFF5F3FF), Color(0xFFFFFFFF))
)
val AccentOrange = Color(0xFFFF6D00)
val StreakFire = Color(0xFFFF5722)
