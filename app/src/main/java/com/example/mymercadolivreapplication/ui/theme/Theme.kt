package com.example.mymercadolivreapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Cores personalizadas baseadas na identidade visual do Mercado Libre.
 *
 * Por que definir cores personalizadas?
 * - Mantém consistência visual com a marca
 * - Garante contraste adequado para acessibilidade
 * - Facilita manutenção e mudanças futuras
 */
private val MercadoLibreYellow = Color(0xFFFFE600)
private val MercadoLibreBlue = Color(0xFF3483FA)
private val MercadoLibreGreen = Color(0xFF00A650)

/**
 * Esquema de cores para tema claro.
 * Baseado nas diretrizes do Material Design 3 com cores do Mercado Libre.
 */
private val LightColorScheme = lightColorScheme(
    primary = MercadoLibreBlue,
    onPrimary = Color.White,
    primaryContainer = MercadoLibreBlue.copy(alpha = 0.1f),
    onPrimaryContainer = MercadoLibreBlue,
    secondary = MercadoLibreYellow,
    onSecondary = Color.Black,
    secondaryContainer = MercadoLibreYellow.copy(alpha = 0.1f),
    onSecondaryContainer = Color.Black,
    tertiary = MercadoLibreGreen,
    onTertiary = Color.White,
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    error = Color(0xFFBA1A1A),
    onError = Color.White
)

/**
 * Esquema de cores para tema escuro.
 * Mantém a identidade visual mas com cores adequadas para ambientes com pouca luz.
 */
private val DarkColorScheme = darkColorScheme(
    primary = MercadoLibreBlue.copy(alpha = 0.8f),
    onPrimary = Color.White,
    primaryContainer = MercadoLibreBlue.copy(alpha = 0.2f),
    onPrimaryContainer = MercadoLibreBlue.copy(alpha = 0.8f),
    secondary = MercadoLibreYellow.copy(alpha = 0.8f),
    onSecondary = Color.Black,
    secondaryContainer = MercadoLibreYellow.copy(alpha = 0.2f),
    onSecondaryContainer = MercadoLibreYellow.copy(alpha = 0.8f),
    tertiary = MercadoLibreGreen.copy(alpha = 0.8f),
    onTertiary = Color.White,
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

/**
 * Tema principal da aplicação.
 *
 * Por que usar Material Design 3?
 * - Diretrizes modernas de design
 * - Suporte nativo a temas dinâmicos (Android 12+)
 * - Melhor acessibilidade out-of-the-box
 * - Componentes consistentes e testados
 *
 * Por que suportar tema dinâmico?
 * - Melhora a experiência do usuário no Android 12+
 * - Integração com as cores do sistema do usuário
 * - Mantém a identidade visual quando possível
 */
@Composable
fun MyMercadoLivreApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Tema dinâmico disponível no Android 12+
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = YellowCustom.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}