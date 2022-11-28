package com.example.simondicemvc

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@OptIn(DelicateCoroutinesApi::class)
class MyViewModel(application: Application) : AndroidViewModel(application) {
    private var ronda = 1     //número de ronda
    private var record = 1     //número de record
    private var numero = 1    //número de luces encendidas
    private var secuencia = ArrayList<String>() //Secuencia en ronda actual
    private var comprobacion = ArrayList<String>() //Secuencia comprobar en ronda actual
    var indice = 1
    var room: AppDatabase? = null


    @SuppressLint("StaticFieldLeak")
    private val context: Context = getApplication<Application>().applicationContext


    // El MutableLiveData almacena la información para que no se pierda, cuando estamos usando los ViewModel
    var secJuego = MutableLiveData<ArrayList<String>>()
    var liveRonda = MutableLiveData<Int>()
    var liveRecord = MutableLiveData<Int>()

    // nos será saber el estado del juego para mostrar o no "x" funciones
    private var estado = MutableLiveData<Boolean>()

    init {
        secJuego.value = secuencia
        liveRonda.value = ronda
        liveRecord.value = record
        room = Room
            .databaseBuilder(
                context,
                AppDatabase::class.java, "Record"
            )
            .build()
        //recogerRecord()

        val roomCorrutine = GlobalScope.launch(Dispatchers.Main) {
            try {
                liveRecord.value = room!!.recordDao().getRecord()
                Log.d("recSQLite", liveRecord.value.toString())
            } catch (ex: java.lang.NullPointerException) {
                room!!.recordDao().crearRecord()
                liveRecord.value = room!!.recordDao().getRecord()
            }
        }
        roomCorrutine.start()

    }

    /* fun recogerRecord() {
         val roomCorrutine = GlobalScope.launch(Dispatchers.IO) {
             try {
                 record = room!!.recordDao().getRecord()
                 Log.d("recSQLite", record.toString())
             } catch (ex: java.lang.NullPointerException) {
                 room!!.recordDao().crearRecord()
                 liveRecord.value = room!!.recordDao().getRecord()
             }
         }
         roomCorrutine.start()
     }*/

    fun actualizarRecord() {
        // if (record < ronda) {
        liveRecord.value = liveRonda.value
        //recogerRecord()
        //}
        val updateCorrutine = GlobalScope.launch(Dispatchers.Main) {

            room!!.recordDao().update(Record(1, liveRecord.value))
        }
        updateCorrutine.start()
    }

    // el postValue avisa al observador de que cambió un valor para ejecutar el trozo de código una vez haya detectado este cambio
    // el juego esta empezando, borra lo anterior y añade el valor de la lista de colores random

    internal fun iniciarPartida() {
        estado.value = false
        mostrarSecuencia()
        //recogerRecord()
    }

    /**
     * Muesta una secuencia de parpadeos
     * @listabotones: una lista con los botones que se tienen que iluminar
     */
    fun mostrarSecuencia() {

        var encendido = 0L
        for (i in 1..ronda) {
            val random = Random().nextInt(4)
            // el ? en el value verifica si hay algo almacenado o no
            encendido += 1000L
            when (random) {
                0 -> {
                    secuencia.add("azul")
                    secJuego.setValue(secuencia)
                }
                1 -> {
                    secuencia.add("amarillo")
                    secJuego.setValue(secuencia)
                }
                2 -> {
                    secuencia.add("verde")
                    secJuego.setValue(secuencia)
                }
                3 -> {
                    secuencia.add("rojo")
                    secJuego.setValue(secuencia)
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
        /*if (secuencia.isEmpty()) {
            ronda = 1
            numero = 1
            liveRonda.value = ronda

            Toast.makeText(context, "Presiona Play para jugar", Toast.LENGTH_SHORT)
                .show()
            secuencia.clear()
            secJuego.value?.clear()
            comprobacion.clear()
        } else {*/
       /* val iterator = comprobacion.iterator()
        while(iterator.hasNext()){
            val item = iterator.next()
            if(item[indice] == indice){
                iterator.remove()
            }
        }*/
            comprobacion.add(color)
            indice = comprobacion.size - 1
            val resultado = comprobacion[indice] == secuencia[indice]
            if (comprobacion.size == ronda) {
                Toast.makeText(context, "Acierto", Toast.LENGTH_SHORT).show()
                numero++
                ronda++
                //record++
                liveRonda.value = ronda
                // liveRecord.value = record
                runBlocking {
                    Toast.makeText(context, "Ronda: $ronda", Toast.LENGTH_SHORT)
                        .show()
                }
                actualizarRecord()
                mostrarSecuencia()
                comprobacion = arrayListOf()
            }
            if (!resultado && comprobacion.size != ronda) {
                ronda = 1
                numero = 1
                liveRonda.value = ronda
                // liveRecord.value = record
                actualizarRecord()
                Toast.makeText(
                    context,
                    "Ohhh...has fallado,vuelve a intentarlo",
                    Toast.LENGTH_SHORT
                ).show()
                secuencia.clear()
                comprobacion.clear()
                secJuego.value?.clear()
                /*if (secuencia[0] == color) {
                    secuencia.removeAt(0)
                    secJuego.value = secuencia
                    Toast.makeText(context, "Acierto", Toast.LENGTH_SHORT).show()
                    if (secuencia.isEmpty()) {
                        numero++
                        ronda++
                        //record++
                        liveRonda.value = ronda
                       // liveRecord.value = record
                        runBlocking {
                            Toast.makeText(context, "Ronda: $ronda", Toast.LENGTH_SHORT)
                                .show()
                        }
                        actualizarRecord()
                        mostrarSecuencia()

                    }*/
            } /*else {
                ronda = 1
                numero = 1
                liveRonda.value = ronda
                // liveRecord.value = record
                actualizarRecord()
                Toast.makeText(
                    context,
                    "Ohhh...has fallado,vuelve a intentarlo",
                    Toast.LENGTH_SHORT
                ).show()
                secuencia.clear()
                secJuego.value?.clear()

            }*/
        }
    }
