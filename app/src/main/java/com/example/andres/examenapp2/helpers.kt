package com.example.andres.examenapp2

import android.app.Activity
import android.arch.core.util.Function
import android.content.Context
import android.util.Log
import com.beust.klaxon.Klaxon
import com.example.andres.examenapp2.BDD.Companion.sistemasOperativos
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf



fun mensaje(actividad:Activity,tipo: String,contenido:String){

    com.tapadoo.alerter.Alerter.create(actividad)
            .setTitle(tipo)
            .setText(contenido)
            .show()
}

fun cargarDatosSO(url:String,funcion_intent: () -> Unit){
    url.httpGet().responseString{request, response, result ->
        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                Log.i("http", ex.toString())
            }
            is Result.Success -> {
                val data = result.get()
                sistemasOperativos.clear()
                val wordDict = Klaxon().parseArray<SistemaOperativo>(data)
                Log.i("http", "Datos: ${wordDict.toString()}")
                if (wordDict != null) {
                    for ( item in wordDict.iterator()){
                        sistemasOperativos.add(item)
                    }
                }

                funcion_intent()

            }
        }
    }
}
