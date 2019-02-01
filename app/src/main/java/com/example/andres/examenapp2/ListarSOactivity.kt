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
import kotlinx.android.synthetic.main.activity_listar_soactivity.*
import android.support.v7.widget.PopupMenu
import android.widget.Button


class ListarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_soactivity)

        //BDD.crearMas()

        val layoutManager = LinearLayoutManager(this)
        val rv = rv_so
        val adaptador = SistemaOpAdaptador(BDD.sistemasOperativos, this, rv)

        rv_so.layoutManager = layoutManager
        rv_so.itemAnimator = DefaultItemAnimator()
        rv_so.adapter = adaptador

        adaptador.notifyDataSetChanged()

    }
}




class SistemaOpAdaptador(private val listaSistemaOperativos: List<SistemaOperativo>,
                         private val contexto: ListarActivity,
                         private val recyclerView: RecyclerView) :
        RecyclerView.Adapter<SistemaOpAdaptador.MyViewHolder>() {


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nombreTextView: TextView
        var versionTextView: TextView
        var opciones:Button

        init {
            nombreTextView = view.findViewById(R.id.txt_nombre_so) as TextView
            versionTextView = view.findViewById(R.id.txt_version_so) as TextView
            opciones = view.findViewById(R.id.btn_opciones) as Button



            // val left = apellido.paddingLeft
            // val top = apellido.paddingTop
            // Log.i("vista-principal", "Hacia la izquierda es $left y hacia arriba es $top")

            val layout = view.findViewById(R.id.relative_layout_so) as RelativeLayout

            layout
                    .setOnClickListener {
                        val nombreActual = it.findViewById(R.id.txt_nombre_so) as TextView

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
                        R.layout.recycler_view_so_item,
                        parent,
                        false
                )

        return MyViewHolder(itemView)
    }

    // Llenamos los datos del layout
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val persona = listaSistemaOperativos[position]

        holder.nombreTextView.setText(persona.nombre)
        holder.versionTextView.setText(persona.version)
        holder.opciones.setOnClickListener {
            val popup = PopupMenu(contexto, holder.nombreTextView)
            popup.inflate(R.menu.options_menu)
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.getItemId()) {
                    R.id.eliminar_so ->
                        //handle menu1 click
                        true
                    R.id.editar_so ->
                        //handle menu2 click
                        true
                    R.id.compartir_so ->
                        //handle menu3 click
                        true
                    else -> false
                }
            }
            //displaying the popup
            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return listaSistemaOperativos.size
    }

}










