package br.com.distribuidoradosapaoteste.view.requestforfinish.adapterRecebidosParcial

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapaoteste.R
import br.com.distribuidoradosapaoteste.model.RequestReceivedPartial

class RequestParcialViewHolder(var view: View): RecyclerView.ViewHolder(view) {

    fun bind(requestReceivedPartial: RequestReceivedPartial) {
        view.findViewById<TextView>(R.id.tv_name).text = requestReceivedPartial.name
        view.findViewById<TextView>(R.id.tv_value).text = "R$ ".plus("%.2f".format(requestReceivedPartial.value))
    }
}