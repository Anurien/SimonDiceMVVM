package com.example.simondicemvc

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import com.google.firebase.database.ktx.getValue


@OptIn(DelicateCoroutinesApi::class)
class MyViewModel(application: Application) : AndroidViewModel(application) {
    private var ronda = 1     //número de ronda
    private var record = 1     //número de record
    private var secuencia = ArrayList<String>() //Secuencia en ronda actual
    private var comprobacion = ArrayList<String>() //Secuencia comprobar en ronda actual
    private lateinit var fireBaseR: DatabaseReference
    private var indice = 1 //numero de elementos de comprobacion

    // para poder utilizar toast en el view
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
        fireBaseR =
            Firebase.database("https://simondicef-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("record")
        //Defino el listener del record
        val recordListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                liveRecord.value = dataSnapshot.getValue<Int>()
                Log.d("RecFirebase", liveRecord.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ReaLTime", "recordListener:OnCancelled", error.toException())
            }
        }
        //Añado el listener a la BD
        fireBaseR.addValueEventListener(recordListener)

    }

    /**
     * Actualiza el record de la base de datos con el mutablelive
     */
    fun actualizarRecord() {
        liveRecord.value = liveRonda.value
        fireBaseR.setValue(liveRecord.value)
        Log.d("FireAct", liveRecord.toString())

    }

    internal fun iniciarPartida() {
        estado.value = false
        mostrarSecuencia()
    }

    /**
     * Muesta una secuencia de parpadeos que se guardan en la secuencia
     */
    fun mostrarSecuencia() {

        var encendido = 0L
        val random = Random().nextInt(4)
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

    /**
     * Comprueba si el usuario, a la hora de pulsar un botón, ha acertado con la siguiente luz de la secuencia.
     * Realiza una serie de operaciones según haya acertado, haya fallado o no queden comprobaciones restantes.
     * @color: Botón que pulsó el usuario
     */
    @SuppressLint("SetTextI18n")
    fun comprobar(color: String) {
        comprobacion.add(color)
        indice = comprobacion.size - 1
        val resultado = comprobacion[indice] == secuencia[indice]
        if (comprobacion.size == ronda) {
            Toast.makeText(context, "Acierto", Toast.LENGTH_SHORT).show()
            ronda++

            liveRonda.value = ronda
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
            liveRonda.value = ronda
            actualizarRecord()
            Toast.makeText(
                context,
                "Ohhh...has fallado,vuelve a intentarlo",
                Toast.LENGTH_SHORT
            ).show()
            secuencia.clear()
            comprobacion.clear()
            secJuego.value?.clear()
        }
    }
}
