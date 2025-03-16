package com.example.wake_up_radio.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.wake_up_radio.R


// 🖋 **Custom Typography**
val CustomTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,  // Classic clean font
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,  // Uses the system default font
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Monospace,  // Gives a unique, techy look
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    )
)

// Dark Theme Colors**
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00B4D8),  // Bright Teal Blue
    secondary = Color(0xFFFFA500), // Vibrant Orange
    tertiary = Color(0xFFFFD700),  // Gold Accent

    background = Color(0xFF121212),  // True Dark
    surface = Color(0xFF1E1E1E),     // Darker Surface
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color(0xFFE0E0E0)
)

// 🌞 **Light Theme Colors**
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0077B6),  // Deep Ocean Blue
    secondary = Color(0xFFD00000), // Bold Red
    tertiary = Color(0xFFFFA500),  // Warm Orange

    background = Color(0xFFFFFBFE),
    surface = Color(0xFFF2F2F2), // Light Gray
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF121212),
    onSurface = Color(0xFF333333)
)

// 🎭 **Theme Wrapper**
@Composable
fun Wake_up_radioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,  // Using custom typography
        content = content
    )
}
