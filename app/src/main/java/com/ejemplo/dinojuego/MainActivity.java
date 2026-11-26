package com.ejemplo.dinojuego;

import android.app.Activity;
import android.os.Bundle;

// Unica pantalla de la app: muestra la vista del juego
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }
}
