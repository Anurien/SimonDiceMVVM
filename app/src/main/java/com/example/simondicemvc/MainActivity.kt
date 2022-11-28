package com.example.simondicemvc

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import kotlinx.coroutines.*

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

    @OptIn(DelicateCoroutinesApi::class)
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
        }
        bBlue.setOnClickListener {
            otraClase.comprobar("azul")
        }
        bYellow.setOnClickListener {
            otraClase.comprobar("amarillo")
        }
        bGreen.setOnClickListener {
            otraClase.comprobar("verde")
        }
        binit.setOnClickListener {
            otraClase.iniciarPartida()
        }

        //Observacion del cambio de Ronda
        otraClase.liveRonda.observe(
            this,
            Observer(
                @SuppressLint("SetTextI18n")
                fun(ronda: Int) {
                    val tvRonda: TextView = findViewById(R.id.ronda)
                    if (ronda == 1) binit.isClickable = true

                    tvRonda.setText("Ronda: $ronda")

                }
            )
        )
        //Observacion del cambio de Record
        otraClase.liveRecord.observe(
            this,
            Observer(
                @SuppressLint("SetTextI18n")
                fun(record: Int) {
                    val tvRecord: TextView = findViewById(R.id.record)

                    tvRecord.setText("Record: $record")

                }
            )
        )
        //Observacion de la secuencia
        otraClase.secJuego.observe(
            this,
            Observer(
                fun(secuencia: MutableList<String>) {
                    GlobalScope.launch(Dispatchers.Main) {
                        for (color in secuencia) {
                            delay(1000)
                            when (color) {
                                "azul" -> {
                                    botones[0].setBackgroundResource(R.drawable.blue_on)
                                    delay(500L)
                                    botones[0].setBackgroundResource(R.drawable.blue_button)
                                }
                                "amarillo" -> {
                                    botones[1].setBackgroundResource(R.drawable.yellow_on)
                                    delay(500L)
                                    botones[1].setBackgroundResource(R.drawable.yellow_button)
                                }
                                "verde" -> {
                                    botones[2].setBackgroundResource(R.drawable.green_on)
                                    delay(500L)
                                    botones[2].setBackgroundResource(R.drawable.green_button)
                                }
                                "rojo" -> {
                                    botones[3].setBackgroundResource(R.drawable.red_on)
                                    delay(500L)
                                    botones[3].setBackgroundResource(R.drawable.red_button)
                                }
                            }
                        }
                    }
                }
            )
        )
    }
}