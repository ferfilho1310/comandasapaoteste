package br.com.distribuidoradosapao.view.client.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.model.Client

class ClientViewHolder(val view: View) :
    RecyclerView.ViewHolder(view) {

    fun bind(client: Client) {
        view.findViewById<TextView>(R.id.tv_data).text = client.date
        view.findViewById<TextView>(R.id.tv_nome_cliente).text = client.name
    }
}