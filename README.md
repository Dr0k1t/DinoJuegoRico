# DinoJuegoRico

Juego para Android inspirado en la mecánica del juego del dinosaurio sin conexión:
un personaje corre, aparecen obstáculos y el jugador solo puede saltar.
Los gráficos (rectángulos y texto) son de diseño propio y se dibujan con `Canvas`.

## Cómo clonar y ejecutar

1. Instala [Android Studio](https://developer.android.com/studio) (incluye el JDK 17 y el SDK de Android).
2. Clona el repositorio:

   ```powershell
   git clone https://github.com/Dr0k1t/DinoJuegoRico.git
   ```

3. En Android Studio: **File > Open** y elige la carpeta `DinoJuegoRico`.
4. Espera a que termine la sincronización de Gradle. Si falta el SDK 34 o Gradle 8.7,
   Android Studio los descarga solo.
5. Conecta un teléfono con depuración USB o crea un emulador y pulsa **Run 'app'**.

También se puede compilar desde la terminal (con `ANDROID_HOME` configurado):

```powershell
.\gradlew.bat assembleDebug
```

## Controles

- **Tocar la pantalla**: saltar (solo cuando el dinosaurio está en el suelo).
- **Tocar después de perder**: reiniciar la partida.

El puntaje aumenta en cada frame y se muestra arriba a la derecha. La velocidad es constante.

## Estructura

```
app/src/main/AndroidManifest.xml                  pantalla horizontal y completa
app/src/main/java/com/ejemplo/dinojuego/
    MainActivity.java                             única Activity, muestra GameView
    GameView.java                                 lógica y dibujo del juego
```

Sin dependencias externas: solo el SDK de Android.

## Versiones

| Herramienta | Versión |
|-------------|---------|
| Android Gradle Plugin | 8.5.2 |
| Gradle (wrapper) | 8.7 |
| JDK | 17 |
| compileSdk / targetSdk | 34 |
| minSdk | 24 |

## Aportes por grupo

| Parte | Grupo | Aporte |
|-------|-------|--------|
| 1 | San | Esqueleto del proyecto: Gradle, wrapper, manifest, Activity y suelo |
| 2 | Cla | Dinosaurio dibujado sobre el suelo y cálculo de `unidad` |
| 3 | Ev  | Bucle de juego, gravedad y salto con toque |
| 4 | San | Obstáculos en orden fijo, en movimiento y en bucle |
| 5 | Cla | Colisión, game over y reinicio |
| 6 | Ev  | Puntaje, limpieza final y este README |
