package com.example.andres.examenapp2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.support.v7.widget.PopupMenu
import android.widget.Button
import com.github.kittinunf.result.Result
import android.content.Intent
import com.example.andres.examenapp2.BDD.Companion.aplicaciones
import com.github.kittinunf.fuel.httpDelete
import kotlinx.android.synthetic.main.activity_listar_aplicaciones.*


class ListarAplicacionesActivity : AppCompatActivity() {
    var id_so = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_aplicaciones)


        val sistema = intent.getParcelableExtra<SistemaOperativoSe>("sistema")

        id_so = sistema.id!!

        txt_nombre_so_parce.setText(sistema.nombre)
        txt_version_so_parce.setText(sistema.version)

        btn_nuevo_app
                .setOnClickListener {
                    irACrearHijo()
                }

        val layoutManager = LinearLayoutManager(this)
        val rv = rv_hijos
        val adaptador = AppAdaptador(BDD.aplicaciones, this, rv)

        rv_hijos.layoutManager = layoutManager
        rv_hijos.itemAnimator = DefaultItemAnimator()
        rv_hijos.adapter = adaptador

        adaptador.notifyDataSetChanged()

    }


    fun refrescar(){
        finish()
        val direccion = "http://${BDD.ip}:80/sistemas/api/app/?so=$id_so"
        Log.i("http",direccion)
        cargarDatosApp(direccion,fun(){})
        startActivity(getIntent())
    }

    fun compartir(contenido:String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, contenido)
            type = "text/plain"
        }
        startActivity(sendIntent)
    }


    fun irActualizar(aplicacion: AplicacionSe){
        val intentActividadIntent = Intent(
                this,
                CrearSOActivity::class.java
        )

        intentActividadIntent.putExtra("sistema",aplicacion)
        startActivity(intentActividadIntent)

    }

    fun irACrearHijo(){
        val intentActividadIntent = Intent(
                this,
                CrearApplicacionActivity::class.java
        )

          startActivity(intentActividadIntent)
    }
}




class AppAdaptador(private val listaAplicaciones: List<Aplicacion>,
                   private val contexto: ListarAplicacionesActivity,
                   private val recyclerView: RecyclerView) :
        RecyclerView.Adapter<AppAdaptador.MyViewHolder>() {


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nombreTextView: TextView
        var versionTextView: TextView
        var idAppTextView: TextView
        var opciones:Button

        init {
            nombreTextView = view.findViewById(R.id.txt_nombre_app) as TextView
            versionTextView = view.findViewById(R.id.txt_version_app) as TextView
            idAppTextView = view.findViewById(R.id.txt_app_id) as TextView
            opciones = view.findViewById(R.id.btn_opciones_app) as Button



            // val left = apellido.paddingLeft
            // val top = apellido.paddingTop
            // Log.i("vista-principal", "Hacia la izquierda es $left y hacia arriba es $top")

            val layout = view.findViewById(R.id.relative_lay_list_hijos) as RelativeLayout

            layout
                    .setOnClickListener {
                        val nombreActual = it.findViewById(R.id.txt_nombre_app) as TextView

                        Log.i("recycler-view",
                                "El nombre actual es: ${nombreActual.text}")

                    }



        }
    }

    // Definimos el layout
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int): MyViewHolder {

        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(
                        R.layout.items_hijos,
                        parent,
                        false
                )

        return MyViewHolder(itemView)
    }

    // Llenamos los datos del layout
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val aplicacion = listaAplicaciones[position]

        holder.nombreTextView.setText(aplicacion.nombre)
        holder.versionTextView.setText(aplicacion.version)
        holder.idAppTextView.setText(aplicacion.id.toString())
        holder.opciones.setOnClickListener {
            val popup = PopupMenu(contexto, holder.idAppTextView)
            popup.inflate(R.menu.options_menu)
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.getItemId()) {
                    R.id.eliminar_so ->{
                        //handle menu1 click
                        mensaje_dialogo(contexto,"Eliminar la APP?",
                                fun (){
                                    val id = holder.idAppTextView.text.toString()
                                    Log.i("Eliminar APP->",id)

                                    val parametros = listOf("nombre" to id)
                                    val url = "http://${BDD.ip}:80/sistemas/api/app/$id/delete"
                                            .httpDelete(parametros)
                                            .responseString { request, response, result ->
                                                when (result) {
                                                    is Result.Failure -> {
                                                        val ex = result.getException()
                                                        Log.i("http-p", ex.toString())
                                                        mensaje(contexto,"error","Datos no validos")

                                                    }
                                                    is Result.Success -> run {
                                                        val data = result.get()
                                                        Log.i("http-p", data)
                                                        mensaje(contexto,"Aceptado","Datos validos, espere...")
                                                        contexto.refrescar()
                                                    }
                                                }
                                            }




                                }
                        )



                        true
                    }

                    R.id.editar_so ->{
                        val id = holder.idAppTextView.text.toString()
                        val app = aplicaciones.filter { it.id==id.toInt() }[0]
                        Log.i("Actualizar SO->",app.fechaLanzamiento)
                        val appSerializada = AplicacionSe(
                                id.toInt(),
                                nombre = app.nombre,
                                version = app.version,
                                fechaLanzamiento = app.fechaLanzamiento,
                                peso_gigas = app.peso_gigas,
                                costo = app.costo,
                                url_descargar = app.url_descargar,
                                codigo_barras = app.codigo_barras,
                                sistemaOperativo = app.sistemaOperativo
                        )
                        contexto.irActualizar(appSerializada)
                        //handle menu2 click
                        true
                    }

                    R.id.compartir_so ->{
                        val nombre = holder.nombreTextView.text.toString()
                        contexto.compartir(nombre)
                        //handle menu3 click
                        true
                    }

                    else -> false
                }
            }
            //displaying the popup
            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return listaAplicaciones.size
    }


}










