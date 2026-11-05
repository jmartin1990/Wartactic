package com.example.wartactic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wartactic.ui.theme.*

/**
 * CLASE PRINCIPAL: Activity entry point de la aplicación.
 * Hereda de ComponentActivity (estándar para Jetpack Compose).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita el diseño inmersivo Edge-to-Edge (ocupa debajo de la barra de estado y gestos)
        enableEdgeToEdge()

        // Inyecta el árbol de composables sustituyendo los layouts clásicos en XML
        setContent {
            WartacticTheme {
                WartacticApp()
            }
        }
    }
}

/**
 * FUNCIÓN AUXILIAR DE CONVERSIÓN DE COORDENADAS:
 * Convierte el índice plano de un array (0 al 63) a coordenadas en notación militar A1..H8.
 *
 * @param index Índice lineal de la celda pulsada.
 * @return String con la coordenada legible (ej. 27 -> "D4").
 */
fun getCellCoordinateName(index: Int?): String {
    if (index == null) return "F4" // Valor por defecto si no hay celda seleccionada
    val colLetters = listOf("A", "B", "C", "D", "E", "F", "G", "H")

    val col = colLetters[index % 8]  // Operación Módulo: Determina la columna (0-7 -> A-H)
    val row = (index / 8) + 1       // División Entera: Determina la fila (0-7 -> 1-8)
    return "$col$row"
}

/**
 * COMPOSABLE CONTENEDOR RAÍZ: WartacticApp
 * Contiene el estado general de la aplicación y maqueta la pantalla en vertical.
 */
