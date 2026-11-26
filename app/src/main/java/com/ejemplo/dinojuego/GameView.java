package com.ejemplo.dinojuego;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

// Vista del juego: por ahora solo pinta el fondo y el suelo
public class GameView extends View {

    private static final int COLOR_FONDO = Color.WHITE;
    private static final int COLOR_ELEMENTOS = Color.rgb(83, 83, 83);

    private final Paint pincel = new Paint();

    public GameView(Context context) {
        super(context);
        pincel.setColor(COLOR_ELEMENTOS);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(COLOR_FONDO);
        // Suelo al 80 % de la altura
        float sueloY = 0.8f * getHeight();
        pincel.setStrokeWidth(getHeight() / 200f);
        canvas.drawLine(0f, sueloY, getWidth(), sueloY, pincel);
    }
}
