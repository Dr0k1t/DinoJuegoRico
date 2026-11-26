package com.ejemplo.dinojuego;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

// Vista del juego: en cada onDraw se actualiza el estado, se dibuja y se pide otro frame
public class GameView extends View {

    private static final int COLOR_FONDO = Color.WHITE;
    private static final int COLOR_ELEMENTOS = Color.rgb(83, 83, 83);

    // Medidas en unidades (1 unidad = 1 % de la altura de la pantalla)
    private static final float DINO_X = 10f;
    private static final float DINO_ANCHO = 6f;
    private static final float DINO_ALTO = 10f;

    // Velocidades fijas en unidades por frame
    private static final float GRAVEDAD = 0.25f;
    private static final float IMPULSO_SALTO = 4.2f;
    private static final float VELOCIDAD_OBSTACULOS = 1.3f;

    // Orden fijo de los 8 obstaculos (unidades); se repite en bucle.
    // separacionObs[i] es la distancia desde el obstaculo anterior hasta el obstaculo i.
    private final float[] anchoObs = {3f, 5f, 4f, 7f, 3f, 6f, 4f, 5f};
    private final float[] altoObs = {8f, 6f, 12f, 7f, 14f, 9f, 10f, 6f};
    private final float[] separacionObs = {60f, 55f, 80f, 65f, 90f, 50f, 75f, 70f};
    private final float[] posXObs = new float[8];

    private final Paint pincel = new Paint();
    private final Paint pincelTexto = new Paint();

    private float unidad;
    private float sueloY;
    private float dinoY;      // borde inferior del dinosaurio (pixeles)
    private float velocidadY; // pixeles por frame, positivo hacia abajo
    private boolean juegoTerminado;
    private int puntaje;

    public GameView(Context context) {
        super(context);
        pincel.setColor(COLOR_ELEMENTOS);
        pincelTexto.setColor(COLOR_ELEMENTOS);
        pincelTexto.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int anchoAnterior, int altoAnterior) {
        super.onSizeChanged(ancho, alto, anchoAnterior, altoAnterior);
        unidad = alto / 100f;
        sueloY = 80f * unidad;
        pincel.setStrokeWidth(0.5f * unidad);
        pincelTexto.setTextSize(5f * unidad);
        reiniciar();
    }

    // Deja todo como al inicio de la partida
    private void reiniciar() {
        dinoY = sueloY;
        velocidadY = 0f;
        posXObs[0] = getWidth();
        for (int i = 1; i < 8; i++) {
            posXObs[i] = posXObs[i - 1] + separacionObs[i] * unidad;
        }
        juegoTerminado = false;
        puntaje = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent evento) {
        if (evento.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (juegoTerminado) {
                reiniciar();
            } else if (dinoY >= sueloY) {
                // Solo salta si esta en el suelo (sin doble salto)
                velocidadY = -IMPULSO_SALTO * unidad;
            }
        }
        return true;
    }

    private void actualizar() {
        // Gravedad constante
        velocidadY += GRAVEDAD * unidad;
        dinoY += velocidadY;
        if (dinoY >= sueloY) {
            dinoY = sueloY;
            velocidadY = 0f;
        }

        for (int i = 0; i < 8; i++) {
            posXObs[i] -= VELOCIDAD_OBSTACULOS * unidad;
        }
        // El obstaculo que sale por la izquierda se coloca detras del anterior en el ciclo,
        // que siempre es el que esta mas a la derecha
        for (int i = 0; i < 8; i++) {
            if (posXObs[i] + anchoObs[i] * unidad < 0f) {
                int anterior = (i + 7) % 8;
                posXObs[i] = posXObs[anterior] + separacionObs[i] * unidad;
            }
        }

        // Colision: interseccion del rectangulo del dinosaurio con cada obstaculo
        float dinoIzq = DINO_X * unidad;
        float dinoDer = dinoIzq + DINO_ANCHO * unidad;
        float dinoArriba = dinoY - DINO_ALTO * unidad;
        for (int i = 0; i < 8; i++) {
            float obsIzq = posXObs[i];
            float obsDer = obsIzq + anchoObs[i] * unidad;
            float obsArriba = sueloY - altoObs[i] * unidad;
            if (dinoIzq < obsDer && dinoDer > obsIzq && dinoArriba < sueloY && dinoY > obsArriba) {
                juegoTerminado = true;
            }
        }

        puntaje++;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!juegoTerminado) {
            actualizar();
        }

        canvas.drawColor(COLOR_FONDO);
        canvas.drawLine(0f, sueloY, getWidth(), sueloY, pincel);
        dibujarDino(canvas);

        for (int i = 0; i < 8; i++) {
            canvas.drawRect(posXObs[i], sueloY - altoObs[i] * unidad,
                    posXObs[i] + anchoObs[i] * unidad, sueloY, pincel);
        }

        // Puntaje arriba a la derecha
        pincelTexto.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(puntaje), getWidth() - 3f * unidad, 8f * unidad, pincelTexto);

        if (juegoTerminado) {
            pincelTexto.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("GAME OVER - toca para reiniciar", getWidth() / 2f, 40f * unidad, pincelTexto);
        }

        postInvalidateOnAnimation();
    }

    // Dinosaurio hecho con rectangulos, siempre dentro de su caja de DINO_ANCHO x DINO_ALTO
    private void dibujarDino(Canvas canvas) {
        float x = DINO_X * unidad;
        float u = unidad;
        canvas.drawRect(x, dinoY - 7f * u, x + 4f * u, dinoY - 1.5f * u, pincel);        // cuerpo
        canvas.drawRect(x + 2.5f * u, dinoY - 10f * u, x + 6f * u, dinoY - 6.5f * u, pincel); // cabeza
        canvas.drawRect(x + 0.5f * u, dinoY - 1.5f * u, x + 1.5f * u, dinoY, pincel);      // pata trasera
        canvas.drawRect(x + 2.5f * u, dinoY - 1.5f * u, x + 3.5f * u, dinoY, pincel);      // pata delantera
        pincel.setColor(COLOR_FONDO);
        canvas.drawRect(x + 4.5f * u, dinoY - 9.3f * u, x + 5.2f * u, dinoY - 8.6f * u, pincel); // ojo
        pincel.setColor(COLOR_ELEMENTOS);
    }
}
