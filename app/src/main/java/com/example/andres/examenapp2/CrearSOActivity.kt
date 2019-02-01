package com.example.andres.examenapp2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.beust.klaxon.Klaxon
import com.example.andres.examenapp2.BDD.Companion.ip
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_crear_so.*
import kotlinx.android.synthetic.main.activity_main.*

class CrearSOActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_so)
        boton_registrar_so.setOnClickListener {
            crearSO()
        }
    }
    fun crearSO(){
        //Crear objeto
        val nombre = txt_nombre.text.toString()
        val version = txt_version.text.toString()
        val fecha = txt_fecha.text.toString()
        val peso = txt_peso.text.toString()

        val so = SistemaOperativo(nombre, version, fecha, peso)
        val parametros = listOf("nombre" to so.nombre, "version" to so.version,
                "fechaLanzamiento" to so.fechaLanzamiento, "peso_gigas" to so.peso_gigas)
        Log.i("htpp",parametros.toString())

        val direccion = "http://$ip:80/sistemas/api/"
        Log.i("http",direccion)
        val url = "http://$ip:80/sistemas/api/"
                .httpPost(parametros)
                .responseString { request, response, result ->
                    when (result) {
                        is Result.Failure -> {
                            val ex = result.getException()
                            Log.i("http-p", ex.toString())
                            mensaje(this,"error","Datos no validos")

                        }
                        is Result.Success -> run {
                                val data = result.get()
                                Log.i("http-p", data)
                                mensaje(this,"Aceptado","Datos validos, espere...")
                            cargarDatosSO(direccion, ::irlistarSo)
                            }
                    }
                }
    }

    fun irlistarSo(){
        val intent = Intent(
                this,
                ListarActivity::class.java
        )
        startActivity(intent)
    }
}
