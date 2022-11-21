package br.com.distribuidoradosapaoteste.view.request.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapaoteste.R
import br.com.distribuidoradosapaoteste.model.Request

class RequestFinishViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(request: Request) {
        view.findViewById<TextView>(R.id.tv_amount).text = request.amount.toString()
        view.findViewById<TextView>(R.id.tv_name_product).text = request.nameProduct
        view.findViewById<TextView>(R.id.tv_value).text = "R$ ".plus("%.2f".format(request.valueTotal))
    }
}