package com.example.andres.examenapp2

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

// PARA LA API
class SistemaOperativo(
        var id:Int?,
        var nombre: String,
        var version: String,
        var fechaLanzamiento: String,
        var peso_gigas: String){}


class Aplicacion(
        var id:Int?,
        var nombre: String,
        var version: String,
        var fechaLanzamiento: String,
        var peso_gigas: String,
        var costo:String,
        var url_descargar:String,
        var codigo_barras:String,
        var sistemaOperativo: Int
){}




// PARA SERIALIZAR ENTRE INTENTS
class SistemaOperativoSe(var id:Int?,
                         var nombre: String,
                         var version: String,
                         var fechaLanzamiento: String,
                         var peso_gigas: String):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nombre)
        parcel.writeString(version)
        parcel.writeString(fechaLanzamiento)
        parcel.writeString(peso_gigas)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SistemaOperativoSe> {
        override fun createFromParcel(parcel: Parcel): SistemaOperativoSe {
            return SistemaOperativoSe(parcel)
        }

        override fun newArray(size: Int): Array<SistemaOperativoSe?> {
            return arrayOfNulls(size)
        }
    }
}

class AplicacionSe(
        var id:Int?,
        var nombre: String,
        var version: String,
        var fechaLanzamiento: String,
        var peso_gigas: String,
        var costo:String,
        var url_descargar:String,
        var codigo_barras:String,
        var sistemaOperativo: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nombre)
        parcel.writeString(version)
        parcel.writeString(fechaLanzamiento)
        parcel.writeString(peso_gigas)
        parcel.writeString(costo)
        parcel.writeString(url_descargar)
        parcel.writeString(codigo_barras)
        parcel.writeInt(sistemaOperativo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AplicacionSe> {
        override fun createFromParcel(parcel: Parcel): AplicacionSe {
            return AplicacionSe(parcel)
        }

        override fun newArray(size: Int): Array<AplicacionSe?> {
            return arrayOfNulls(size)
        }
    }
}