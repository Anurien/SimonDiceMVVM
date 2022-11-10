package com.example.simondicemvc

import android.annotation.SuppressLint
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.List

class MyViewModel : ViewModel() {
    private var ronda = 1     //número de ronda
    private var numero = 1    //número de luces encendidas


    // El MutableLiveData almacena la información para que no se pierda, cuando estamos usando los ViewModel

    private var secJuego = MutableLiveData<MutableList<String>>()
    var liveRonda = MutableLiveData<Int>()


    // nos será saber el estado del juego para mostrar o no "x" funciones
    private var estado = MutableLiveData<Boolean>()

    init {
        secJuego.value = mutableListOf()
        liveRonda.value = ronda

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
     * @listabotones: una lista con los botones que se tienen que iluminar
     */
    fun mostrarSecuencia(listaBotones: List<Button>) {

        var encendido = 0L
        for (i in 1..numero) {
            val random = Random().nextInt(4)
            // el ? en el value verifica si hay algo almacenado o no
            encendido += 750L
            when (random) {
                0 -> {
                    parpadeo(encendido, "azul", listaBotones[0])
                    secJuego.value?.add("azul")
                    secJuego.postValue(secJuego.value)
                }
                1 -> {
                    parpadeo(encendido, "amarillo", listaBotones[1])
                    secJuego.value?.add("amarillo")
                    secJuego.postValue(secJuego.value)
                }
                2 -> {
                    parpadeo(encendido, "verde", listaBotones[2])
                    secJuego.value?.add("amarillo")
                    secJuego.postValue(secJuego.value)
                }
                3 -> {
                    parpadeo(encendido, "rojo", listaBotones[3])
                    secJuego.value?.add("rojo")
                    secJuego.postValue(secJuego.value)
                }
            }
        }
    }

    /**
     * Realiza el parpadeo del botón @color
     * @encendido: Milisegundos de la secuenda a los que se va a encender la luz
     * @color: Color que se va a encender
     * @listabotones: Boton que va a iluminarse
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun parpadeo(encendido: Long, color: String, listaBotones: Button) {
        GlobalScope.launch {
            delay(encendido)
            when (color) {
                "azul" -> {
                    listaBotones.setBackgroundResource(R.drawable.blue_on)
                    delay(500L)
                    listaBotones.setBackgroundResource(R.drawable.blue_button)
                }
                "amarillo" -> {
                    listaBotones.setBackgroundResource(R.drawable.yellow_on)
                    delay(500L)
                    listaBotones.setBackgroundResource(R.drawable.yellow_button)
                }
                "verde" -> {
                    listaBotones.setBackgroundResource(R.drawable.green_on)
                    delay(500L)
                    listaBotones.setBackgroundResource(R.drawable.green_button)
                }
                "rojo" -> {
                    listaBotones.setBackgroundResource(R.drawable.red_on)
                    delay(500L)
                    listaBotones.setBackgroundResource(R.drawable.red_button)
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
                }
            } else {
                ronda = 1
                numero = 1
                borrar()
            }
        }
    }
}