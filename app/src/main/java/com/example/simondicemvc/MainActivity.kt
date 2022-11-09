package com.example.simondicemvc

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private lateinit var bRed: Button
    private lateinit var bYellow: Button
    private lateinit var bBlue: Button
    private lateinit var bGreen: Button
    private lateinit var binit: Button

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val otraClase by viewModels<MyViewModel>()

        bRed = findViewById(R.id.red)
        bBlue = findViewById(R.id.blue)
        bYellow = findViewById(R.id.yellow)
        bGreen = findViewById(R.id.green)
        binit = findViewById(R.id.start)

        val botones = listOf(bBlue, bYellow, bGreen, bRed)

        bRed.setOnClickListener {
            otraClase.comprobar("rojo")
            otraClase.mostrarSecuencia(botones)
        }
        bBlue.setOnClickListener {
            otraClase.comprobar("azul")
            otraClase.mostrarSecuencia(botones)
        }
        bYellow.setOnClickListener {
            otraClase.comprobar("amarillo")
            otraClase.mostrarSecuencia(botones)
        }
        bGreen.setOnClickListener {
            otraClase.comprobar("verde")
            otraClase.mostrarSecuencia(botones)
        }
        binit.setOnClickListener {
            otraClase.iniciarPartida(botones)
        }
    }
}