# 🎯 Wartactic - Tablero Táctico 8x8 (Jetpack Compose)

**Wartactic** es una interfaz gráfica táctica para dispositivos Android desarrollada con **Jetpack Compose** (Material3) y arquitectura declarativa pura (100% Kotlin, sin XML Layouts).

🎨 **Prototipo de Diseño:** [Ver maquetación en Figma](https://www.figma.com/design/CC41y54nkv44lZpUogsTkY/maquetado-juego?node-id=0-1&t=GoGvc6yV3OFM9dw3-1)

---

## 🚀 Características
* **Tablero Responsivo 8x8:** Maquetado mediante `LazyVerticalGrid` y `aspectRatio(1f)` para mantener proporciones cuadradas homogéneas.
* **Coordenadas Tácticas:** Distribución dinámica de columnas (A-H) y filas (1-8) alineadas simétricamente con el tablero.
* **Estilo Militar Personalizado:** Paleta de colores basada en Figma (`TacticalLime`, `TacticalOrangeAlert`, `TacticalDarkBg`).
* **Estados Reactivos de UI:**
    * Conmutación de interfaz entre el modo **MI SECTOR** y **RADAR ENEMIGO**.
    * Lectura dinámica de coordenadas tácticas en la barra de estado según la casilla tocada (ej. `OBJETIVO F4`).
    * Botón de rotación angular con alternancia dinámica ($90^\circ$, $180^\circ$, $270^\circ$, $0^\circ$).
    * Selección e iluminación de unidades en campo.
* **Recursos Vectoriales:** Iconos convertidos a Vector Assets escalables (`ic_target`, `ic_unit_tank`, etc.) tintados dinámicamente mediante Compose.

## 🎨 Diseño y Prototipado
El diseño visual del juego, la paleta cromática táctica y los activos vectoriales se maquetaron originalmente en Figma:
👉 [Acceder al tablero de Figma](https://www.figma.com/design/CC41y54nkv44lZpUogsTkY/maquetado-juego?node-id=0-1&t=GoGvc6yV3OFM9dw3-1)

## 🛠️ Tecnologías utilizadas
* **Lenguaje:** Kotlin
* **UI Framework:** Jetpack Compose (Material3)
* **Target SDK:** Android API 35
* **Diseño previo:** Figma Prototyping

## 💻 Ejecución del proyecto
1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/jmcampos90/Wartactic.git](https://github.com/jmcampos90/Wartactic.git)

2. Abrir el proyecto en Android Studio (Ladybug / Jellyfish o superior).

3. Sincronizar dependencias de Gradle.

4. Ejecutar en un emulador Android (API 24+).