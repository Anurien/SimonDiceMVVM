package com.example.simondicemvc

import android.annotation.SuppressLint
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.util.*

class MyViewModel : ViewModel() {
    private var secuencia = ArrayList<String>() //Secuencia en ronda actual
    private var comprobar = ArrayList<String>() //Secuencia de comprobacion
    private var ronda = 1     //número de ronda
    private var numero = 1    //número de luces encendidas


    // El MutableLiveData almacena la información para que no se pierda, cuando estamos usando los ViewModel

    private var secJuego = MutableLiveData<MutableList<String>>()


    // nos será saber el estado del juego para mostrar o no "x" funciones
    private var estado = MutableLiveData<Boolean>()

    init {
        secJuego.value = mutableListOf()

    }


    // hacemos un metodo para restablecer todos los datos almacenados a 0 y poder iniciar sin nada en memoria
    private fun borrar() {
        secJuego.value?.clear()

    }

    // el postValue avisa al observador de que cambió un valor para ejecutar el trozo de código una vez haya detectado este cambio
    // el juego esta empezando, borra lo anterior y añade el valor de la lista de colores random

    internal fun iniciarPartida(listaBotones: List<Button>) {
        estado.value = false
        borrar()
        mostrarSecuencia(listaBotones)
    }

    /**
     * Muesta una secuencia de parpadeos
     * @numero: número de parpadeos que se van a realizar
     */
    private fun mostrarSecuencia(listaBotones: List<Button>) {

        var encendido = 0L
        val random = Random().nextInt(4)
        // el ? en el value verifica si hay algo almacenado o no
        encendido += 750L
        when (random) {
            0 -> {
                parpadeo(encendido, "azul", listOf(listaBotones[0]))
                secuencia.add("azul")
                secJuego.value?.add("azul")
                secJuego.postValue(secJuego.value)
            }
            1 -> {
                parpadeo(encendido, "amarillo", listOf(listaBotones[1]))
                secuencia.add("amarillo")
                secJuego.value?.add("amarillo")
                secJuego.postValue(secJuego.value)
            }
            2 -> {
                parpadeo(encendido, "verde", listOf(listaBotones[2]))
                secuencia.add("amarillo")
                secJuego.value?.add("amarillo")
                secJuego.postValue(secJuego.value)
            }
            3 -> {
                parpadeo(encendido, "rojo", listOf(listaBotones[3]))
                secuencia.add("rojo")
                secJuego.value?.add("rojo")
                secJuego.postValue(secJuego.value)
            }
        }

        comprobar = secuencia
    }

    /**
     * Realiza el parpadeo del botón @color
     * @encendido: Milisegundos de la secuenda a los que se va a encender la luz
     * @color: Color que se va a encender
     * @maximo: número de luces totales que se van a encender
     * @actual: número de luz actual que se va a encender respecto a @maximo de luces que se encienden
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun parpadeo(encendido: Long, color: String, listaBotones: List<Button>) {
        GlobalScope.launch(Dispatchers.Main) {
            for (colors in secJuego.value!!) {
                delay(encendido)
                when (color) {
                    "azul" -> {
                        listaBotones[0].setBackgroundResource(R.drawable.blue_on)
                        delay(500L)
                        listaBotones[0].setBackgroundResource(R.drawable.blue_button)
                    }
                    "amarillo" -> {
                        listaBotones[1].setBackgroundResource(R.drawable.yellow_on)
                        delay(500L)
                        listaBotones[1].setBackgroundResource(R.drawable.yellow_button)
                    }
                    "verde" -> {
                        listaBotones[2].setBackgroundResource(R.drawable.green_on)
                        delay(500L)
                        listaBotones[2].setBackgroundResource(R.drawable.green_button)
                    }
                    "rojo" -> {
                        listaBotones[3].setBackgroundResource(R.drawable.red_on)
                        delay(500L)
                        listaBotones[3].setBackgroundResource(R.drawable.red_button)
                    }
                }
            }
        }
    }

    /**
     * Comprueba si el usuario, a la hora de pulsar un botón, ha acertado con la siguiente luz de la secuencia.
     * Realiza una serie de operaciones según haya acertado, haya fallado o no queden comprobaciones restantes.
     * @color: Botón que pulsó el usuario
     */
    @SuppressLint("SetTextI18n")
    fun comprobar(color: String) {
        if (secJuego.value?.isEmpty() == true) {
            ronda = 1
            numero = 1

            // Toast.makeText(this@MainActivity, "Presiona Play para jugar", Toast.LENGTH_SHORT).show()
            borrar()
        } else {
            if (secJuego.value?.get(0).equals(color)) {
                secJuego.value?.removeAt(0)
                //Toast.makeText(this@MainActivity, "Acierto", Toast.LENGTH_SHORT).show()
                if (secJuego.value?.isEmpty() == true) {
                    numero++
                    ronda++
                    /*  runBlocking {
                          Toast.makeText(this@MainActivity, "Ronda: $ronda", Toast.LENGTH_SHORT)
                              .show()
                      }*/
                    /*val rond: TextView = findViewById(R.id.ronda)
                    rond.text = "Ronda: $ronda"*/
                }
            } else {
                ronda = 1
                numero = 1

                /*Toast.makeText(
                    this@MainActivity, "Ohhh...has fallado,vuelve a intentarlo", Toast.LENGTH_SHORT
                ).show()
                val rond: TextView = findViewById(R.id.ronda)
                rond.text = "Ronda: $ronda"*/
                borrar()
            }
        }
    }
}