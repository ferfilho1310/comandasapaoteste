package br.com.distribuidoradosapao.view.requestforfinish.adapterRecebidosParcial

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.model.PedidoRecebidoParcial

class RequestParcialViewHolder(var view: View): RecyclerView.ViewHolder(view) {

    fun bind(pedidoRecebidoParcial: PedidoRecebidoParcial) {
        view.findViewById<TextView>(R.id.tv_name).text = pedidoRecebidoParcial.name
        view.findViewById<TextView>(R.id.tv_value).text = "R$ ".plus("%.2f".format(pedidoRecebidoParcial.value))
    }
}