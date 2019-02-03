package com.example.andres.examenapp2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.ArrayAdapter
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_crear_applicacion.*
import kotlinx.android.synthetic.main.activity_crear_so.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CrearApplicacionActivity : AppCompatActivity() {


    var pathActualFoto = ""
    var respuestaBarcode = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_applicacion)


        btn_tomar_foto.setOnClickListener {
            tomarFoto()
        }



    }

    fun tomarFoto(){
        val archivoImagen = crearArchivo("JPEG_", Environment.DIRECTORY_PICTURES, ".jpg")
        pathActualFoto = archivoImagen.absolutePath
        enviarIntentFoto(archivoImagen)
    }

    private fun crearArchivo(prefijo: String, directorio: String, extension: String): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = prefijo + timeStamp + "_"
        val storageDir = getExternalFilesDir(directorio)

        // Crear archivo temporal
        return File.createTempFile(
                imageFileName, /* prefix */
                extension, /* suffix */
                storageDir      /* directory */
        )
    }

    private fun enviarIntentFoto(archivo: File) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.andres.examenapp2",
                archivo)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, TOMAR_FOTO_REQUEST);
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TOMAR_FOTO_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    mostrarFotoImagenView()
                    obtenerInfoCodigoBarras(obtenerBitmapDeArchivo(pathActualFoto))
                }
            }

        }
    }

    fun mostrarFotoImagenView(){
        img_barras.setImageBitmap(obtenerBitmapDeArchivo(pathActualFoto))
    }

    fun obtenerBitmapDeArchivo(path: String): Bitmap {
        val archivoImagen = File(path)
        return BitmapFactory.decodeFile(archivoImagen.getAbsolutePath())
    }

    fun obtenerInfoCodigoBarras(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance()
                .visionBarcodeDetector
        Log.i("info", "------- Entro a detectar")
        val result = detector.detectInImage(image)
                .addOnSuccessListener { barCodes ->
                    Log.i("info", "------- tamano del barcode ${barCodes.size}")
                    respuestaBarcode.add("Ejemplo")
                    for (barcode in barCodes) {
                        val bounds = barcode.getBoundingBox()
                        val corners = barcode.getCornerPoints()

                        val rawValue = barcode.getRawValue()

                        Log.i("ml", "------- $bounds")
                        Log.i("ml", "------- $corners")
                        Log.i("ml", "------- $rawValue")

                        respuestaBarcode.add(rawValue.toString())
                    }
                    txt_codigo.setText(respuestaBarcode[1])
                }
                .addOnFailureListener {
                    Log.i("info", "------- No reconocio nada")
                }
    }

    fun crearActualizarSO(es_nuevo:Boolean){

        val id = txt_id_so_r.text.toString()
        val nombre = txt_nombre.text.toString()
        val version = txt_version.text.toString()
        val fecha = txt_fecha.text.toString()
        val peso = txt_peso.text.toString()
        val so:SistemaOperativo
        if(es_nuevo){
            so = SistemaOperativo(id=null,nombre = nombre,version =  version,fechaLanzamiento =  fecha,peso_gigas =  peso)
        }else{
            so = SistemaOperativo(id=id.toInt(),nombre = nombre,version =  version,fechaLanzamiento =  fecha,peso_gigas =  peso)
        }

        //Crear objeto
        val parametros = listOf("nombre" to so.nombre, "version" to so.version,
                "fechaLanzamiento" to so.fechaLanzamiento, "peso_gigas" to so.peso_gigas)
        Log.i("htpp",parametros.toString())
        var direccion = ""
        if(es_nuevo){
            direccion = "http://${BDD.ip}:80/sistemas/api/"
            val url = direccion
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
                                //cargarDatosSO(direccion, ::irlistarSo)
                            }
                        }
                    }
        }else{
            direccion = "http://${BDD.ip}:80/sistemas/api/${so.id}/update"
            val url = direccion
                    .httpPut(parametros)
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
                                val redire = "http://${BDD.ip}:80/sistemas/api/"
                               // cargarDatosSO(redire, ::irlistarSo)
                            }
                        }
                    }
        }
        Log.i("http",direccion)

    }


    companion object {
        val TOMAR_FOTO_REQUEST = 1;
    }
}
