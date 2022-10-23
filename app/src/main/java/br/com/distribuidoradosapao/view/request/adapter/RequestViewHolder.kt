package br.com.distribuidoradosapao.view.request.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.model.Request

class RequestViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(request: Request) {
        view.findViewById<TextView>(R.id.tv_amount).text = request.amount.toString()
        view.findViewById<TextView>(R.id.tv_nome_cliente).text = request.nameProduct
        view.findViewById<TextView>(R.id.tv_value).text = "R$ ".plus("%.2f".format(request.valueTotal))
    }
}