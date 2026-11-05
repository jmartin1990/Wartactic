package com.example.wartactic.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * 1. ESQUEMA DE COLOR OSCURO (Dark Theme)
 * Mapea las variables de Color.kt con los roles del sistema de diseño Material3.
 */
private val DarkColorScheme = darkColorScheme(
    primary = TacticalLime,         // Botones principales y resaltados
    secondary = TacticalOrangeAlert,// Alertas y modo Radar
    tertiary = TacticalCream,       // Botón de rotación
    background = TacticalDarkBg,    // Fondo de pantalla completa
    surface = TacticalDarkBg       // Fondo de tarjetas y paneles
)

/**
 * 2. ESQUEMA DE COLOR CLARO (Light Theme Override)
 * Se fuerzan exactamente los mismos tonos oscuros para garantizar que la app
 * mantenga SIEMPRE su estética militar nocturna aunque el usuario cambie el móvil a modo claro.
 */
private val LightColorScheme = lightColorScheme(
    primary = TacticalLime,
    secondary = TacticalOrangeAlert,
    tertiary = TacticalCream,
    background = TacticalDarkBg,
    surface = TacticalDarkBg
)

/**
 * COMPOSABLE CONTENEDOR DEL TEMA: WartacticTheme
 * Envuelve toda la jerarquía de vistas de la aplicación inyectando la paleta de colores y tipografía.
 *
 * @param darkTheme Consulta si el dispositivo está en modo oscuro (por defecto usa isSystemInDarkTheme()).
 * @param dynamicColor Desactivado por defecto (false) para que Android 12+ no sobreescriba
 *                     nuestra paleta militar con los colores del fondo de pantalla del usuario.
 * @param content Bloque de UI hijo (WartacticApp) que heredará esta configuración.
 */
@Composable
fun WartacticTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactivado para mantener el branding
    content: @Composable () -> Unit
) {
    // Selección del esquema de color en tiempo de ejecución
    val colorScheme = when {
        // Soporte opcional para Dynamic Color / Material You (Android 12+ API 31)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        // Selección según el tema del sistema (ambos mapeados a la paleta táctica)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Aplica el tema de Material3 a toda la aplicación
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}