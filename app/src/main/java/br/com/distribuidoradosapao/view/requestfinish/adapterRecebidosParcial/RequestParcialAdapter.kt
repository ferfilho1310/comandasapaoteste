package br.com.distribuidoradosapao.view.requestfinish.adapterRecebidosParcial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.model.PedidoRecebidoParcial
import br.com.distribuidoradosapao.view.client.adapter.ClientRequestFinishViewHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class RequestParcialAdapter(
    options: FirestoreRecyclerOptions<PedidoRecebidoParcial>
) : FirestoreRecyclerAdapter<PedidoRecebidoParcial, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestParcialViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_pedidos_recebidos_parcial, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        model: PedidoRecebidoParcial
    ) {
        val clientViewHolder = holder as RequestParcialViewHolder
        clientViewHolder.bind(model)
    }
}