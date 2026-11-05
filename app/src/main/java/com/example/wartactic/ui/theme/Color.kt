package com.example.wartactic.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * SISTEMA DE COLORIMETRÍA TÁCTICA (DESIGN SYSTEM)
 *
 * En Compose, los colores se instancian con la clase `Color(0xFF......)`.
 * Notación hexadecimal ARGB:
 * - `0xFF`: Canal Alfa (Opacidad 100%).
 * - Red (Rojo), Green (Verde), Blue (Azul) en hexadecimal (00 a FF).
 */

// --- PALETA PRINCIPAL DE MARCA Y ESTADO ---
// Color primario: Verde lima militar para elementos activos, selección y logotipos
val TacticalLime = Color(0xFFCCFF00)

// Fondo general: Verde militar ultra oscuro (casi negro) para mantener la estética nocturna
val TacticalDarkBg = Color(0xFF0D130B)

// Color secundario: Naranja de alerta para el modo RADAR, mirilla y acciones críticas
val TacticalOrangeAlert = Color(0xFFFF5E36)

// Color terciario: Crema suave para botones secundarios (Rotar Pieza) facilitando contraste
val TacticalCream = Color(0xFFECE8D0)

// --- PALETA DE LA CUADRÍCULA (CHECKERBOARD) ---
// Tonos verde oliva oscuros intercalados para generar el efecto ajedrezado del tablero 8x8
val CellDarkGreen = Color(0xFF1D2919)   // Casilla impar
val CellLightGreen = Color(0xFF23311F)  // Casilla par