@Composable
fun WartacticApp() {
    // =========================================================================
    // 1. GESTIÓN DEL ESTADO REACTIVO (UI STATE)
    // Se utiliza `remember { mutableStateOf(...) }` para preservar el estado
    // durante las recomposiciones provocadas por la interacción del usuario.
    // =========================================================================

    // Switch de modo: false = "MI SECTOR" (Defensa), true = "RADAR ENEMIGO" (Ataque)
    var isRadarMode by remember { mutableStateOf(false) }

    // Índice de la casilla seleccionada en el tablero (inicializado en 27 = F4/D4)
    var selectedCell by remember { mutableStateOf<Int?>(27) }

    // Selector de modo de juego: true = 1 vs IA, false = 1 vs 1
    var isVsAI by remember { mutableStateOf(true) }

    // Ángulo de rotación de la pieza activa: se cicla en 90°, 180°, 270°, 0°
    var rotationAngle by remember { mutableIntStateOf(90) }

    // Nombre de la unidad militar activa en el panel inferior
    var selectedUnitTitle by remember { mutableStateOf("Lanza Misiles") }

    // =========================================================================
    // 2. MAQUETACIÓN GENERAL (COLUMN VERTIKAL CON DISPOSICIÓN ESPACIADA)
    // =========================================================================
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TacticalDarkBg)
            // Agrega rellenos dinámicos para no invadir las barras del sistema (Edge-to-Edge safe)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.SpaceBetween // Empuja los bloques aprovechando todo el alto
    ) {

        // --- SECCIÓN 1: CABECERA, SELECTORES DE MODO Y PESTAÑAS NAVEGABLES ---
        Column {
            // Fila 1.1: Logo con icono vectorial y ajuste
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_target),
                        contentDescription = "Logo",
                        tint = TacticalLime, // Tintado dinámico del vector ic_target
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "WARTACTIC",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TacticalLime
                    )
                }
                Text(
                    text = "⚙",
                    fontSize = 20.sp,
                    color = Color(0xFFC0C0C0)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Fila 1.2: Etiqueta "MODO DE JUEGO" (Fidelidad Figma)
            Text(
                text = "MODO DE JUEGO",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFA0B598),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            // Fila 1.3: Botones Mutuamente Excluyentes (1 vs IA / 1 vs 1)
            Row {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isVsAI) TacticalLime else Color(0xFF1B2618),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { isVsAI = true } // Modifica el estado reactivo `isVsAI`
                ) {
                    Text(
                        text = "01  1 vs IA",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isVsAI) TacticalDarkBg else Color(0xFF888888),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (!isVsAI) TacticalLime else Color(0xFF1B2618),
                    modifier = Modifier.clickable { isVsAI = false }
                ) {
                    Text(
                        text = "02  1 vs 1",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (!isVsAI) TacticalDarkBg else Color(0xFF888888),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Fila 1.4: Pestañas de Navegación con Indicador Inferior (Figma Exact)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    // Pestaña 1: RADAR ENEMIGO (Modo Ataque)
                    Column(
                        modifier = Modifier
                            .clickable { isRadarMode = true }
                            .padding(end = 20.dp)
                    ) {
                        Text(
                            text = "RADAR ENEMIGO",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isRadarMode) TacticalOrangeAlert else Color(0xFF888888)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Barra horizontal animada/activa en Naranja
                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(2.5.dp)
                                .background(if (isRadarMode) TacticalOrangeAlert else Color.Transparent)
                        )
                    }

                    // Pestaña 2: MI SECTOR (Modo Defensa)
                    Column(
                        modifier = Modifier.clickable { isRadarMode = false }
                    ) {
                        Text(
                            text = "MI SECTOR",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (!isRadarMode) TacticalLime else Color(0xFF888888)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Barra horizontal animada/activa en Verde
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(2.5.dp)
                                .background(if (!isRadarMode) TacticalLime else Color.Transparent)
                        )
                    }
                }

                // Subtexto de Estado Dinámico Superior
                Text(
                    text = if (isRadarMode) "• MIRILLA ACTIVA" else "• DEFENSA LISTA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isRadarMode) TacticalOrangeAlert else TacticalLime,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }

        // --- SECCIÓN 2: COMPOSABLE DEL TABLERO 8x8 ---
        // Se le pasa el estado actual y la función lambda para actualizar la casilla seleccionada
        TacticalBoard(
            isRadarMode = isRadarMode,
            selectedCellIndex = selectedCell,
            onCellClick = { index -> selectedCell = index }
        )

        // --- SECCIÓN 3: BARRA DE INFORMACIÓN DE ESTADO DINÁMICA ---
        // Lee en tiempo real el estado `isRadarMode` y `selectedCell`
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isRadarMode) "MIRILLA ACTIVA" else "GRID 08×08",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isRadarMode) TacticalOrangeAlert else Color(0xFFA0B598)
            )
            Text(
                // Muestra dinámicamente la coordenada calculada por la función auxiliar
                text = if (isRadarMode) "OBJETIVO ${getCellCoordinateName(selectedCell)}" else "LAT 40.4168° N",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isRadarMode) TacticalOrangeAlert else Color(0xFFA0B598)
            )
            Text(
                text = if (isRadarMode) "TOCA UNA CASILLA" else "SECTOR B-12",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isRadarMode) TacticalOrangeAlert else TacticalLime
            )
        }

        // --- SECCIÓN 4: BOTÓN ACCIÓN DE ROTACIÓN DE PIEZAS ---
        Button(
            onClick = {
                // Algoritmo circular para cambiar el ángulo en pasos de 90°
                rotationAngle = when (rotationAngle) {
                    90 -> 180
                    180 -> 270
                    270 -> 0
                    else -> 90
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TacticalCream)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "↻",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TacticalDarkBg
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ROTAR PIEZA ($rotationAngle°)", // Muestra el ángulo dinámico
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TacticalDarkBg
                )
            }
        }

        // --- SECCIÓN 5: PANEL DE TARJETAS DE UNIDADES EN CAMPO ---
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Título con el icono vectorial oficial `ic_layers`
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_layers),
                        contentDescription = "Icono Unidades",
                        tint = TacticalLime,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "UNIDADES EN CAMPO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TacticalLime
                    )
                }
                Text(
                    text = "4 disponibles",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFCCCCCC)
                )
            }

            // Grid 2x2 compuesto por filas de Composables reutilizables `UnitCard`
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    UnitCard(
                        title = "Lanza Misiles",
                        tag = "4 ÁNGULOS",
                        iconRes = R.drawable.ic_unit_missile,
                        isSelected = selectedUnitTitle == "Lanza Misiles",
                        onClick = { selectedUnitTitle = "Lanza Misiles" },
                        modifier = Modifier.weight(1f)
                    )
                    UnitCard(
                        title = "Cañón",
                        tag = "2 ÁNGULOS",
                        iconRes = R.drawable.ic_unit_cannon,
                        isSelected = selectedUnitTitle == "Cañón",
                        onClick = { selectedUnitTitle = "Cañón" },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    UnitCard(
                        title = "Tanque",
                        tag = "4 ÁNGULOS",
                        iconRes = R.drawable.ic_unit_tank,
                        isSelected = selectedUnitTitle == "Tanque",
                        onClick = { selectedUnitTitle = "Tanque" },
                        modifier = Modifier.weight(1f)
                    )
                    UnitCard(
                        title = "Mortero",
                        tag = "1 ÁNGULO",
                        iconRes = R.drawable.ic_unit_mortar,
                        isSelected = selectedUnitTitle == "Mortero",
                        onClick = { selectedUnitTitle = "Mortero" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // --- SECCIÓN 6: BOTÓN PRINCIPAL DE ATAQUE / CONMUTACIÓN ---
        Button(
            // Alterna el modo global de la aplicación (MI SECTOR <-> RADAR ENEMIGO)
            onClick = { isRadarMode = !isRadarMode },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRadarMode) TacticalOrangeAlert else TacticalLime
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_target),
                    contentDescription = "Mirilla Botón",
                    tint = if (isRadarMode) Color.White else TacticalDarkBg,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isRadarMode) "ATAQUE PREPARADO" else "LANZAR ATAQUE TÁCTICO",
                    color = if (isRadarMode) Color.White else TacticalDarkBg,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

/**
 * COMPOSABLE REUTILIZABLE: UnitCard
 * Modula el diseño individual de cada naves/unidad táctica.
 * Demuestra el principio de no repetición de código (DRY - Don't Repeat Yourself).
 *
 * @param title Nombre de la unidad.
 * @param tag Etiqueta informativa (ej. "4 ÁNGULOS").
 * @param iconRes Referencia al recurso gráfico XML.
 * @param isSelected Booleano que indica si la tarjeta está activa.
 * @param onClick Lambda que se ejecuta al seleccionar la tarjeta.
 */
@Composable
fun UnitCard(
    title: String,
    tag: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Colores reactivos según la selección
    val borderColor = if (isSelected) TacticalLime else Color(0xFF283A24)
    val bgColor = if (isSelected) Color(0xFF1D2B1B) else Color(0xFF141E12)

    Surface(
        modifier = modifier
            .height(52.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(6.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(if (isSelected) 1.5.dp else 0.8.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = tag,
                    fontSize = 10.sp,
                    color = TacticalLime,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}