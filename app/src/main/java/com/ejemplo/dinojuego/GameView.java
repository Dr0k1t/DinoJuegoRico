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

    private final Paint pincel = new Paint();

    private float unidad;
    private float sueloY;
    private float dinoY;      // borde inferior del dinosaurio (pixeles)
    private float velocidadY; // pixeles por frame, positivo hacia abajo

    public GameView(Context context) {
        super(context);
        pincel.setColor(COLOR_ELEMENTOS);
    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int anchoAnterior, int altoAnterior) {
        super.onSizeChanged(ancho, alto, anchoAnterior, altoAnterior);
        unidad = alto / 100f;
        sueloY = 80f * unidad;
        pincel.setStrokeWidth(0.5f * unidad);
        dinoY = sueloY;
        velocidadY = 0f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent evento) {
        if (evento.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (dinoY >= sueloY) {
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        actualizar();

        canvas.drawColor(COLOR_FONDO);
        canvas.drawLine(0f, sueloY, getWidth(), sueloY, pincel);
        dibujarDino(canvas);

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
