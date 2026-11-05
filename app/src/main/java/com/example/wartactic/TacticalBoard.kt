package com.example.wartactic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wartactic.ui.theme.*

/**
 * COMPOSABLE: TacticalBoard
 * Dibuja la cuadrícula principal 8x8 con ejes de coordenadas (A-H y 1-8).
 *
 * Sigue el patrón architectural "State Hoisting" (Elevación de Estado):
 * No maneja estados propios, sino que recibe datos de su contenedor superior (WartacticApp)
 * y le notifica eventos mediante la función lambda `onCellClick`.
 *
 * @param isRadarMode Define el modo visual (true = Radar Enemigo / false = Mi Sector).
 * @param selectedCellIndex Índice de la celda seleccionada (0 a 63).
 * @param onCellClick Callback ejecutado al hacer clic en cualquier celda.
 */
@Composable
fun TacticalBoard(
    isRadarMode: Boolean,
    selectedCellIndex: Int?,
    onCellClick: (index: Int) -> Unit
) {
    // 1. CONFIGURACIÓN DE EJES Y PALETA DE COLOR DINÁMICA
    val cols = listOf("A", "B", "C", "D", "E", "F", "G", "H")

    // Cambia el color del borde e identificadores según el modo activo
    val boardBorderColor = if (isRadarMode) TacticalOrangeAlert else TacticalLime
    val headerTextColor = if (isRadarMode) TacticalOrangeAlert else TacticalLime

    // 2. POSICIONAMIENTO MATRICIAL DE LAS PIEZAS ("MI SECTOR")
    // Se definen como conjuntos (Set) de pares (Fila, Columna) para búsquedas O(1) ultra rápidas.
    val lPieceCells = setOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(2, 1)) // Pieza L (Lanza Misiles)
    val tPieceCells = setOf(Pair(1, 4), Pair(1, 5), Pair(1, 6), Pair(2, 5)) // Pieza T (Tanque)
    val cannonCells = setOf(Pair(5, 2), Pair(5, 3))                        // Pieza Lineal (Cañón)
    val mortarCells = setOf(Pair(7, 7))                                    // Pieza Individual (Mortero)

    // 3. ESTRUCTURA DE MAQUETACIÓN GLOBAL (COLUMNA EXTERNA)
    Column(modifier = Modifier.fillMaxWidth()) {

        // ----------------------------------------------------
        // SECCIÓN A: CABECERA HORIZONTAL DE COORDENADAS (A-H)
        // ----------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // Padding lateral de 24.dp para alinear con el inicio exacto del tablero (saltando la columna de números)
                .padding(start = 24.dp, end = 24.dp, bottom = 6.dp)
        ) {
            cols.forEach { letter ->
                Text(
                    text = letter,
                    color = headerTextColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    // .weight(1f) reparte el ancho equitativamente entre las 8 letras
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // ----------------------------------------------------
        // SECCIÓN B: CUERPO PRINCIPAL (NÚMEROS 1-8 + TABLERO)
        // ----------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // IntrinsicSize.Min ajusta automáticamente la altura del eje vertical (números) a la altura real del tablero
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- B1. EJE VERTICAL DE COORDENADAS (NÚMEROS 1 a 8) ---
            Column(
                modifier = Modifier
                    .width(24.dp)
                    .fillMaxHeight(),
                // Distribuye los 8 números con espacios idénticos a lo largo de la altura del tablero
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                (1..8).forEach { number ->
                    Text(
                        text = "$number",
                        color = headerTextColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // --- B2. MARCO CONTENEDOR DEL TABLERO 8x8 ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    // aspectRatio(1f) fuerza a que el marco sea siempre un cuadrado perfecto en cualquier densidad de pantalla
                    .aspectRatio(1f)
                    .border(width = 2.dp, color = boardBorderColor)
                    .background(TacticalDarkBg)
            ) {
                // --- B3. CUADRÍCULA RECTANGULAR REUTILIZABLE (8 COLUMNAS) ---
                LazyVerticalGrid(
                    columns = GridCells.Fixed(8),
                    modifier = Modifier.fillMaxSize(),
                    // Se desactiva el scroll interno ya que el tablero entra completo en pantalla
                    userScrollEnabled = false
                ) {
                    // Genera las 64 casillas del tablero (del índice 0 al 63)
                    items(64) { index ->

                        // MATEMÁTICA DE CONVERSIÓN PLANA A MATRIZ:
                        val row = index / 8  // División entera para obtener la fila (0 a 7)
                        val col = index % 8  // Módulo para obtener la columna (0 a 7)

                        // LÓGICA PATRÓN AJEDREZADO (CHECKERBOARD):
                        // Si la suma de (fila + columna) es par, aplica color claro; si es impar, oscuro.
                        val isEvenCell = (row + col) % 2 == 0
                        val cellBg = if (isEvenCell) CellLightGreen else CellDarkGreen
                        val cellCoord = Pair(row, col)

                        // --- B4. CASILLA INDIVIDUAL (CELDA 1x1) ---
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f) // Cada celda dentro del grid es cuadrada
                                .background(cellBg)
                                .border(width = 0.5.dp, color = Color(0xFF152012)) // Rejilla militar sutil
                                .clickable { onCellClick(index) }, // Emite el evento con su índice al tocarla
                            contentAlignment = Alignment.Center
                        ) {
                            // CONDICIONAL DE RENDERIZADO SEGÚN EL MODO ACTIVO
                            if (!isRadarMode) {
                                // MODO "MI SECTOR": Pinta bloques de colores para representar las naves/piezas
                                when {
                                    lPieceCells.contains(cellCoord) -> Box(
                                        modifier = Modifier.fillMaxSize(0.9f).background(Color(0xFFB5BEB0))
                                    )
                                    tPieceCells.contains(cellCoord) -> Box(
                                        modifier = Modifier.fillMaxSize(0.9f).background(Color(0xFF8B5CF6))
                                    )
                                    cannonCells.contains(cellCoord) -> Box(
                                        modifier = Modifier.fillMaxSize(0.9f).background(Color(0xFFD97706))
                                    )
                                    mortarCells.contains(cellCoord) -> Box(
                                        modifier = Modifier.fillMaxSize(0.9f).background(Color(0xFF92400E))
                                    )
                                }
                            } else {
                                // MODO "RADAR ENEMIGO": Muestra la mirilla objetivo sobre la celda actualmente seleccionada
                                val targetIndex = selectedCellIndex ?: 27 // Celda por defecto (F4 = índice 27)
                                if (index == targetIndex) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_target),
                                        contentDescription = "Mirilla Táctica",
                                        tint = TacticalOrangeAlert,
                                        modifier = Modifier.fillMaxSize(0.85f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // --- B5. COMPENSADOR DE SIMETRÍA VISUAL ---
            // Un Spacer del mismo ancho que la columna de números (24.dp) a la derecha
            // para mantener el tablero 8x8 perfectamente centrado en el dispositivo.
            Spacer(modifier = Modifier.width(24.dp))
        }
    }
}