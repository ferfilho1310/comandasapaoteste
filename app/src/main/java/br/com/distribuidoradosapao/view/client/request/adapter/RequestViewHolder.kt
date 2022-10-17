package br.com.distribuidoradosapao.view.client.request.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.model.Request

class RequestViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(request: Request) {
        view.findViewById<TextView>(R.id.tv_amount).text = request.amount
        view.findViewById<TextView>(R.id.tv_nome_cliente).text = request.nameProduct
        view.findViewById<TextView>(R.id.tv_value).text = "R$ ".plus(request.valueTotal)
    }
}